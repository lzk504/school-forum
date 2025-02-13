package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.ImageStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageService extends IService<ImageStore> {

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


    /**
     * 上传图片
     *
     * @param file 要上传的图片文件
     * @return 上传后的图片路径
     * @throws IOException 如果文件读取或上传过程中发生输入输出异常
     */
    String uploadImage(MultipartFile file,int id) throws IOException;
}
