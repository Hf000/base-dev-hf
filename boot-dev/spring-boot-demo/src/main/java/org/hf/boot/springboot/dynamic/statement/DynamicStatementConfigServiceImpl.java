package org.hf.boot.springboot.dynamic.statement;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.dao.DynamicStatementConfigMapper;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.pojo.entity.DynamicStatementConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class DynamicStatementConfigServiceImpl implements DynamicStatementConfigService {

    @Resource
    private DynamicStatementConfigMapper dynamicStatementConfigMapper;

    @Autowired
    @Lazy
    private DynamicStatementDataApiManager dynamicDataApiManager;


    @Override
    public List<DynamicStatementConfig> getDataApiConfigList() {
        DynamicStatementConfig queryParam = new DynamicStatementConfig();
        queryParam.setDeleteState(new Byte("0"));
        return dynamicStatementConfigMapper.select(queryParam);
    }

    @Override
    public DynamicStatementConfigDTO convertConfigDTO(DynamicStatementConfig config) {
        if (config == null) {
            return null;
        }
        DynamicStatementConfigDTO configDTO = new DynamicStatementConfigDTO();
        BeanUtils.copyProperties(config, configDTO);
        configDTO.setMethodEnum(MappingMethodEnum.matchByCode(config.getMappingMethod()));
        configDTO.setSelectEnum(SelectTypeEnum.matchByCode(config.getSelectType()));
        configDTO.setParametersList(JSON.parseArray(config.getParameters(), ParameterDTO.class));
        configDTO.setColumnsList(JSON.parseArray(config.getColumns(), ColumnDTO.class));
        return configDTO;
    }

    @Override
    public void addDynamicStatementConfig(DynamicStatementConfigAddReq req) {
        DynamicStatementConfig queryParam = new DynamicStatementConfig();
        queryParam.setApiName(req.getApiName());
        queryParam.setDeleteState(new Byte("0"));
        if (dynamicStatementConfigMapper.selectCount(queryParam) > 0) {
            throw new BusinessException("接口名称已存在");
        }
        queryParam.setApiName(null);
        queryParam.setMappingPath(req.getMappingPath());
        if (dynamicStatementConfigMapper.selectCount(queryParam) > 0) {
            throw new BusinessException("接口路径已存在");
        }
        // 保存配置
        DynamicStatementConfig addEntity = new DynamicStatementConfig();
        BeanUtils.copyProperties(req, addEntity);
        String serialNo = UUID.randomUUID().toString().replaceAll("-", "");
        addEntity.setApiNo("APINO" + serialNo);
        addEntity.setMappingMethod(req.getMethodEnum().getCode());
        addEntity.setStatementId("SNO" + serialNo);
        List<ParameterDTO> parametersList = req.getParametersList();
        if (CollectionUtils.isNotEmpty(parametersList)) {
            addEntity.setParameters(JSON.toJSONString(parametersList));
        }
        List<ColumnDTO> columnsList = req.getColumnsList();
        if (CollectionUtils.isNotEmpty(columnsList)) {
            addEntity.setColumns(JSON.toJSONString(columnsList));
        }
        addEntity.setSelectType(req.getSelectEnum().getCode());
        addEntity.setDeleteState(new Byte("0"));
        addEntity.setUpdatedBy("system");
        addEntity.setCreatedBy("system");
        dynamicStatementConfigMapper.insertSelective(addEntity);
        // 加载配置
        DynamicStatementConfigDTO configDTO = convertConfigDTO(addEntity);
        dynamicDataApiManager.unregisterConfig(configDTO);
        dynamicDataApiManager.addConfigMap(configDTO);
        dynamicDataApiManager.registerConfig(configDTO);
    }

}