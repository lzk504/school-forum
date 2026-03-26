package com.example.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.CommentVO;
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

     */
    String createTopic(int uid, TopicCreateVO vo);

    /**
     * 查询所有帖子管理员
     */

    JSONObject listAllTopicByPage(int page,int size);

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


    /**
     * 创建新评论
     */
    String createComment(int uid, AddCommentVO vo);

    /**
     * 获取指定话题的评论列表
     */
    List<CommentVO> listCommentsByPage(int tid, int pageNumber);


    /**
     * 删除指定话题的指定用户的评论
     */
    void deleteComment(int id, int uid);

    /**
     * 删除帖子
     */
    void deleteTopic(int id);

    /**
     * 设置帖子置顶
     */
    void setTopicTop(int id, boolean status);

    /**
     * 设置帖子锁定
     */
    void setTopicLocked(int id, boolean status);

    /**
     * 设置帖子屏蔽
     */
    void setTopicInvisible(int id, boolean status);
}

