package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;

import java.util.List;

public interface TopicService extends IService<Topic> {
    /**
     * 获取所有话题类型。
     */
    List<TopicType> getTopicTypes();


    /**
     * 创建一个新帖子。
     *
     * @param uid 用户ID
     * @param vo  话题创建视图对象，包含话题标题、内容等信息
     * @return 创建成功返回话题ID的字符串表示，失败则返回null
     */
    String createTopic(int uid, TopicCreateVO vo);

    /**
     * 按页获取话题列表
     */
    List<TopicPreviewVO> listTopicByPage(int page, int type);

    /**
     * 获取置顶帖子列表
     */
    List<TopicTopVO> listTopicTop();

    /**
     * 获取帖子详情
     */
    TopicDetailVO getTopicDetail(int tid, int uid);


    /**
     * 处理用户与帖子的点赞收藏操作
     */
    void interact(Interact interact, boolean state);


    /**
     * 获取指定用户的收藏列表
     */
    List<TopicPreviewVO> listTopicCollects(int uid);

    /**
     * 更新帖子信息
     */
    String updateTopic(int uid, TopicUpdateVO vo);
}
