package org.hf.boot.springboot.export;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流式导出excel实现 - 1
 * 流失导出各场景枚举
 */
@Getter
@AllArgsConstructor
public enum ExportExcelFileEnum {

    /**
     * 枚举项
     */
    STREAM_EXPORT_TEST("流失导出测试","流失导出测试Sheet",
            "streamTestResultHandlerFactory",
            "org.hf.boot.springboot.dao.UserInfoMapper.streamExportTest"),
    ;
    /**
     * 导出文件名称
     */
    private final String tableName;
    /**
     * 导出文件sheet页名称
     */
    private final String sheetName;
    /**
     * 结果处理Spring容器中对象Bean名称
     */
    private final String resultHandlerFactory;
    /**
     * 数据查询mybatis的Mapper方法全名称
     */
    private final String queryMapperPath;

}