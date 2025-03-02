package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

    /**
     * 批量插入互动记录到数据库中。
     *
     * @param interacts 包含互动记录的列表
     * @param type      互动类型，用于决定插入到哪个数据表
     *                  <p>
     *                  使用MyBatis的@Insert注解，通过动态SQL脚本批量插入互动记录到指定的数据表中。
     *                  动态SQL脚本中使用了<foreach>标签来遍历interacts列表，并将每个互动记录插入到数据表中。
     *                  插入时使用了insert ignore语句，以避免因重复插入而导致的错误。
     */
    @Insert("""
            <script>
                insert ignore into db_topic_interact_${type} values
                <foreach collection ="interacts" item="item" separator =",">
                    (#{item.tid}, #{item.uid}, #{item.time})
                </foreach>
            </script>
            """)
    void addInteract(List<Interact> interacts, String type);

    /**
     * 从数据库中删除指定类型的互动记录。
     *
     * @param interacts 包含要删除的互动记录的列表
     * @param type      互动类型，用于确定从哪个表中删除记录
     *                  <p>
     *                  使用MyBatis的@Delete注解，通过动态SQL脚本从指定的数据表中删除互动记录。
     *                  动态SQL脚本中使用了<foreach>标签来遍历interacts列表，并为每个互动记录构建一个删除条件。
     *                  删除条件由tid和uid组成，表示要删除的记录的主键。
     *                  使用or关键字将多个删除条件连接起来，以便一次性删除多个记录。
     */
    @Delete("""
            <script>
                delete from db_topic_interact_${type} where
                <foreach collection="interacts" item="item" separator=" or ">
                    (tid = #{item.tid} and uid = #{item.uid})
                </foreach>
            </script>
            """)
    void deleteInteract(List<Interact> interacts, String type);
}


