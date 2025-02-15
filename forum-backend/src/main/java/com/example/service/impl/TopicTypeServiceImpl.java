package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.TopicType;
import com.example.mapper.TopicTypeMapper;
import com.example.service.TopicTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicTypeServiceImpl extends ServiceImpl<TopicTypeMapper, TopicType> implements TopicTypeService {

    @Resource
    private TopicTypeMapper topicTypeMapper;

    /**
     * 获取所有话题类型。
     *
     * @return 包含所有话题类型的列表。
     */
    @Override
    public List<TopicType> getTopicTypes() {
        return topicTypeMapper.selectList(null);
    }
}
