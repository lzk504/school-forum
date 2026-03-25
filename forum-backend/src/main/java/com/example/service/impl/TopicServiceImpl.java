package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.CommentVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.TopicCommentMapper;
import com.example.mapper.TopicMapper;
import com.example.mapper.TopicTypeMapper;
import com.example.service.NotificationService;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.CommonUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private TopicTypeMapper topicTypeMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private FlowUtils flowUtils;

    @Resource
    private CommonUtils commonUtils;

    @Resource
    private StringRedisTemplate template;

    @Resource
    private TopicCommentMapper topicCommentMapper;

    @Resource
    private TopicCommentMapper commentMapper;

    @Resource
    private NotificationService notificationService;


    // 话题类型集合，用于快速检查话题类型是否存在。
    private Set<Integer> types = null;

    @Resource
    private CacheUtils cacheUtils;

    // 初始化话题类型集合，在对象创建后执行。
    @PostConstruct
    private void initTypes() {
        types = this.getTopicTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }


    /**
     * 获取所有话题类型。
     *
     * @return 包含所有话题类型的列表。
     */
    @Override
    public List<TopicType> getTopicTypes() {
        return topicTypeMapper.selectList(null);
    }

    /**
     * 创建一个新话题。
     *
     * @param uid 用户ID
     * @param vo  话题创建视图对象，包含话题标题、内容、类型等信息
     * @return 如果创建成功则返回null，否则返回错误信息字符串
     */
    @Override
    public String createTopic(int uid, TopicCreateVO vo) {
        if (!textLimitCheck(vo.getContent(), 20000))
            return "文本长度超过限制";
        if (!types.contains(vo.getType()))
            return "话题类型不存在";
        String key = Const.FORUM_TOPIC_CREATE_CONTENT + uid;
        if (!flowUtils.limitPeriodCounterCheck(key, 3, 3600))
            return "发帖频率过高请稍后在试";
        Topic topic = new Topic();
        BeanUtils.copyProperties(vo, topic);
        topic.setContent(vo.getContent().toJSONString());
        topic.setUid(uid);
        topic.setTime(new Date());
        if (this.save(topic)) {
            cacheUtils.deleteCachePattern(Const.FORUM_TOPIC_PREVIEW_CACHE + "*");
            return null;
        } else {
            return "内部错误请联系管理员";
        }
    }

    /**
     * 获取全部帖子列表
     * @param page 页码
     * @param size
     * @return 对象集合
     */
    @Override
    public JSONObject listAllTopicByPage(int page, int size) {
        Page<Topic> topicPage = baseMapper
                .selectPage(Page.of(page, size, true), Wrappers.<Topic>query()
                        .select("id", "title", "uid","type", "time", "top")
                        .orderByDesc("time"));
        List<TopicPreviewVO> list = topicPage.getRecords().stream().map(this::resolveToPreview).toList();
        JSONObject obj = new JSONObject();
        obj.put("total", topicPage.getTotal());
        obj.put("list", list);
        return obj;
    }

    /**
     * 校验并更新帖子信息
     */
    @Override
    public String updateTopic(int uid, TopicUpdateVO vo) {
        if (!textLimitCheck(vo.getContent(), 20000))
            return "文本长度超过限制";
        if (!types.contains(vo.getType()))
            return "话题类型不存在";
        baseMapper.update(null, Wrappers.<Topic>update()
                .eq("uid", uid)
                .eq("id", vo.getId())
                .set("title", vo.getTitle())
                .set("content", vo.getContent().toString())
                .set("type", vo.getType())
        );
        return null;
    }


    /**
     * 创建评论
     *
     * @param uid 用户ID
     * @param vo  包含评论信息的评论添加对象
     * @return 评论内容，如果创建失败则返回空字符串
     */
    @Override
    public String createComment(int uid, AddCommentVO vo) {
        if (!textLimitCheck(JSONObject.parseObject(vo.getContent()), 2000))
            return "评论内容太多，发表失败！";
        String key = Const.FORUM_TOPIC_COMMENT_CONTENT + uid;
        if (!flowUtils.limitPeriodCounterCheck(key, 1, 60))
            return "发评论频率过高请稍后在试";
        TopicComment comment = new TopicComment();
        comment.setUid(uid);
        BeanUtils.copyProperties(vo, comment);
        comment.setTime(new Date());
        topicCommentMapper.insert(comment);
        Topic topic = this.getById(vo.getTid());
        Account account = accountMapper.selectById(uid);
        if (vo.getQuote() > 0) {
            TopicComment topicComment = commentMapper.selectById(vo.getQuote());
            if (!Objects.equals(account.getId(), topicComment.getUid())) {
                notificationService.addNotification(
                        topicComment.getUid(),
                        "您有新的帖子评论回复",
                        account.getUsername() + " 回复了你发表的评论，快去看看吧！",
                        "success", "/index/topic-detail/" + topicComment.getTid()
                );
            }
        } else if (!Objects.equals(account.getId(), topic.getUid())) {
            notificationService.addNotification(
                    topic.getUid(),
                    "您有新的帖子回复",
                    account.getUsername() + " 回复了你发表主题: " + topic.getTitle() + "，快去看看吧！",
                    "success", "/index/topic-detail/" + topic.getId()
            );
        }
        return null;
    }


    /**
     * 按页获取指定帖子的评论列表
     *
     * @param tid        话题ID
     * @param pageNumber 页码
     * @return 包含指定话题的评论列表的CommentVO对象列表
     */
    @Override
    public List<CommentVO> listCommentsByPage(int tid, int pageNumber) {
        Page<TopicComment> page = Page.of(pageNumber, 10);
        topicCommentMapper.selectPage(page, Wrappers.<TopicComment>query()
                .eq("tid", tid));
        return page.getRecords().stream().map(dto -> {
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(dto, vo);
            if (dto.getQuote() > 0) {
                TopicComment comment = commentMapper.selectOne(Wrappers.<TopicComment>query().eq("id", dto.getQuote()).orderByAsc("time"));
                if (comment != null) {
                    JSONObject object = JSONObject.parseObject(comment.getContent());
                    StringBuilder builder = new StringBuilder();
                    this.shortContent(object.getJSONArray("ops"), builder, ignore -> {
                    });
                    vo.setQuote(builder.toString());
                } else {
                    vo.setQuote("此评论已经被删除");
                }
            }
            CommentVO.User user = new CommentVO.User();
            commonUtils.fillUserDetailsByPrivacy(user, dto.getUid());
            vo.setUser(user);
            return vo;
        }).toList();
    }

    /**
     * 删除指定话题的指定用户的评论
     */
    @Override
    public void deleteComment(int id, int uid) {
        commentMapper.delete(Wrappers.<TopicComment>query().eq("id", id).eq("uid", uid));
    }

    /**
     * 按页获取话题列表
     *
     * 根据给定的页码和话题类型，从数据库中查询对应的话题列表，并转换为TopicPreviewVO对象列表返回。
     *
     * @param pageNumber 页码，从0开始
     * @param type       话题类型，0表示获取所有类型的话题
     * @return 包含话题列表的TopicPreviewVO对象集合，如果没有找到对应的话题，则返回null
     */
    @Override
    public List<TopicPreviewVO> listTopicByPage(int pageNumber, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + pageNumber + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeFromCacheList(key, TopicPreviewVO.class);
        if (list != null)
            return list;
        Page<Topic> page = Page.of(pageNumber, 10);
        if (type == 0)
            baseMapper.selectPage(page, Wrappers.<Topic>query().orderByDesc("time"));
        else
            baseMapper.selectPage(page, Wrappers.<Topic>query()
                    .eq("type", type).orderByDesc("time"));
        List<Topic> topics = page.getRecords();
        if (topics.isEmpty()) return null;
        list = topics.stream().map(this::resolveToPreview).toList();
        cacheUtils.saveCacheList(key, list, 60);
        return list;
    }

    /**
     * 获取置顶帖子列表
     */
    @Override
    public List<TopicTopVO> listTopicTop() {
        List<Topic> topics = baseMapper.selectList(Wrappers.<Topic>query().select("id", "title", "time")
                .eq("top", 1));
        return topics.stream().map(topic -> {
            TopicTopVO vo = new TopicTopVO();
            BeanUtils.copyProperties(topic, vo);
            return vo;
        }).toList();
    }

    /**
     * 根据帖子ID获取帖子详情。
     *
     * @param tid 帖子ID
     * @return 帖子详情对象
     */
    @Override
    public TopicDetailVO getTopicDetail(int tid, int uid) {
        TopicDetailVO vo = new TopicDetailVO();
        Topic topic = baseMapper.selectById(tid);
        BeanUtils.copyProperties(topic, vo);
        TopicDetailVO.Interact interact = new TopicDetailVO.Interact(
                checkInteract(tid, uid, "like"),
                checkInteract(tid, uid, "collect")
        );
        vo.setInteract(interact);
        TopicDetailVO.User user = new TopicDetailVO.User();
        vo.setUser(commonUtils.fillUserDetailsByPrivacy(user, topic.getUid()));
        vo.setComments(commentMapper.
                selectCount(Wrappers.<TopicComment>query().eq("tid", tid)));
        return vo;
    }

    /**
     * 处理用户与帖子的点赞收藏操作：
     * 由于论坛交互数据实时到mysql数据库不太现实，所以需要redis缓存交互数据，并在合适时机一次性入库
     * 当数据更像时，会创建一个定时任务，此任务会在一段时间之后执行
     * 将全部缓存的交互数据入库，从而缓解mysql的压力。
     * 在定时任务已经设定期间又有新的更新到来，仅更新redis不会创建新的定时任务。
     *
     * @param interact 包含用户ID和帖子ID的Interact对象
     * @param state    操作状态，true表示点赞或收藏，false表示取消点赞或收藏
     */
    @Override
    public void interact(Interact interact, boolean state) {
        String type = interact.getType();
        synchronized (type.intern()) {
            template.opsForHash().put(type, interact.toKey(), Boolean.toString(state));
            this.saveInteractTask(type);
        }
    }


    /**
     * 获取指定用户的收藏帖子列表（预览版）
     *
     * @param uid 用户ID
     * @return 包含用户收藏帖子预览信息的列表，每个元素为TopicPreviewVO类型
     */
    @Override
    public List<TopicPreviewVO> listTopicCollects(int uid) {
        return baseMapper.collectTopics(uid)
                .stream()
                .map(topics -> {
                    TopicPreviewVO vo = new TopicPreviewVO();
                    BeanUtils.copyProperties(topics, vo);
                    return vo;
                })
                .toList();
    }


    /**
     * 检查指定帖子和用户的互动状态
     *
     * @param tid  话题ID
     * @param uid  用户ID
     * @param type 话题类型
     * @return 如果用户与话题存在互动记录，则返回true；否则返回false
     */
    private boolean checkInteract(int tid, int uid, String type) {
        String key = tid + ":" + uid;
        if (template.opsForHash().hasKey(type, key))
            return Boolean.parseBoolean(template.opsForHash().entries(type).get(key).toString());
        return baseMapper.userInteractCount(tid, uid, type) > 0;

    }

    // 存储当前状态，用于判断是否需要创建新的定时任务
    private final Map<String, Boolean> state = new HashMap<>();

    // 创建定时任务，用于定期保存互动记录到数据库
    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);


    /**
     * 异步保存互动记录到数据库的任务调度方法。
     *
     * @param type 互动类型
     *             <p>
     *             如果指定类型的互动记录尚未被处理（即state中未标记为已处理），则将该类型的互动记录标记为正在处理，
     *             并使用service.schedule方法异步调度一个任务，该任务将在10秒后执行，执行时会调用saveInteract方法保存互动记录到数据库，
     *             然后将指定类型的互动记录标记为已处理。如果指定类型的互动记录已经被处理过，则不会重复处理。
     */
    private void saveInteractTask(String type) {
        if (!state.getOrDefault(type, false)) {
            state.put(type, true);
            service.schedule(() -> {
                this.saveInteract(type);
                state.put(type, false);
            }, 5, TimeUnit.SECONDS);
        }
    }

    /**
     * 保存互动记录到数据库。
     *
     * @param type 互动类型
     *             <p/>
     *             此方法首先通过同步块确保线程安全，然后遍历指定类型的所有互动记录。
     *             对于每个互动记录，根据其值（true或false）将其添加到“check”或“uncheck”列表中。
     *             如果“check”列表不为空，则调用baseMapper的addInteract方法将互动记录添加到数据库中。
     *             如果“uncheck”列表不为空，则调用baseMapper的deleteInteract方法从数据库中删除互动记录。
     *             最后，从缓存中删除指定类型的所有互动记录。
     */
    private void saveInteract(String type) {
        synchronized (type.intern()) {
            List<Interact> check = new LinkedList<>();
            List<Interact> uncheck = new LinkedList<>();
            template.opsForHash().entries(type).forEach((key, value) -> {
                if (Boolean.parseBoolean(value.toString())) {
                    check.add(Interact.parseInteract(key.toString(), type));
                } else {
                    uncheck.add(Interact.parseInteract(key.toString(), type));
                }
            });
            if (!check.isEmpty()) {
                baseMapper.addInteract(check, type);
            }
            if (!uncheck.isEmpty()) {
                baseMapper.deleteInteract(uncheck, type);
            }
            template.delete(type);
        }
    }


    /**
     * 将Topic对象转换为TopicPreviewVO对象
     * 将Topic对象的属性复制到TopicPreviewVO对象中，并提取Topic对象中的文本和图片信息进行处理。
     * 对于文本信息，提取出前300个字符作为预览文本；对于图片信息，提取出所有图片的地址。
     *
     * @param topic Topic对象
     * @return 转换后的TopicPreviewVO对象
     */
    private TopicPreviewVO resolveToPreview(Topic topic) {
        TopicPreviewVO vo = new TopicPreviewVO();
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()), vo);
        BeanUtils.copyProperties(topic, vo);
        vo.setLike(baseMapper.interactCount(topic.getId(), "like"));
        vo.setCollect(baseMapper.interactCount(topic.getId(), "collect"));
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        if (topic.getContent() != null) {
            JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
            this.shortContent(ops, previewText, obj -> images.add(obj.toString()));
        }
        vo.setText(previewText.length() > 300 ? previewText.substring(0, 300) : previewText.toString());
        vo.setImages(images);
        return vo;
    }


    /**
     * 生成短内容预览
     *
     * @param ops          操作数组，包含要生成预览的文本和图片信息
     * @param previewText  用于存储生成的预览文本的StringBuilder对象
     * @param imageHandler 用于处理图片信息的Consumer接口实现
     */
    private void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler) {
        for (Object o : ops) {
            Object insert = JSONObject.from(o).get("insert");
            if (insert instanceof String text) {
                if (previewText.length() >= 300) continue;
                previewText.append(text);
            } else if (insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image")).ifPresent(imageHandler);
            }
        }
    }

    /**
     * 检查文本长度是否超过限制。
     *
     * @param obj 包含文本信息的JSONObject对象
     * @return 如果文本长度未超过限制，则返回true；否则返回false
     */
    private boolean textLimitCheck(JSONObject obj, int limit) {
        if (obj == null) return false;
        long length = 0;
        for (Object o : obj.getJSONArray("ops")) {
            length += JSONObject.from(o).getString("insert").length();
            if (length > limit) return false;
        }
        return true;
    }
}
