package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.TopicTypeVO;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.TopicTypeService;
import com.example.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    private WeatherService weatherService;

    @Resource
    private TopicTypeService topicTypeService;

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
        return RestBean.success(topicTypeService.getTopicTypes()
                .stream()
                .map(type -> type.asViewObject(TopicTypeVO.class))
                .toList());
    }
}
