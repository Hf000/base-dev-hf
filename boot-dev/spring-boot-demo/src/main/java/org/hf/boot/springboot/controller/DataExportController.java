package org.hf.boot.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.export.ExcelStremExportUtil;
import org.hf.boot.springboot.export.ExportExcelFileEnum;
import org.hf.boot.springboot.pojo.dto.BasePageReq;
import org.hf.boot.springboot.export.CommonExportDataUtils;
import org.hf.boot.springboot.pojo.dto.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 数据导出controller </p>
 */
@Slf4j
@RestController
@RequestMapping("/export")
public class DataExportController {

    @Autowired
    private CommonExportDataUtils commonExportDataUtils;

    @Autowired
    private ExcelStremExportUtil excelStremExportUtil;

    @ApiOperation("数据导出请求示例1")
    @PostMapping("/test1")
    public void exportDataTest(BasePageReq req, HttpServletResponse response) {
        try {
            List<List<String>> headerNameList = new ArrayList<>();
            commonExportDataUtils.commonPartExportData(response,
                "数据导出测试", "数据导出测试sheet", req,1000, headerNameList,
                (pageReq) -> {
                    //分页查询
                    log.info("分页查询入参 req:{}", pageReq);
                    //查询列表数据
                    PageInfo<List<Object>> pageInfo = pageQueryDatabaseMethod(pageReq);
                    log.info("exportDataTest pageInfo:{}", pageInfo);
                    return pageInfo;
                });
        } catch (IOException e) {
            throw new BusinessException("数据导出异常");
        }
    }

    public PageInfo<List<Object>> pageQueryDatabaseMethod(BasePageReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        // 分页查询数据伪代码
        return new PageInfo<>(new ArrayList<>());
    }

    @ApiOperation("数据导出请求示例2")
    @PostMapping("/test2")
    public void exportDataTest2(HttpServletResponse response) throws IOException {
        List<List<Object>> listData = listDatabaseMethod();
        List<List<String>> exportHeaderList = new ArrayList<>();
        commonExportDataUtils.commonExportData(response, "数据导出测试", "数据导出测试sheet", listData, exportHeaderList);
    }

    public List<List<Object>> listDatabaseMethod() {
        // 查询数据列表伪代码
        return new ArrayList<>();
    }

    @PostMapping("/streamExport")
    @ApiOperation(value = "流式导出示例")
    public void exportBillList(HttpServletResponse response, @RequestBody UserReq req){
        try {
            excelStremExportUtil.exportExcel(ExportExcelFileEnum.STREAM_EXPORT_TEST, response, req);
        }catch (Exception e){
            log.error("export failed",e);
        }
    }

}
