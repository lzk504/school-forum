package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ObjectController {

    @Resource
    private ImageService imageService;

    /**
     * 处理获取头像图片的请求
     */
    @GetMapping("/images/avatar/**")
    public void imageFetch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.fetchImage(req, resp);
    }


    /**
     * 从Minio服务器获取图片并返回给客户端
     *
     * @param req  HttpServletRequest对象，用于获取请求信息
     * @param resp HttpServletResponse对象，用于向客户端返回响应
     * @throws Exception 如果在获取图片或返回响应过程中出现异常，则抛出异常
     */
    private void fetchImage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String imagePath = req.getServletPath().substring(7);
        ServletOutputStream stream = resp.getOutputStream();
        if (imagePath.length() <= 13) {
            resp.setStatus(404);
            stream.println(RestBean.failure(404, "Not Found").toString());
        } else {
            try {
                imageService.fetchImageFormMinio(stream, imagePath);
                //设置默认缓一个月
                resp.setHeader("Cache-Control", "max-age=2592000");
            } catch (ErrorResponseException e) {
                if (e.response().code() == 404) {
                    resp.setStatus(404);
                    stream.println(RestBean.failure(404, "Not Found").toString());
                } else {
                    log.error("从Minio获取图片出现异常: {}", e.getMessage(), e);
                }
            }
        }
    }
}
