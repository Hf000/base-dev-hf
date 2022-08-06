package org.hf.application.mongodb.springdata.pojo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p> mongoDB数据库对应实体 </p>
 * //@Document("Barrage")  //使用此注解建立该实体类和mongoDB数据中collection的映射关系
 * @author hufei
 * @date 2022/8/6 21:04
*/
@Document("Barrage")
@Data
public class Barrage {
    @Id//用来标识主键
    private int id;
//    @Field//此注解来建立实体中属性和collection中document的字段关系，如果省略标识名称保持一致
    private String content;
    private String time;
}
