package org.hf.boot.springboot.pojo.dto;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;

import java.util.List;

/**
 * Excel的sheetDto
 */
@Data
public class SheetDTO {

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 自定义表头Dto
     * headClass和headArrays, 只能指定一个
     */
    private Class<?> headClass;

    /**
     * 动态表头 相邻相同的单元格可自动合并
     * headClass和headArrays, 只能指定一个
     * 示例:
     * String[][] headArrays = {
     *  {"表头一","表头一", "字段一"},
     *  {"表头二","表头二", "字段二"},
     *  {"表头二","表头二", "字段三"},
     *  {"表头三","表头三", "字段四"},
     *  {"表头三","表头三", "字段五"},
     *  {"表头三","表头三", "字段六"}
     * };
     */
    private String[][] headArrays;

    /**
     * 指定单元格宽度 序号从0开始, 如不指定某列宽度, 则可设置为null. 数值等同@ColumnWidth注解
     * 示例: Integer[] columnWidths = {10,30,null,40,50,null};
     * 仅在未指定headClass时生效
     * 如果指定了headClass, 请在headClass中使用@ColumnWidth注解
     */
    private Integer[] columnWidths;

    /**
     * 自定义样式策略 列宽 字体等
     */
    private WriteHandler writeHandler;

    /**
     * 要写入的数据
     */
    private List<?> dataList;

    public SheetDTO() {

    }

    public SheetDTO(String sheetName, List<?> dataList) {
        this(sheetName, null, null, null, null, dataList);
    }

    public SheetDTO(String sheetName, Class<?> headClass, List<?> dataList) {
        this(sheetName, headClass, null, null, null, dataList);
    }

    public SheetDTO(String sheetName, String[][] headArrays, List<?> dataList) {
        this(sheetName, null, headArrays, null, null, dataList);
    }

    public SheetDTO(String sheetName, String[][] headArrays, Integer[] columnWidths, List<?> dataList) {
        this(sheetName, null, headArrays, columnWidths, null, dataList);
    }

    public SheetDTO(String sheetName, Class<?> headClass, WriteHandler writeHandler, List<?> dataList) {
        this(sheetName, headClass, null, null, writeHandler, dataList);
    }

    public SheetDTO(String sheetName, String[][] headArrays, WriteHandler writeHandler, List<?> dataList) {
        this(sheetName, null, headArrays, null, writeHandler, dataList);
    }

    public SheetDTO(String sheetName, String[][] headArrays, Integer[] columnWidths, WriteHandler writeHandler, List<?> dataList) {
        this(sheetName, null, headArrays, columnWidths, writeHandler, dataList);
    }

    private SheetDTO(String sheetName, Class<?> headClass, String[][] headArrays, Integer[] columnWidths, WriteHandler writeHandler, List<?> dataList) {
        this.sheetName = sheetName;
        this.headClass = headClass;
        this.headArrays = headArrays;
        this.columnWidths = columnWidths;
        this.writeHandler = writeHandler;
        this.dataList = dataList;
    }
}