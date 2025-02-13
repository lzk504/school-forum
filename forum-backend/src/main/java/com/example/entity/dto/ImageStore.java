package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db-image-store")
@AllArgsConstructor
public class ImageStore implements BaseData {
    Integer uid;
    String name;
    Date time;
}
