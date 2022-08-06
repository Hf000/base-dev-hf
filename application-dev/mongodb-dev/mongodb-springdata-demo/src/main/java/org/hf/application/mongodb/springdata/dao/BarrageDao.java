package org.hf.application.mongodb.springdata.dao;

import org.hf.application.mongodb.springdata.pojo.entity.Barrage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> mongoDB数据库dao </p>
 * {@link MongoRepository}基础mongoDB的父接口
 * @author hufei
 * @date 2022/8/6 21:05
*/
public interface BarrageDao extends MongoRepository<Barrage, Integer> {

    /**根据内容模糊查询**/
    List<Barrage> findByContentLike(String context);

    /**根据时间查询，大于什么时间的数据**/
    List<Barrage> findByTimeGreaterThan(String time);

}
