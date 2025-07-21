package org.hf.boot.springboot.export;

import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 流式导出excel实现 - 4
 * 导出工具类: 适用范围：流式导出优势在于通过原生的sqlSesiiontemplate流式查询数据，分页导出数据，适用于在单服务中直接查询当前表的数据并导出。
 * 相关实现步骤
 * 1.mapper类: 维护查询sql, mapper文件等，select 标签上记得添加 fetchSize="1000" resultSetType="FORWARD_ONLY" 属性，
 *  其中fetchSize 代表每批次从数据库中读取的数据量，可自行定义, 例如:org.hf.boot.springboot.dao.UserInfoMapper#
 *  streamExportTest(org.hf.boot.springboot.pojo.dto.UserReq, org.apache.ibatis.session.ResultHandler)
 * 2.实现org.hf.boot.springboot.export.ResultHandlerFactory,用于指定场景下的查询后结果处理之后导出, 例如: org.hf.boot.
 *  springboot.export.StreamTestResultHandlerFactory
 * 3.枚举类ExportExcelFileEnum，定义导出 excel文件名，sheet名，导出结果处理工厂类（步骤2中创建的结果处理类），导出查询sql语句所在
 *  mapper文件中方法的完整路径(步骤1中定义的导出查询方法完整路径，精确到方法), 例如:org.hf.boot.springboot.export.
 *  ExportExcelFileEnum#STREAM_EXPORT_TEST
 */
@Slf4j
@Component
public class ExcelStremExportUtil {

    /**
     * 导出结果集处理工厂类 map ,新增需实现 ResultHandlerFactory 并添加上@Component 注解，启动时自动注入
     */
    @Lazy
    @Autowired
    private Map<String, ResultHandlerFactory> resultHandlerFactoryMap;

    /**
     * @param exportExcelFileEnum 导出文件配置枚举
     * @param response            请求响应对象
     * @param queryReq            导出查询条件入参
     */
    public void exportExcel(ExportExcelFileEnum exportExcelFileEnum, HttpServletResponse response, Object queryReq) throws IOException {
        try (ServletOutputStream out = response.getOutputStream()) {
            //1. 初始化参数
            initHeader(exportExcelFileEnum.getTableName(), response);
            SqlSessionTemplate sqlSessionTemplate = SpringContextUtil.getBean(SqlSessionTemplate.class);
            //2. 创建结果集处理器
            ResultHandlerFactory resultHandlerFactory = resultHandlerFactoryMap.get(exportExcelFileEnum.getResultHandlerFactory());
            CommonResultHandler<?, ?> resultHandler = resultHandlerFactory.getResultHandler(response);
            resultHandler.getSheet().setSheetName(exportExcelFileEnum.getSheetName());
            //3. 查询要导出的数据，放入结果集处理器中 (注意 select 语句添加 fetchSize="1000" ，限制每批从数据库取出来的数据量）, 结果集处理器实现导出数据
            sqlSessionTemplate.select(exportExcelFileEnum.getQueryMapperPath(), queryReq, resultHandler);
            //4. 处理最后一批数据
            resultHandler.finish();
            //5.导出数据
            resultHandler.getWriter().finish();
            out.flush();
        }
    }

    /**
     * 初始化返回值参数
     * @param fileName  文件名称
     * @param response  响应对象
     */
    public void initHeader(String fileName, HttpServletResponse response) throws IOException {
        fileName = fileName.endsWith(".xlsx") ? fileName : fileName + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
    }

}