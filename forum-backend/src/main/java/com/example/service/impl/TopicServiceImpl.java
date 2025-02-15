package com.example.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.mapper.TopicMapper;
import com.example.mapper.TopicTypeMapper;
import com.example.service.TopicService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private TopicTypeMapper topicTypeMapper;

    @Resource
    FlowUtils flowUtils;

    // 话题类型集合，用于快速检查话题类型是否存在。
    private Set<Integer> types = null;

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
        if (this.save(topic))
            return null;
        else return "内部错误请联系管理员";
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
