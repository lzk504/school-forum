package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Interact;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.*;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    private WeatherService weatherService;

    @Resource
    private TopicService topicService;

    /**
     * 获取指定地理位置的天气信息
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 包含天气信息的RestBean对象
     */
    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longitude, double latitude) {
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo == null ?
                RestBean.failure(400, "获取地理位置信息与天气失败，请联系管理员！") : RestBean.success(vo);
    }

    /**
     * 获取所有话题类型列表，并转换为视图对象返回。
     *
     * @return 包含所有话题类型视图对象的RestBean对象，状态码为200表示成功。
     */
    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> listTypes() {
        return RestBean.success(topicService.getTopicTypes()
                .stream()
                .map(type -> type.asViewObject(TopicTypeVO.class))
                .toList());
    }

    /**
     * 创建帖子
     *
     * @param vo  包含帖子创建信息的视图对象
     * @param uid 用户ID，通过请求属性获取
     * @return 包含操作结果的RestBean对象，如果成功则无内容，如果失败则包含错误消息
     */
    @PostMapping("/create-topics")
    public RestBean<Void> createTopics(@Valid @RequestBody TopicCreateVO vo
            , @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        return RestBean.messageHandle(() -> topicService.createTopic(uid, vo));
    }

    /**
     * 获取帖子列表
     *
     * @param page 页码，从1开始
     * @param type 类型，从1开始
     * @return 包含帖子列表的RestBean对象
     */
    @GetMapping("/list-topic")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type) {
        return RestBean.success(topicService.listTopicByPage(page + 1, type));
    }

    /**
     * 获取热门帖子列表
     *
     * @return 包含热门帖子列表的RestBean对象
     */
    @GetMapping("/top-topic")
    public RestBean<List<TopicTopVO>> topTopic() {
        return RestBean.success(topicService.listTopicTop());
    }

    /**
     * 根据帖子ID获取帖子详情
     *
     * @param tid 帖子ID，必须为非负整数
     * @return 包含帖子详情的RestBean对象
     */
    @GetMapping("/topic-detail")
    public RestBean<TopicDetailVO> topicDetail(@RequestParam @Min(0) int tid,
                                               @RequestAttribute(Const.ATTR_USER_ID) int uid
    ) {
        return RestBean.success(topicService.getTopicDetail(tid, uid));
    }

    /**
     * 处理用户与帖子的互动操作
     *
     * @param tid   帖子ID，必须为非负整数
     * @param type  互动类型，只能是"like"或"collect"
     * @param state 互动状态，true表示进行互动，false表示取消互动
     * @param uid   当前用户ID，从请求属性中获取
     * @return 返回包含操作结果的RestBean对象
     */
    @GetMapping("/interact")
    public RestBean<Void> interact(@RequestParam @Min(0) int tid,
                                   @RequestParam @Pattern(regexp = "(like|collect)") String type,
                                   @RequestParam boolean state,
                                   @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        topicService.interact(new Interact(tid, uid, new Date(), type), state);
        return RestBean.success();
    }

    /**
     * 获取用户的收藏列表
     *
     * @param uid 用户ID，通过@RequestAttribute注解从请求属性中获取
     */
    @GetMapping("/collects")
    public RestBean<List<TopicPreviewVO>> collects(@RequestAttribute(Const.ATTR_USER_ID) int uid) {
        return RestBean.success(topicService.listTopicCollects(uid));
    }

    /**
     * 更新帖子信息
     *
     * @param uid 用户ID，通过@RequestAttribute注解从请求属性中获取
     * @param vo  包含要更新的帖子信息的话题更新对象，通过@RequestBody注解从请求体中获取，并通过@Valid注解进行校验
     * @return 包含操作结果的RestBean对象，其中状态码和消息内容通过RestBean.messageHandle方法生成
     */
    @PostMapping("/update-topic")
    public RestBean<Void> updateTopic(@RequestAttribute(Const.ATTR_USER_ID) int uid,
                                      @Valid @RequestBody TopicUpdateVO vo
    ) {
        return RestBean.messageHandle(() -> topicService.updateTopic(uid, vo));
    }

    /**
     * 添加评论
     *
     * @param vo  包含评论信息的评论添加对象，通过@RequestBody注解从请求体中获取，并通过@Valid注解进行校验
     * @param uid 用户ID，通过@RequestAttribute注解从请求属性中获取
     * @return 包含操作结果的RestBean对象，其中状态码和消息内容通过RestBean.messageHandle方法生成
     */
    @PostMapping("/add-comment")
    public RestBean<Void> addComment(@Valid @RequestBody AddCommentVO vo,
                                     @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        return RestBean.messageHandle(() -> topicService.createComment(uid, vo));
    }

    /**
     * 获取指定帖子的评论列表
     *
     * @param tid  帖子D，通过@RequestParam注解从请求参数中获取，且该参数的值必须大于等于0
     * @param page 页码，通过@RequestParam注解从请求参数中获取，且该参数的值必须大于等于0
     * @return 包含指定帖子的评论列表的RestBean对象，其中状态码为200表示成功，数据部分包含评论列表
     */
    @GetMapping("/comments")
    public RestBean<List<CommentVO>> comments(@RequestParam @Min(0) int tid,
                                              @RequestParam @Min(0) int page
    ) {
        return RestBean.success(topicService.listCommentsByPage(tid, page + 1));
    }
}
