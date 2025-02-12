package com.example.service;

import com.example.entity.vo.response.WeatherVO;

public interface WeatherService {
    /**
     * 获取指定经纬度位置的天气信息
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 返回天气信息的对象 WeatherVO
     */
    WeatherVO fetchWeather(double longitude, double latitude);
}
