package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.TopicCreateVO;

import java.util.List;

public interface TopicService extends IService<Topic> {
    /**
     * 获取所有话题类型。
     */
    List<TopicType> getTopicTypes();


    /**
     * 创建一个新帖子。
     *
     * @param id 用户ID
     * @param vo 话题创建视图对象，包含话题标题、内容等信息
     * @return 创建成功返回话题ID的字符串表示，失败则返回null
     */
    String createTopic(int uid, TopicCreateVO vo);
}
