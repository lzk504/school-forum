package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTypeVO;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

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
     * @param vo  包含话题创建信息的视图对象
     * @param uid 用户ID，通过请求属性获取
     * @return 包含操作结果的RestBean对象，如果成功则无内容，如果失败则包含错误消息
     */
    @PostMapping("/create-topics")
    public RestBean<Void> createTopics(@Valid @RequestBody TopicCreateVO vo
            , @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        return RestBean.messageHandle(() -> topicService.createTopic(uid, vo));
    }

    /**
     * 获取话题列表
     *
     * @param page 页码，从0开始
     * @param type 类型，从0开始
     * @return 包含话题列表的RestBean对象
     */
    @GetMapping("/list-topic")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type) {
        return RestBean.success(topicService.listTopicByPage(page, type));
    }
}
