package org.hf.boot.springboot.dynamic.statement;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.dynamic.datasource.DataSourceContextHolder;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DynamicStatementDataApiServiceImpl implements DynamicStatementDataApiService {

    @Autowired
    private DynamicStatementDataApiManager dynamicConfigManager;

    @Autowired
    private ParameterParser parameterParser;

    @Autowired
    private DynamicSqlExecutor dynamicSqlExecutor;

    @Override
    public Object postJson(String path, JSONObject paramJson){
        if (path.contains("/springboot")) {
            path = path.replaceFirst("/springboot", "");
        }
        DynamicStatementConfigDTO configDTO = dynamicConfigManager.getConfigInfoByPath(path);
        if(Objects.isNull(configDTO)){
            throw new BusinessException("接口不存在或已下架");
        }
        String statementId = configDTO.getStatementId();
        List<ParameterDTO> paramConfigList = configDTO.getParametersList();
        Map<String, Object> parameter = parameterParser.parseMap(paramJson, paramConfigList);
        //设置数据源
        String dataSourceKey = configDTO.getDataSourceKey();
        dataSourceKey = StringUtils.isBlank(dataSourceKey) ? "master" : dataSourceKey;
        DataSourceContextHolder.setDataSource(dataSourceKey);
        Object execResult;
        SelectTypeEnum selectEnum = configDTO.getSelectEnum();
        try {
            if (SelectTypeEnum.SELECT_ONE.equals(selectEnum)) {
                execResult = dynamicSqlExecutor.selectOne(statementId, parameter);
            } else if (SelectTypeEnum.SELECT_PAGE.equals(selectEnum)) {
                BigDecimal pageNumValue = (BigDecimal) parameter.get("pageNum");
                BigDecimal pageSizeValue = (BigDecimal) parameter.get("pageSize");
                Integer pageNum = Objects.nonNull(pageNumValue) ? pageNumValue.intValue() : 1;
                Integer pageSize = Objects.nonNull(pageSizeValue) ? pageSizeValue.intValue() : 10;
                execResult = dynamicSqlExecutor.selectPage(statementId, parameter, pageNum, pageSize);
            } else {
                execResult = dynamicSqlExecutor.selectList(statementId, parameter);
            }
        }finally {
            DataSourceContextHolder.removeDataSource();
        }
        return execResult;
    }

}