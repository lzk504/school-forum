package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Notification;
import com.example.entity.vo.response.NotificationVO;

import java.util.List;

public interface NotificationService extends IService<Notification> {

    /**
     * 获取指定用户的通知列表
     */
    List<NotificationVO> findUserNotification(int uid);

    /**
     * 删除指定用户的指定通知
     *
     * @param id  通知ID
     * @param uid 用户ID
     */
    void deleteUserNotification(int id, int uid);

    /**
     * 删除指定用户的所有通知
     *
     * @param uid 用户ID
     */
    void deleteUserAllNotification(int uid);

    /**
     * 添加通知
     *
     * @param uid     用户ID
     * @param title   通知标题
     * @param content 通知内容
     * @param type    通知类型
     * @param url     通知链接
     */
    void addNotification(int uid, String title, String content, String type, String url);
}
