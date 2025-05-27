package org.hf.boot.springboot.dynamic.statement;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 动态脚本Statement构建类
 */
@Component
@Slf4j
public class DynamicMappedStatementBuilder {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 根据脚本生成MappedStatement
     * @param orginalSql        原始sql脚本，参考mybatis <select>标签内容
     * @param statementId       mybatis映射的xml方法唯一标识, 参考mybatis的xml文件标签id
     * @param resultType        返回类型
     * @param resultMappings    返回列信息集合
     */
    public void addMappedStatement(String orginalSql, String statementId, Class<?> resultType, List<ResultMapping> resultMappings){
        Configuration configuration = sqlSessionFactory.getConfiguration();
        // 1.原始文本添加<script>标签
        String scriptSql = "<script>" + orginalSql + "</script>";
        // 2.借助mybatis XMLLanguageDriver解析script文本生成SqlSource
        XMLLanguageDriver languageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, scriptSql, Map.class);
        // 3. 构建MappedStatement Builder, 参照<select>属性值构建
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, statementId, sqlSource, SqlCommandType.SELECT);
        //4. 如果定义了resultType，加入MappedStatement Builder
        if(Objects.nonNull(resultType)){
            List<ResultMap> resultMaps = getStatementResultMaps(configuration, resultType, statementId, resultMappings);
            statementBuilder.resultMaps(resultMaps);
        }
        // mybatis解析后的单个SQL操作的静态配置模板
        MappedStatement mappedStatement =  statementBuilder.build();
        // 5.添加到Configuration
        configuration.addMappedStatement(mappedStatement);
    }

    @SuppressWarnings("unchecked")
    public void removeMappedStatement(String statementId){
        // Cofiguration无removeMappedStatement方法, 通过反射修改成员变量
        try {
            Field mappedStatementsField = Configuration.class.getDeclaredField("mappedStatements");
            mappedStatementsField.setAccessible(true);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            Map<String, MappedStatement> mappedStatements = (Map<String, MappedStatement>) mappedStatementsField.get(configuration);
            mappedStatements.remove(statementId);
            mappedStatementsField.set(configuration,mappedStatements);
        } catch (Exception e) {
            log.error("移除mappedStatement失败,statementId={}", statementId, e);
        }
    }

    /**
     * 构建MappedStatement的结果resultMap
     * @param configuration     mybatis解析完成后的所有配置信息,包含配置如数据源、事务管理器、类型别名、类型处理器、对象工厂、插件、环境、数据库标识、映射器等
     * @param resultType        结果类型
     * @param statementId       sql片段的唯一标识
     * @param resultMappings    结果列信息
     * @return 返回结果集合
     */
    private List<ResultMap> getStatementResultMaps(Configuration configuration, Class<?> resultType,
                                                   String statementId, List<ResultMapping> resultMappings) {
        List<ResultMap> resultMaps = new ArrayList<>();
        if (resultType != null) {
            ResultMap inlineResultMap = new ResultMap.Builder(
                    configuration,
                    statementId + "-Inline",
                    resultType,
                    resultMappings,
                    null).build();
            resultMaps.add(inlineResultMap);
        }
        return resultMaps;
    }

}