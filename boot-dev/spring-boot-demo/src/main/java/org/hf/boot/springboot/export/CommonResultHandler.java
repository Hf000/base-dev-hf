package org.hf.boot.springboot.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流式导出excel实现 - 2
 * 数据库查询结果处理抽象类
 */
@Getter
@Slf4j
public abstract class CommonResultHandler<T, R> implements ResultHandler<T> {

    protected final HttpServletResponse response;

    protected final ExcelWriter writer;

    protected WriteSheet sheet;

    protected final Integer batchSize = 500;

    /**
     *  导出exel表对应的dto list
     */
    protected final List<R> rowDataList;

    /**
     * @param response http请求response
     * @param clazz  导出exel表对应的dto的类
     */
    public CommonResultHandler(HttpServletResponse response, Class<R> clazz) throws IOException {
        this.response = response;
        this.writer = EasyExcel.write(response.getOutputStream(), clazz).build();
        rowDataList = new ArrayList<>(1);
        this.initSheet();
    }

    /**
     * 初始化导出写入的对象
     */
    public void initSheet(){
        this.sheet = EasyExcel.writerSheet().build();
    }

    /**
     * 通过mybatis查询数据库后得到结果集并导出数据
     * @param resultContext  SqlSessionTemplate 调用 sql 查询出来的接口查询得到的结果集
     */
    @Override
    @SneakyThrows
    public void handleResult(ResultContext<? extends T> resultContext){
        T obj = resultContext.getResultObject();
        rowDataList.add(processing(obj));
        if(batchSize == rowDataList.size()){
            writer.write(rowDataList, sheet);
            rowDataList.clear();
        }
    }

    /**
     * 进行最后一次数据导出处理
     */
    public void finish(){
        if(CollectionUtils.isNotEmpty(rowDataList)){
            writer.write(rowDataList, sheet);
            rowDataList.clear();
        }
    }

    /**
     * 将数据库查询出来的对象转换成导出所需对象,具体子类对象实现数据转换方法
     * @param t   sql 查询得到的结果集的类
     * @return {@link R }  转换成导出excel 表格对应的类
     */
    public abstract R processing(T t);

}