package org.hf.application.mybatis.plus.springboot.generator;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <p> 自定义freemaker模板引擎 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/15 9:41
 */
public class CustomFreemarkerTemplateEngine extends AbstractTemplateEngine {

    private Configuration configuration;

    @NotNull
    @Override
    public AbstractTemplateEngine init(@NotNull ConfigBuilder configBuilder) {
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.configuration.setDefaultEncoding(ConstVal.UTF8);
        this.configuration.setClassForTemplateLoading(CustomFreemarkerTemplateEngine.class, "/");
        return this;
    }

    @Override
    public void writer(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull File outputFile) throws Exception {
        Template template = this.configuration.getTemplate(templatePath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        Throwable throwable = null;
        try {
            objectMap.put("date", (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date()));
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        } catch (Throwable t) {
            throwable = t;
            throw t;
        } finally {
            if (throwable != null) {
                try {
                    fileOutputStream.close();
                } catch (Throwable t) {
                    throwable.addSuppressed(t);
                }
            } else {
                fileOutputStream.close();
            }
        }
        logger.debug("模板:" + templatePath + "; 文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
