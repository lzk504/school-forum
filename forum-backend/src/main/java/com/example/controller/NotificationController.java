package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.NotificationVO;
import com.example.service.NotificationService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {


    @Resource
    private NotificationService notificationService;

    /**
     * 获取指定用户的通知列表
     *
     * @param id 用户ID，通过@RequestAttribute注解从请求属性中获取
     * @return 包含指定用户的通知列表的RestBean对象，其中状态码为200表示成功，数据部分包含通知列表
     */
    @GetMapping("/list")
    public RestBean<List<NotificationVO>> listNotification(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(notificationService.findUserNotification(id));
    }

    /**
     * 删除指定用户的指定通知
     *
     * @param id  通知ID，通过@RequestParam注解从请求参数中获取，且该参数的值必须大于等于0
     * @param uid 用户ID，通过@RequestAttribute注解从请求属性中获取
     * @return 包含操作结果的RestBean对象，其中状态码为200表示成功
     */
    @GetMapping("/delete")
    public RestBean<Integer> deleteNotification(@RequestParam @Min(0) int id, @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        notificationService.deleteUserNotification(id, uid);
        return RestBean.success();
    }

    /**
     * 删除指定用户的所有通知
     *
     * @param uid 用户ID，通过@RequestAttribute注解从请求属性中获取
     * @return 包含操作结果的RestBean对象，其中状态码为200表示成功
     */
    @GetMapping("/delete-all")
    public RestBean<Integer> deleteAllNotification(@RequestAttribute(Const.ATTR_USER_ID) int uid) {
        notificationService.deleteUserAllNotification(uid);
        return RestBean.success();
    }
}
