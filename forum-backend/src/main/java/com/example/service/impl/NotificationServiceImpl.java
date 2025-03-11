package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Notification;
import com.example.entity.vo.response.NotificationVO;
import com.example.mapper.NotificationMapper;
import com.example.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    /**
     * 获取指定用户的通知列表
     *
     * @param uid 用户ID
     * @return 包含指定用户的通知信息的NotificationVO对象列表
     */
    @Override
    public List<NotificationVO> findUserNotification(int uid) {
        return this.list(Wrappers.<Notification>query().eq("uid", uid))
                .stream()
                .map(notification -> notification.asViewObject(NotificationVO.class))
                .toList();
    }

    /**
     * 删除指定用户的指定通知
     *
     * @param id  通知ID
     * @param uid 用户ID
     */
    @Override
    public void deleteUserNotification(int id, int uid) {
        this.remove(Wrappers.<Notification>query().eq("id", id).eq("uid", uid));
    }

    /**
     * 删除指定用户的所有通知
     *
     * @param uid 用户ID
     */
    @Override
    public void deleteUserAllNotification(int uid) {
        this.remove(Wrappers.<Notification>query().eq("uid", uid));
    }

    /**
     * 添加通知
     *
     * @param uid     用户ID
     * @param title   通知标题
     * @param content 通知内容
     * @param type    通知类型
     * @param url     通知链接
     */
    @Override
    public void addNotification(int uid, String title, String content, String type, String url) {
        Notification notification = new Notification();
        notification.setUid(uid);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setUrl(url);
        this.save(notification);
    }
}
