package org.hf.application.mybatis.tk.springboot.generator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import tk.mybatis.mapper.generator.MapperPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> tk-mybatis代码生成 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/7/31 18:14
 */
@Slf4j
public class CodeGenerator extends MapperPlugin {

    public static void main(String[] args) {
        // tk-mybatis代码生成
        try {
            List<String> warnings = new ArrayList<>();
            // 获取生成代码配置文件
            InputStream resourceAsStream = CodeGenerator.class.getClassLoader().getResourceAsStream("generator/generatorConfig.xml");
            ConfigurationParser configurationParser = new ConfigurationParser(warnings);
            Configuration config = configurationParser.parseConfiguration(resourceAsStream);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                log.info(warning);
            }
        } catch (Exception e) {
            log.error("tk-mybatis生成代码失败", e);
        }
    }

    /**
     * 生成Entity实体类
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        // 移除Entity类中的注解
        List<String> annotations = topLevelClass.getAnnotations();
        annotations.remove("@ApiModel(\"\")");
        Set<FullyQualifiedJavaType> importedTypes = topLevelClass.getImportedTypes();
        importedTypes.removeIf(fullyQualifiedJavaType -> "io.swagger.annotations.ApiModel".equals(fullyQualifiedJavaType.getFullyQualifiedName()));
        // 生成entity对应的dto
        genResponseDto(topLevelClass);
        return true;
    }

    /**
     * 生成entity对应的dto
     */
    private void genResponseDto(TopLevelClass topLevelClass) {
        StringBuilder sbu = new StringBuilder();
        // 拼接包名
        FullyQualifiedJavaType type = topLevelClass.getType();
        String entityPackageName = type.getPackageName();
        String dtoPackageName = entityPackageName.replace("entity", "dto");
        sbu.append("package ").append(dtoPackageName).append(";\n\n");
        // 实现类import
        Set<FullyQualifiedJavaType> entityImportedTypes = topLevelClass.getImportedTypes();
        Set<FullyQualifiedJavaType> dtoImportType = entityImportedTypes.stream().filter(fullyQualifiedJavaType -> !"javax.persistence".equals(fullyQualifiedJavaType.getPackageName()) && !"io.swagger.annotations.ApiModel".equals(fullyQualifiedJavaType.getFullyQualifiedName())).collect(Collectors.toSet());
        dtoImportType.forEach(fullyQualifiedJavaType -> sbu.append("import ").append(fullyQualifiedJavaType.getFullyQualifiedName()).append(";\n"));
        // 注解
        sbu.append("\n").append("@Getter\n").append("@Setter\n").append("@ToString\n");
        // 继承基类
        sbu.append("public class ").append(type.getShortName()).append("DTO ").append("extends BaseDataDTO {").append("\n");
        // 字段
        List<Field> fields = topLevelClass.getFields();
        fields.forEach(field -> {
            // @ApiModelProperty
            /*field.getAnnotations().forEach(annotation -> {
                if (annotation.contains("@ApiModelProperty")) {
                    sbu.append("\n    ").append(annotation).append("\n");
                }
            });*/
            sbu.append("    ").append("private ").append(field.getType().getShortName()).append(" ").append(field.getName()).append(";\n");
        });
        sbu.append("\n").append("}").append("\n");
        // 保存路径
        String filePath = "src/main/java/" + dtoPackageName.replaceAll("\\.", "/") + "/" + type.getShortName() + "DTO.java";
        File dtoFile = new File(filePath);
        FileUtil.mkParentDirs(dtoFile);
        FileUtil.writeUtf8String(sbu.toString(), dtoFile);
    }

    /**
     * 修改实体类生成字段注释和注解
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
        // 移除字段注释
        // field.getJavaDocLines().clear();
        return true;
    }

    /**
     * 生成Mapper接口
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        super.clientGenerated(interfaze, topLevelClass, introspectedTable);
        // Mapper接口添加注解
        addMapperInterfaceAnnotations(interfaze);
        // 生成接口注释
        generatorJavaTypeComment(interfaze, introspectedTable);
        return true;
    }

    /**
     * Mapper接口添加注解
     */
    private void addMapperInterfaceAnnotations(Interface interfaze) {
        // 添加@Repository注解, 防止idea报红
        // 添类加引入
        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
        // 添加注解
        interfaze.addAnnotation("@Repository");
    }

    /**
     * 为java类或者接口生成文档注释
     */
    private void generatorJavaTypeComment(JavaElement javaElement, IntrospectedTable introspectedTable) {
        String remarks = introspectedTable.getRemarks();
        javaElement.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                javaElement.addJavaDocLine(" * " + remarkLine);
            }
        }
        javaElement.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
        javaElement.addJavaDocLine(" * ");
        javaElement.addJavaDocLine(" * @author " + System.getProperties().getProperty("user.name"));
        javaElement.addJavaDocLine(" * @date " + DateUtil.format(new Date(), "yyyy/MM/dd HH:mm"));
        javaElement.addJavaDocLine("*/ ");
    }

    /**
     * 修改生成的mapper.xml文档
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        List<Element> elements = rootElement.getElements();
        elements.forEach(element -> {
            XmlElement itemElement = (XmlElement)element;
            List<Element> subElements = itemElement.getElements();
            subElements.removeIf(subElement -> subElement instanceof TextElement && ("<!--".equals(((TextElement)subElement).getContent())
                || "  WARNING - @mbg.generated".equals(((TextElement)subElement).getContent())
                || "-->".equals(((TextElement)subElement).getContent()))
            );
            subElements.add(0, new TextElement("<!-- WARNING - @mbg.generated -->"));
        });
        return true;
    }

    /**
     * 修改mapper.xml通用列命名
     */
    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        updateXmlElementAttribute(element, "id", "BaseColumnList");
        return true;
    }

    /**
     * 修改mapper.xml文档的元素节点的属性名称
     */
    private void updateXmlElementAttribute(XmlElement element, String attributeName, String attributeValue) {
        List<Attribute> attributes = element.getAttributes();
        if (CollectionUtil.isNotEmpty(attributes)) {
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attribute = attributes.get(i);
                if (attributeName.equals(attribute.getName())) {
                    attributes.set(i, new Attribute(attribute.getName(), attributeValue));
                    return;
                }
            }
        }
    }

    @Override
    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
//        updateXmlElementAttribute(element, "id", "BlobColumnList");
        return true;
    }

}
