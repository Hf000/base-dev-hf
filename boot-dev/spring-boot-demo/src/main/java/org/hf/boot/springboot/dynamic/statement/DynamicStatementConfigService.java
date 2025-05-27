package org.hf.boot.springboot.dynamic.statement;

import org.hf.boot.springboot.pojo.entity.DynamicStatementConfig;

import java.util.List;

public interface DynamicStatementConfigService {

    List<DynamicStatementConfig> getDataApiConfigList();

    DynamicStatementConfigDTO convertConfigDTO(DynamicStatementConfig config);

    void addDynamicStatementConfig(DynamicStatementConfigAddReq req);

}
