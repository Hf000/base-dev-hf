package org.hf.boot.springboot.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.pojo.dto.BasePageReq;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 数据导出的处理分页查询方式实现 - 1
 * @author hf
 */
@Slf4j
@Component
public class CommonExportDataUtils {

    /**
     * @param response         请求response
     * @param tableName        表格名称
     * @param sheetName        表格名称
     * @param exportObjectList 要导出的数据
     * @param exportHeader     导出数据的表头
     * @throws IOException 异常
     */
    public void commonExportData(HttpServletResponse response, String tableName, String sheetName,
                                 List<List<Object>> exportObjectList, List<List<String>> exportHeader) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition", "attachment;filename=" + tableName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        EasyExcel.write(outputStream)
                .excelType(ExcelTypeEnum.XLSX)
                .head(exportHeader)
                .registerWriteHandler(new AssignRowsAndColumnsToMergeStrategy(new ArrayList<>()))
                .sheet(sheetName)
                .doWrite(exportObjectList);
        if (null != outputStream) {
            outputStream.close();
        }
    }

    /**
     * @param response     http 请求响应
     * @param tableName    excel名
     * @param sheetName    sheet名
     * @param req          导出请求
     * @param pageSize     分页/每页数量
     * @param exportHeader 导出列列头
     * @param function     导出数据查询及转换函数
     * @throws IOException 异常
     */
    public void commonPartExportData(HttpServletResponse response, String tableName, String sheetName,
                                     BasePageReq req, Integer pageSize, List<List<String>> exportHeader,
                                     Function<BasePageReq, PageInfo<?>> function) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(tableName, "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(outputStream)
                .excelType(ExcelTypeEnum.XLSX)
                .head(exportHeader)
                .registerWriteHandler(new AssignRowsAndColumnsToMergeStrategy(new ArrayList<>()));
        ExcelWriter excelWriter = excelWriterBuilder.build();
        ExcelWriterSheetBuilder excelWriterSheetBuilder = excelWriterBuilder.sheet(sheetName);
        WriteSheet writeSheet = excelWriterSheetBuilder.build();
        // 强制写入表头（即使数据为空）
        excelWriter.write(Collections.emptyList(), writeSheet);
        int currentPage = 1;
        int totalPage;
        pageSize = null == pageSize ? 2000 : pageSize;
        req.setPageSize(pageSize);
        do {
            req.setPageNum(currentPage);
            log.debug("commonExportData,req:{}", req);
            PageInfo<?> pageInfo = function.apply(req);
            totalPage = pageInfo.getPages();
            // 写入当前批次的数据
            log.debug("commonExportData,pageInfo.getList:{}", pageInfo.getList());
            if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                // 这里的pageInfo.getList获取的数据对象为导出的List<List<Object>>结构数据
                excelWriter.write(pageInfo.getList(), writeSheet);
            }
        } while (currentPage++ < totalPage);
        excelWriter.finish();
        if (null != outputStream) {
            outputStream.close();
        }
    }

}