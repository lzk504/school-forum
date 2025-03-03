package com.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Interact {
    Integer tid;
    Integer uid;
    Date time;
    String type;

    /**
     * 将用户ID和话题ID组合成一个唯一的键。
     *
     * @return 用户ID和话题ID组合成的唯一键
     */
    public String toKey() {
        return tid + ":" + uid;
    }


    /**
     * 解析包含用户ID、帖子D和互动类型的字符串，并创建一个Interact对象。
     *
     * @param str  字符串，格式为"用户ID:话题ID"，用冒号分隔
     * @param type 互动类型，如"like"或"collect"
     * @return 创建的Interact对象
     * @throws NumberFormatException 如果字符串中的用户ID或帖子ID无法转换为整数
     */
    public static Interact parseInteract(String str, String type) {
        String[] keys = str.split(":");
        return new Interact(Integer.parseInt(keys[0]),
                Integer.parseInt(keys[1]), new Date(), type);
    }
}
