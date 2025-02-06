package com.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 响应实体类封装，Rest风格
 *
 * @param code    状态码
 * @param data    响应数据
 * @param message 其他消息
 * @param <T>     响应数据类型
 */
public record RestBean<T>(long id, int code, T data, String message) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(requestId(), 200, data, "请求成功");
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message);
    }

    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(requestId(), code, null, message);
    }

    /**
     * 处理消息并返回RestBean对象
     *
     * @param action 一个提供字符串的Supplier接口，用于执行某个操作并返回消息
     * @param <T>    泛型类型，表示RestBean中泛型对象的类型
     * @return 如果action返回的消息为null，返回状态码为200的RestBean对象；否则，返回状态码为400的RestBean对象，并附带action返回的消息
     */
    public static <T> RestBean<T> messageHandle(Supplier<String> action) {
        String message = action.get();
        if (message == null) {
            return success();
        } else {
            return failure(400, message);
        }
    }

    /**
     * 快速将当前实体转换为JSON字符串格式
     *
     * @return JSON字符串
     */
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    /**
     * 获取当前请求ID方便快速定位错误
     *
     * @return ID
     */
    private static long requestId() {
        String requestId = Optional.ofNullable(MDC.get("reqId")).orElse("0");
        return Long.parseLong(requestId);
    }
}
