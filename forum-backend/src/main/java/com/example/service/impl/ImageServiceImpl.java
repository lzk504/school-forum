package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.ImageStore;
import com.example.mapper.AccountMapper;
import com.example.mapper.ImageStoreMapper;
import com.example.service.ImageService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import io.minio.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageStoreMapper, ImageStore> implements ImageService {

    @Resource
    MinioClient client;

    @Resource
    AccountMapper accountMapper;

    @Resource
    FlowUtils flowUtils;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 上传头像
     *
     * @param file 上传的文件
     * @param id   用户ID
     * @return 上传结果字符串
     * @throws IOException 如果文件上传过程中发生I/O错误，则抛出该异常
     */
    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("forum")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            client.putObject(args);
            String avatar = accountMapper.selectById(id).getAvatar();
            this.deleteOldAvatar(avatar);
            if (accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id", id).set("avatar", imageName)) > 0) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出现问题: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 删除旧的头像
     *
     * @param avatar 需要删除的头像的路径
     * @throws Exception 如果删除头像时发生异常
     */
    private void deleteOldAvatar(String avatar) throws Exception {
        if (avatar == null || avatar.isEmpty()) return;
        RemoveObjectArgs remove = RemoveObjectArgs.builder()
                .bucket("forum")
                .object(avatar)
                .build();
        client.removeObject(remove);
    }

    /**
     * 从Minio服务器获取图片并写入输出流
     *
     * @param stream 输出流，用于存储从Minio服务器获取的图片数据
     * @param image  图片的文件名
     * @throws Exception 如果在获取图片或写入输出流过程中发生异常，则抛出异常
     */
    @Override
    public void fetchImageFormMinio(OutputStream stream, String image) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("forum")
                .object(image)
                .build();
        GetObjectResponse response = client.getObject(args);
        IOUtils.copy(response, stream);
    }

    /**
     * 上传图片
     *
     * @param file 要上传的图片文件
     * @return 上传后的图片路径
     * @throws IOException 如果文件读取或上传过程中发生输入输出异常
     */
    @Override
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String key = Const.FORUM_IMAGE_COUNTER + id;
        if (flowUtils.limitPeriodCounterCheck(key, 20, 3600))
            return null;
        String imageName = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        imageName = "/cache/" + sdf.format(date) + "/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("forum")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            client.putObject(args);
            if (this.save(new ImageStore(id, imageName, date)))
                return imageName;
            else
                return null;
        } catch (Exception e) {
            log.error("图片上传出现问题: {}", e.getMessage(), e);
            return null;
        }
    }
}
