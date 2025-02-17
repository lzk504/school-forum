package com.example.entity.vo.response;

import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TopicPreviewVO {
    int id;
    int type;
    String title;
    String text;
    List<String> images;
    Date time;
    Integer uid;
    String username;
    String avatar;
}
