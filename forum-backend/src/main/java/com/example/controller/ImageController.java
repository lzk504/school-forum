package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Resource
    private ImageService imageService;

    /**
     * 上传用户头像
     *
     * @param file 上传的图片文件
     * @param id   用户ID
     * @return 包含上传结果的RestBean对象
     * @throws IOException 如果文件读取或处理过程中发生IO异常
     */
    @PostMapping("/avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id
    ) throws IOException {
        if (file.isEmpty())
            return RestBean.failure(400, "未上传图片");
        if (file.getSize() > 1024 * 100)
            return RestBean.failure(400, "头像图片大小不能大于100KB");
        log.info("正在进行头像上传操作");
        String url = imageService.uploadAvatar(file, id);
        if (url != null) {
            log.info("头像上传成功，头像大小:{} kb", file.getSize() / 1024);
            return RestBean.success(url);
        } else {
            return RestBean.failure(400, "头像上传失败，请联系系统管理员");
        }
    }

    /**
     * 上传图片到缓存服务器
     *
     * @param file 上传的图片文件
     * @param id   用户ID
     * @return 包含上传结果的RestBean对象
     * @throws IOException 如果文件读取或处理过程中发生IO异常
     */
    @PostMapping("/cache")
    public RestBean<String> uploadImage(@RequestParam("file") MultipartFile file,
                                        @RequestAttribute(Const.ATTR_USER_ID) int id,
                                        HttpServletResponse response
    ) throws IOException {
        if (file.isEmpty())
            return RestBean.failure(400, "未上传图片");
        if (file.getSize() > 1024 * 1024 * 5)
            return RestBean.failure(400, "图片大小不能大于5MB");
        log.info("正在进行图片缓存操作");
        String url = imageService.uploadImage(file, id);
        if (url != null) {
            log.info("图片缓存成功，图片大小:{} kb", file.getSize() / 1024 );
            return RestBean.success(url);
        } else {
            response.setStatus(400);
            return RestBean.failure(400, "图片缓存失败，请联系系统管理员");
        }
    }

}

