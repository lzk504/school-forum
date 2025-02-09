package com.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageService {

    /**
     * 上传头像
     *
     * @param file 上传的文件
     * @param id   用户ID
     * @return 上传结果字符串
     */
    String uploadAvatar(MultipartFile file, int id) throws IOException;

    /**
     * 从Minio服务器获取图片并写入输出流
     *
     * @param stream 输出流，用于存储从Minio服务器获取的图片数据
     * @param image  图片的文件名
     * @throws Exception 如果在获取图片或写入输出流过程中发生异常，则抛出异常
     */
    void fetchImageFormMinio(OutputStream stream, String image) throws Exception;
}
