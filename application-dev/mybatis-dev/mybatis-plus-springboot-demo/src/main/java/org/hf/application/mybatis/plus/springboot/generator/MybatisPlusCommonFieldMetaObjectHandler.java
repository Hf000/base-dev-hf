package org.hf.application.mybatis.plus.springboot.generator;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p> 基于mybatis-plus的对于公共字段元数据统一处理 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/15 9:55
 */
@Component
public class MybatisPlusCommonFieldMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createdDate = getFieldValByName("createdDate", metaObject);
        Object updatedDate = getFieldValByName("updatedDate", metaObject);
        if (createdDate == null){
            strictInsertFill(metaObject, "createdDate", Date.class, new Date());
        }
        if (updatedDate == null){
            strictInsertFill(metaObject, "updatedDate", Date.class, new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updatedDate", Date.class, new Date());
    }
}
