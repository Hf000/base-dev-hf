package org.hf.boot.springboot.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.pojo.dto.SheetDTO;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Excel工具类
 */
@Slf4j
public class ExcelUtil {
    
    public static final String EXCEL_FORMAT_2003 = ".xls";
    
    public static final String EXCEL_FORMAT_2007 = ".xlsx";

    public static final String DEFAULT_FILE_MKDIR = "/wls/biz";
    
    private ExcelUtil() {
    }
    
    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input 输入:InputStream,File或文件路径
     * @param head  要读取的dto类
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head) {
        return readToDto(input, head, 0, null, 1);
    }
    
    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入:InputStream,File或文件路径
     * @param head      要读取的dto类
     * @param sheetName sheet名称
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, String sheetName) {
        return readToDto(input, head, null, sheetName, 1);
    }
    
    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input   输入:InputStream,File或文件路径
     * @param head    要读取的dto类
     * @param sheetNo 第几个sheet 从0开始
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, Integer sheetNo) {
        return readToDto(input, head, sheetNo, null, 1);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input    输入:InputStream,File或文件路径
     * @param head     要读取的dto类
     * @param headRows 表头行数,默认为1
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, int headRows) {
        return readToDto(input, head, 0, null, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入:InputStream,File或文件路径
     * @param head      要读取的dto类
     * @param sheetName sheet名称
     * @param headRows  表头行数,默认为1
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, String sheetName, int headRows) {
        return readToDto(input, head, null, sheetName, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input    输入:InputStream,File或文件路径
     * @param head     要读取的dto类
     * @param sheetNo  第几个sheet 从0开始
     * @param headRows 表头行数,默认为1
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, Integer sheetNo, int headRows) {
        return readToDto(input, head, sheetNo, null, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input 输入:InputStream,File或文件路径
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input) {
        return readToArray(input, 0, null, 1);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input   输入:InputStream,File或文件路径
     * @param sheetNo 第几个sheet 从0开始
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, Integer sheetNo) {
        return readToArray(input, sheetNo, null, 1);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入:InputStream,File或文件路径
     * @param sheetName sheet名称
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, String sheetName) {
        return readToArray(input, null, sheetName, 1);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input    输入:InputStream,File或文件路径
     * @param headRows 表头行数,默认为1
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, int headRows) {
        return readToArray(input, 0, null, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input    输入:InputStream,File或文件路径
     * @param sheetNo  第几个sheet 从0开始
     * @param headRows 表头行数,默认为1
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, Integer sheetNo, int headRows) {
        return readToArray(input, sheetNo, null, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input       输入:InputStream,File或文件路径
     * @param sheetNo     第几个sheet 从0开始
     * @param headRows    表头行数,默认为1
     * @param columnCount 要读取的列数
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, Integer sheetNo, int headRows, int columnCount) {
        return readToArray(input, sheetNo, null, headRows, columnCount);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入:InputStream,File或文件路径
     * @param sheetName sheet名称
     * @param headRows  表头行数,默认为1
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, String sheetName, int headRows) {
        return readToArray(input, null, sheetName, headRows);
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input       输入:InputStream,File或文件路径
     * @param sheetName   sheet名称
     * @param headRows    表头行数,默认为1
     * @param columnCount 要读取的列数
     * @return List<String [ ]>
     */
    public static List<String[]> readToArray(Object input, String sheetName, int headRows, int columnCount) {
        return readToArray(input, null, sheetName, headRows, columnCount);
    }

    /**
     * 构建ExcelReader
     *
     * @param input    输入
     * @param head     表头
     * @param headRows 表头行数,默认为1
     * @return ExcelReader
     */
    private static <T> ExcelReader bulidExcelReader(Object input, Class<T> head, ReadListener<T> readListener, int headRows) {
        if (input == null) {
            throw new NullPointerException("inputStream is null.");
        }
        ExcelReader excelReader;
        if (input instanceof InputStream) {
            excelReader = EasyExcelFactory.read((InputStream) input, head, readListener).headRowNumber(headRows).build();
        } else if (input instanceof File) {
            excelReader = EasyExcelFactory.read((File) input, head, readListener).headRowNumber(headRows).build();
        } else if (input instanceof String) {
            excelReader = EasyExcelFactory.read((String) input, head, readListener).headRowNumber(headRows).build();
        } else {
            throw new IllegalArgumentException("input is not illegal type.");
        }
        return excelReader;
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入
     * @param head      要读取的dto类
     * @param sheetNo   第几个sheet 从0开始
     * @param sheetName sheet名称
     * @param headRows  表头行数,默认为1
     * @return List<T>
     */
    public static <T> List<T> readToDto(Object input, Class<T> head, Integer sheetNo, String sheetName, int headRows) {
        DtoReadListener<T> excelListener = new DtoReadListener<>();
        ExcelReader excelReader = bulidExcelReader(input, head, excelListener, headRows);
        ReadSheet readSheet = new ReadSheet(sheetNo, sheetName);
        excelReader.read(readSheet);
        excelReader.finish();
        return excelListener.getDataList();
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input     输入
     * @param sheetNo   第几个sheet 从0开始
     * @param sheetName sheet名称
     * @param headRows  表头行数,默认为1
     * @return List<String [ ]>
     */
    private static List<String[]> readToArray(Object input, Integer sheetNo, String sheetName, int headRows) {
        StringReadListener excelListener = new StringReadListener();
        ExcelReader excelReader = bulidExcelReader(input, null, excelListener, headRows);
        ReadSheet readSheet = new ReadSheet(sheetNo, sheetName);
        excelReader.read(readSheet);
        excelReader.finish();
        return excelListener.getDataList();
    }

    /**
     * 从输入读取Excel单个sheet页
     * 读取特定sheet页,包含表头
     *
     * @param input       输入:InputStream,File或文件路径
     * @param sheetNo     第几个sheet 从0开始
     * @param sheetName   sheet名称
     * @param headRows    表头行数,默认为1
     * @param columnCount 要读取的列数
     * @return List<String [ ]>
     */
    private static List<String[]> readToArray(Object input, Integer sheetNo, String sheetName, int headRows, int columnCount) {
        StringReadListener excelListener = new StringReadListener(columnCount);
        ExcelReader excelReader = bulidExcelReader(input, null, excelListener, headRows);
        ReadSheet readSheet = new ReadSheet(sheetNo, sheetName);
        excelReader.read(readSheet);
        excelReader.finish();
        return excelListener.getDataList();
    }

    /**
     * 是否是Excel文件 根据文件名后缀判断
     *
     * @param fileName 文件名称
     * @return boolean
     */
    public static boolean isExcel(String fileName) {
        boolean is = false;
        if (fileName == null || "".equals(fileName)) {
            return false;
        }
        int idx = fileName.lastIndexOf('.');
        if (idx >= 0) {
            String suffix = fileName.substring(idx).toLowerCase();
            if (EXCEL_FORMAT_2003.equals(suffix) || EXCEL_FORMAT_2007.equals(suffix)) {
                is = true;
            }
        }
        return is;
    }

    /**
     * 是否是Excel文件 根据文件名后缀判断
     *
     * @param file MultipartFile
     * @return boolean
     */
    public static boolean isExcel(MultipartFile file) {
        return !file.isEmpty() && file.getOriginalFilename() != null && ExcelUtil.isExcel(file.getOriginalFilename());
    }

    public static void fillDataExport(String exportFileName, String fillDataFilePath, List<?> dataList, HttpServletResponse response) throws IOException {
        // tmp/excel路径必须存在
        String tempExcelPath = "tmp/excel";
        String tempExportFileName = tempExcelPath + exportFileName.replaceFirst(".xlsx", "-" + getShortUuid() + ".xlsx");
        File tempExportFile = new File(tempExportFileName);
        if (!tempExportFile.exists()) {
            boolean tempExportFileNewFile = tempExportFile.createNewFile();
            log.info("创建临时文件:{}", tempExportFileNewFile);
        }
        InputStream fillDataInputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(fillDataFilePath);
            if (classPathResource.exists()) {
                log.info("读取模板文件:{}成功", fillDataFilePath);
                fillDataInputStream = classPathResource.getInputStream();
            } else {
                log.error("模板文件:{}不存在", fillDataFilePath);
                throw new FileNotFoundException();
            }
            EasyExcel.write(tempExportFileName).withTemplate(fillDataInputStream).sheet().doFill(dataList);
            exportFromFilePath(exportFileName, tempExportFileName, response);
            // 删除临时文件
            File tempFile = new File(tempExportFileName);
            if (tempFile.exists()) {
                boolean delete = tempFile.delete();
                log.info("临时文件是否删除{}", delete);
            }
        } finally {
            if (fillDataInputStream != null) {
                fillDataInputStream.close();
            }
        }
    }

    /**
     * 获取一个20位的uuid
     *
     * @return uuid
     */
    public static String getShortUuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, 20);
    }

    /**
     * 根据模板填充数据导出（支持多sheets）
     */
    public static void fillSheetsDataExport(String exportFileName, String fillDataFilePath,HttpServletResponse response,List<?>... sheetDatas) throws IOException {
        String tempExportFileName = getBizFilePath(exportFileName);
        File tempExportFile = new File(tempExportFileName);
        if (!tempExportFile.exists()) {
            boolean tempExportFileNewFile = tempExportFile.createNewFile();
            log.info("创建临时文件:{}", tempExportFileNewFile);
        }
        InputStream fillDataInputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(fillDataFilePath);
            if (classPathResource.exists()) {
                log.info("读取模板文件:{}成功", fillDataFilePath);
                fillDataInputStream = classPathResource.getInputStream();
            } else {
                log.error("模板文件:{}不存在", fillDataFilePath);
                throw new FileNotFoundException();
            }
            ExcelWriter excelWriter = EasyExcel.write(tempExportFileName).withTemplate(fillDataInputStream).build();
            for (int i = 0; i < sheetDatas.length; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(sheetDatas[i], fillConfig, writeSheet);
            }
            excelWriter.finish();
            exportFromFilePath(exportFileName, tempExportFileName, response, StandardCharsets.UTF_8.name());
            // 删除临时文件
            File tempFile = new File(tempExportFileName);
            if (tempFile.exists()) {
                boolean delete = tempFile.delete();
                log.info("临时文件已删除");
            }
        } finally {
            if (fillDataInputStream != null) {
                fillDataInputStream.close();
            }
        }
    }

    public static final String BIZ_FILE_PATH = "biz.file.path";

    /**
     * 获取biz文件路径
     */
    public static String getBizFilePath(String exportFileName) {
        Environment environment = SpringContextUtil.getBean(Environment.class);
        String bizFilePath = environment.getProperty(BIZ_FILE_PATH, String.class, DEFAULT_FILE_MKDIR) + File.separator + getYear(new Date()) + File.separator;
        File tempDir = new File(bizFilePath);
        if (tempDir.mkdirs()) {
            try {
                boolean newFile = tempDir.createNewFile();
                log.info("创建文件是否成功{}", newFile);
            } catch (IOException e) {
                log.error("FileUtil.getBizFilePath error", e);
                throw new BusinessException("创建文件目录异常");
            }
        }
        int dotIndex = exportFileName.lastIndexOf(".");
        String fileSuffix = exportFileName.substring(dotIndex);
        return bizFilePath +
                getShortUuid() +
                fileSuffix;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据模板填充数据导出（支持多sheets）
     */
    public static File fillSheetsDataExport(String exportFileName, String fillDataFilePath,List<?>... sheetDatas) throws IOException {
        String tempExportFileName = getBizFilePath(exportFileName);
        File tempExportFile = new File(tempExportFileName);
        if (!tempExportFile.exists()) {
            boolean tempExportFileNewFile = tempExportFile.createNewFile();
            log.info("创建临时文件:{}", tempExportFileNewFile);
        }
        InputStream fillDataInputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(fillDataFilePath);
            if (classPathResource.exists()) {
                log.info("读取模板文件:{}成功", fillDataFilePath);
                fillDataInputStream = classPathResource.getInputStream();
            } else {
                log.error("模板文件:{}不存在", fillDataFilePath);
                throw new FileNotFoundException();
            }
            ExcelWriter excelWriter = EasyExcel.write(tempExportFileName).withTemplate(fillDataInputStream).build();
            for (int i = 0; i < sheetDatas.length; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(sheetDatas[i], fillConfig, writeSheet);
            }
            excelWriter.finish();
            // 删除临时文件
            return new File(tempExportFileName);
        } finally {
            if (fillDataInputStream != null) {
                fillDataInputStream.close();
            }
        }
    }

    /**
     * 从类路径导出
     */
    public static void exportFromClasspath(String exportFileName, String classpathFilePath, HttpServletResponse response) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(classpathFilePath);
        if (classPathResource.exists()) {
            log.info("读取模板文件:{}成功", classpathFilePath);
            exportExcelFile(exportFileName, classPathResource.getInputStream(), response);
        } else {
            log.error("模板文件:{}不存在", classpathFilePath);
            throw new FileNotFoundException();
        }
    }

    /**
     * 从文件路径导出
     */
    public static void exportFromFilePath(String exportFileName, String filePath, HttpServletResponse response) throws IOException {
        exportExcelFile(exportFileName, Files.newInputStream(Paths.get(filePath)), response);
    }

    /**
     * 从文件路径导出
     */
    public static void exportExcelFile(String exportFileName, InputStream templateInputStream, HttpServletResponse response) throws IOException {
        OutputStream outputStream = null;
        try {
            if (templateInputStream == null) {
                throw new BusinessException("文件输入流不能为空");
            }
            setContentTypeAndFileName(exportFileName, response);
            response.setHeader("Content-disposition", "attachment; filename=" + new String(exportFileName.getBytes("GB2312"), StandardCharsets.ISO_8859_1));
            outputStream = response.getOutputStream();
            IOUtils.copy(templateInputStream, outputStream);
        } catch (Exception e) {
            log.error("下载Excel模板异常", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (templateInputStream != null) {
                templateInputStream.close();
            }
        }
    }

    /**
     * 根据Excel后缀名设置contentType
     */
    private static String setContentTypeAndFileName(String filename, HttpServletResponse response) {
        if (StringUtils.isEmpty(filename)) {
            filename = "Excel.xlsx";
        }
        if ((!filename.toLowerCase().endsWith(EXCEL_FORMAT_2003) && !filename.toLowerCase().endsWith(EXCEL_FORMAT_2007))) {
            // 无后缀文件名设置为2007版Excel
            filename = filename + EXCEL_FORMAT_2007;
        }
        if (filename.toLowerCase().endsWith(EXCEL_FORMAT_2007)) {
            // 2007版Excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (filename.toLowerCase().endsWith(EXCEL_FORMAT_2003)) {
            // 2003版Excel
            response.setContentType("application/vnd.ms-excel");
        } else {
            log.error("Excel格式错误:{}", filename.toLowerCase());
        }
        return filename;
    }

    /**
     * 导出Excel 单个sheet页
     *
     * @param filename 文件名
     * @param sheetDto sheetDto
     */
    public static void exportExcel(String filename, SheetDTO sheetDto) {
        List<SheetDTO> sheetDtoList = new ArrayList<>();
        sheetDtoList.add(sheetDto);
        exportExcel(filename, sheetDtoList);
    }

    /**
     * 导出Excel 多个sheet页
     *
     * @param filename     文件名
     * @param sheetDtoList sheetDtoList
     */
    public static void exportExcel(String filename, List<SheetDTO> sheetDtoList) {
        ExcelWriter excelWriter = EasyExcelFactory.write(filename).build();
        writeSheetsToExcel(excelWriter, sheetDtoList);
    }

    /**
     * 导出Excel 单个sheet页
     *
     * @param filename 文件名
     * @param sheetDto sheetDto
     * @param response 响应
     */
    public static void exportExcel(String filename, SheetDTO sheetDto, HttpServletResponse response) throws IOException {
        List<SheetDTO> sheetDtoList = new ArrayList<>();
        sheetDtoList.add(sheetDto);
        exportExcel(filename, sheetDtoList, response);
    }

    /**
     * 导出Excel 多个sheet页
     *
     * @param filename     文件名
     * @param sheetDtoList sheetDto列表
     * @param response     响应
     */
    public static void exportExcel(String filename, List<SheetDTO> sheetDtoList, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        filename = setContentTypeAndFileName(filename, response);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
        ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream()).build();
        if (sheetDtoList == null) {
            sheetDtoList = new ArrayList<>();
        }
        writeSheetsToExcel(excelWriter, sheetDtoList);
    }

    /**
     * Excel解析监听器
     */
    private static class StringReadListener extends AnalysisEventListener<Map<Integer, String>> {

        private final List<String[]> dataList = new ArrayList<>();

        private final int columnCount;

        public StringReadListener() {
            columnCount = 0;
        }

        public StringReadListener(int columnCount) {
            this.columnCount = columnCount;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            log.info("data:{}", data);
            // data是一个LinkedHashMap类型, 表示一行数据
            Collection<String> collection = data.values();
            if (collection.stream().allMatch(Objects::isNull)) {
                log.info("Skip blank line.");
                return;
            }
            dataList.add(collection.toArray(new String[Math.max(collection.size(), columnCount)]));
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            log.info("Excel parsed finished.");
        }

        /**
         * 获取解析到的数据
         *
         * @return List<String [ ]>
         */
        List<String[]> getDataList() {
            return dataList;
        }
    }

    public static void writeSheetsToExcel(ExcelWriter excelWriter, List<SheetDTO> sheetDtoList) {
        // 写多个sheet页必须指定页码,否则只能导出一页
        int sheetNo = 0;
        for (SheetDTO sheetDto : sheetDtoList) {
            WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheetNo++, sheetDto.getSheetName())
                    .head(sheetDto.getHeadClass())
                    .registerWriteHandler(sheetDto.getWriteHandler() != null ? sheetDto.getWriteHandler() : commonSheetStyle())
                    .build();
            if (sheetDto.getHeadClass() == null && sheetDto.getHeadArrays() != null) {
                writeSheet.setHead(convertHeadList(sheetDto.getHeadArrays()));
            }
            if (sheetDto.getHeadClass() == null && sheetDto.getColumnWidths() != null) {
                // writeSheet.setColumnWidthMap(convertColumnWidthMap(sheetDto.getColumnWidths()));
            }
            excelWriter.write(sheetDto.getDataList() != null ? sheetDto.getDataList() : new ArrayList<>(), writeSheet);
        }
        excelWriter.finish();
    }

    /**
     * 表头数组转List<List<String>>
     *
     * @param headArrays 表头数组
     * @return java.utils.List<java.utils.List < java.lang.String>>
     */
    private static List<List<String>> convertHeadList(String[][] headArrays) {
        if (headArrays == null) {
            return new ArrayList<>();
        }
        List<List<String>> headList = new ArrayList<>(30);
        for (String[] headArray : headArrays) {
            headList.add(Arrays.asList(headArray));
        }
        return headList;
    }

    /**
     * 列宽数组转map
     *
     * @param columnWidths 列宽数组
     * @return java.utils.Map<java.lang.Integer, java.lang.Integer>
     */
    private static Map<Integer, Integer> convertColumnWidthMap(Integer[] columnWidths) {
        if (columnWidths == null) {
            return null;
        }
        Map<Integer, Integer> columnWidthMap = new HashMap<>();
        for (int i = 0; i < columnWidths.length; i++) {
            if (columnWidths[i] != null) {
                columnWidthMap.put(i, columnWidths[i] * 256);
            }
        }
        return columnWidthMap;
    }

    /**
     * 表格样式策略-通用样式
     * 该策略是头是头的样式, 内容是内容的样式
     *
     * @return void
     */
    private static WriteHandler commonSheetStyle() {
        return new HorizontalCellStyleStrategy(commonHeadStyle(), commonContentStyle());
    }

    /**
     * 通用表头样式
     *
     * @return WriteCellStyle
     */
    private static WriteCellStyle commonHeadStyle() {
        return buildWriteCellStyle("微软雅黑", (short) 10, IndexedColors.BLACK.getIndex(), true);
    }

    /**
     * 通用内容样式
     *
     * @return WriteCellStyle
     */
    private static WriteCellStyle commonContentStyle() {
        return buildWriteCellStyle("微软雅黑", (short) 10, IndexedColors.BLACK.getIndex(), false);
    }

    public static WriteCellStyle buildWriteCellStyle(String fontName, Short fontSize, Short fontColor, Boolean isBoldFont) {
        return buildWriteCellStyle(null, fontName, fontSize, fontColor, isBoldFont);
    }

    public static WriteCellStyle buildWriteCellStyle(Short foregroundColor, String fontName, Short fontSize, Short fontColor, Boolean isBoldFont, Boolean isWrapped) {
        WriteCellStyle writeCellStyle = buildWriteCellStyle(foregroundColor, fontName, fontSize, fontColor, isBoldFont);
        writeCellStyle.setWrapped(isWrapped);
        return writeCellStyle;
    }

    /**
     * 从文件路径导出
     */
    public static void exportFromFilePath(String exportFileName, String filePath, HttpServletResponse response,String charsets) throws IOException {
        exportExcelFile(exportFileName, Files.newInputStream(Paths.get(filePath)), response,charsets);
    }

    /**
     * 从文件路径导出
     */
    public static void exportExcelFile(String exportFileName, InputStream templateInputStream, HttpServletResponse response,String charsets) throws IOException {
        OutputStream outputStream = null;
        try {
            if (templateInputStream == null) {
                throw new BusinessException("文件输入流不能为空");
            }
            setContentTypeAndFileName(exportFileName, response);
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(exportFileName, charsets));
            outputStream = response.getOutputStream();
            IOUtils.copy(templateInputStream, outputStream);
        } catch (Exception e) {
            log.error("下载Excel模板异常", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (templateInputStream != null) {
                templateInputStream.close();
            }
        }
    }

    /**
     * 构建表头或内容单元格样式
     *
     * @param foregroundColor 前景色
     * @param fontName        字体名称
     * @param fontSize        字体大小
     * @param fontColor       字体颜色
     * @param isBoldFont      字体是否加粗
     * @return WriteCellStyle
     */
    public static WriteCellStyle buildWriteCellStyle(Short foregroundColor, String fontName, Short fontSize, Short fontColor, Boolean isBoldFont) {
        // 表头样式
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        // 前景色
        if (foregroundColor != null) {
            writeCellStyle.setFillForegroundColor(foregroundColor);
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        }
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体
        WriteFont headWriteFont = new WriteFont();
        if (fontName != null) {
            headWriteFont.setFontName(fontName);
        }
        if (fontSize != null) {
            headWriteFont.setFontHeightInPoints(fontSize);
        }
        if (fontColor != null) {
            headWriteFont.setColor(fontColor);
        }
        if (isBoldFont != null) {
            headWriteFont.setBold(isBoldFont);
        }
        writeCellStyle.setWriteFont(headWriteFont);
        return writeCellStyle;
    }

    /**
     * Excel解析监听器
     */
    private static class DtoReadListener<T> extends AnalysisEventListener<T> {

        private final List<T> dataList = new ArrayList<>();

        @Override
        public void invoke(T data, AnalysisContext context) {
            dataList.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            log.info("Excel parsed finished.");
        }

        /**
         * 获取解析到的数据
         *
         * @return List<T>
         */
        List<T> getDataList() {
            return dataList;
        }
    }
}