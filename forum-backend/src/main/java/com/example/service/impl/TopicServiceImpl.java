package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;
import com.example.mapper.TopicMapper;
import com.example.mapper.TopicTypeMapper;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private TopicTypeMapper topicTypeMapper;

    @Resource
    FlowUtils flowUtils;

    // 话题类型集合，用于快速检查话题类型是否存在。
    private Set<Integer> types = null;
    @Resource
    private TopicMapper topicMapper;

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
        if (!textLimitCheck(vo.getContent()))
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
     * 按页获取话题列表
     * <p>
     * 根据给定的页码和话题类型，从数据库中查询对应的话题列表，并转换为TopicPreviewVO对象列表返回。
     *
     * @param page 页码，从0开始
     * @param type 话题类型，0表示获取所有类型的话题
     * @return 包含话题列表的TopicPreviewVO对象集合，如果没有找到对应的话题，则返回null
     */
    @Override
    public List<TopicPreviewVO> listTopicByPage(int page, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + page + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeFromCacheList(key, TopicPreviewVO.class);
        if (list != null) return list;
        List<Topic> topics;
        if (type == 0)
            topics = topicMapper.topicList(page * 10);
        else
            topics = topicMapper.topicListByType(page * 10, type);
        if (topics.isEmpty()) return null;
        list = topics.stream().map(this::resolveTopicPreview).toList();
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
     * 将Topic对象转换为TopicPreviewVO对象
     * 将Topic对象的属性复制到TopicPreviewVO对象中，并提取Topic对象中的文本和图片信息进行处理。
     * 对于文本信息，提取出前300个字符作为预览文本；对于图片信息，提取出所有图片的地址。
     *
     * @param topic Topic对象
     * @return 转换后的TopicPreviewVO对象
     */
    private TopicPreviewVO resolveTopicPreview(Topic topic) {
        TopicPreviewVO vo = new TopicPreviewVO();
        BeanUtils.copyProperties(topic, vo);
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        for (Object o : ops) {
            Object insert = JSONObject.from(o).get("insert");
            if (insert instanceof String text) {
                if (previewText.length() >= 300) continue;
                previewText.append(text);
            } else if (insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(obj -> images.add(obj.toString()));
            }
        }
        vo.setText(previewText.length() > 300 ? previewText.substring(0, 300) : previewText.toString());
        vo.setImages(images);
        return vo;

    }

    /**
     * 检查文本长度是否超过限制。
     *
     * @param obj 包含文本信息的JSONObject对象
     * @return 如果文本长度未超过限制，则返回true；否则返回false
     */
    private boolean textLimitCheck(JSONObject obj) {
        if (obj == null) return false;
        long length = 0;
        for (Object o : obj.getJSONArray("ops")) {
            length += JSONObject.from(o).getString("insert").length();
            if (length > 20000)
                return false;
        }
        return true;
    }
}
