package org.hf.application.mybatis.plus.springboot.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.pojo.entity.BaseEntity;
import org.hf.common.publi.utils.PropertiesUtil;

import java.util.Collections;
import java.util.Scanner;

/**
 * <p> mybatis-plus代码生成器主类 </p>
 * 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 17:17
 */
public class CodeGenerator {

    private static String DB_URL = "";
    private static final String DB_SCHEMA_NAME = "public";
    private static String DB_USERNAME = "";
    private static String DB_PASSWORD = "";

    static {
        DB_URL = PropertiesUtil.init().getPropertiesValue("spring.datasource.url");
        DB_USERNAME = PropertiesUtil.init().getPropertiesValue("spring.datasource.username");
        DB_PASSWORD= PropertiesUtil.init().getPropertiesValue("spring.datasource.password");
    }

    /**
     * 代码生成执行主方法
     */
    public static void main(String[] args) {
        if (StringUtils.isBlank(DB_PASSWORD)) {
            // 数据库连接密码
            DB_PASSWORD = scanner("数据库连接密码");
        }
        // 获取系统变量, 来获得项目路径
        String projectPath = scanner("模块磁盘路径");
        FastAutoGenerator.create(
            // 配置数据源
            new DataSourceConfig.Builder(DB_URL, DB_USERNAME, DB_PASSWORD)
            .dbQuery(new MySqlQuery())
            .schema(DB_SCHEMA_NAME)
            .typeConvert(new MySqlTypeConvert())
            .keyWordsHandler(new MySqlKeyWordsHandler())
            // pgSQL类型转换  转换数据库中的字段类型
            /*.typeConvert(new PostgreSqlTypeConvert() {
                @Override
                public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                    // 将数据库中的timestamp转换成date类型
                    if (fieldType.toLowerCase().contains("timestamp")) {
                        return DbColumnType.DATE;
                    }
                    return super.processTypeConvert(config, fieldType);
                }
            })*/
        ).globalConfig(globalConfig -> {
            // 全局配置
            globalConfig
            // 文件输出路径
            .outputDir(projectPath + "/src/main/java")
            .author(scanner("author信息"))
            .dateType(DateType.TIME_PACK)
            .commentDate("yyyy-MM-dd")
            // 是否打开输出目录
            .disableOpenDir();
        }).packageConfig(packageConfig -> {
            // 包配置
            packageConfig.parent(scanner("项目父包名"))
            .moduleName(scanner("当前模块包名"))
            .entity("pojo.entity")
            .service("service")
            .serviceImpl("service.impl")
            .mapper("mapper")
            .xml("mapper.xml")
            .controller("controller")
            // 存放mapper.xml路径
            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper"));
        }).templateConfig(templateConfig -> {
            // 模板配置
            // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别, 如果不配置则按照默认模板文件生成
            // 设置为null时, 则不会生成xml文件, controller,service,mapper,entity等java类
            templateConfig.entity("/templates/entity.java")
            .service("/templates/service.java")
            .serviceImpl("/templates/serviceImpl.java")
            .mapper("/templates/mapper.java")
            .xml("/templates/mapper.xml")
            .controller("/templates/controller.java");
        })/*.injectionConfig(injectionConfig -> {
            // 注入配置  https://baomidou.com/pages/981406/#%E6%B3%A8%E5%85%A5%E9%85%8D%E7%BD%AE-injectionconfig
            // 可以用来生成DTO
            injectionConfig.beforeOutputFile((tableInfo, objectMap) -> {
                System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
            })
            .customMap(Collections.singletonMap("", ""))
            .customFile(Collections.singletonMap("", ""));
        })*/.strategyConfig(strategyConfig -> {
            // 表策略配置
            strategyConfig.enableCapitalMode()
            .enableSkipView()
            .disableSqlFilter()
            .likeTable(new LikeTable("USER"))
            .addInclude(scanner("表名，多个英文逗号分割").split(","))
            .addTablePrefix(scanner("表前缀"))
//            .addFieldPrefix(scanner("字段前缀"))
             // entity策略配置
            .entityBuilder()
            .superClass(BaseEntity.class)
            .enableLombok()
            .enableRemoveIsPrefix()
            .enableTableFieldAnnotation()
            .logicDeleteColumnName("deleted_state")
            .logicDeletePropertyName("deletedState")
            .naming(NamingStrategy.underline_to_camel)
            .columnNaming(NamingStrategy.underline_to_camel)
            .addTableFills(new Property("createdDate", FieldFill.INSERT), new Property("updatedDate", FieldFill.INSERT_UPDATE))
            .idType(IdType.AUTO)
            .formatFileName("%sEntity")
            // Controller 策略配置
            .controllerBuilder()
            .enableHyphenStyle()
            .enableRestStyle()
            .formatFileName("%sController")
            // Service 策略配置
            .serviceBuilder()
            .superServiceClass(IService.class)
            .superServiceImplClass(ServiceImpl.class)
            .formatServiceFileName("%sService")
            .formatServiceImplFileName("%sServiceImpl")
            // mapper xml生成策略
            .mapperBuilder()
            .superClass(BaseMapper.class)
            .enableBaseResultMap()
            .enableBaseColumnList()
            .formatMapperFileName("%sMapper")
            .formatXmlFileName("%sMapper");
        })
        // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        .templateEngine(new CustomFreemarkerTemplateEngine())
        .execute();;
    }

    /**
     * 读取控制台内容
     * @param tip 提示信息
     * @return 返回
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
