package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.TopicType;

import java.util.List;

public interface TopicTypeService extends IService<TopicType> {

    /**
     * 获取所有话题类型。
     */
    List<TopicType> getTopicTypes();
}
