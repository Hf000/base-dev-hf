package org.hf.boot.springboot.dynamic.statement;

import com.alibaba.fastjson2.JSONObject;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hf.boot.springboot.controller.DynamicStatementDataApiController;
import org.hf.boot.springboot.pojo.entity.DynamicStatementConfig;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态statement管理器
 */
@Slf4j
public class DynamicStatementDataApiManager {

    private final RequestMappingHandlerMapping handlerMapping;

    private final DynamicMappedStatementBuilder mappedStatementBuilder;

    private final SqlSessionFactory sqlSessionFactory;

    private final DynamicStatementConfigService statementConfigService;

    private final Map<String, DynamicStatementConfigDTO> statementConfigMap = new ConcurrentHashMap<>();

    private final Map<String, RequestMappingInfo> requestMappingInfoMap = new ConcurrentHashMap<>();

    public DynamicStatementDataApiManager(RequestMappingHandlerMapping handlerMapping,
                                          DynamicMappedStatementBuilder mappedStatementBuilder,
                                          SqlSessionFactory sqlSessionFactory,
                                          DynamicStatementConfigService statementConfigService) {
        // 初始化相关对象
        this.handlerMapping = handlerMapping;
        this.mappedStatementBuilder = mappedStatementBuilder;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementConfigService = statementConfigService;
    }

    @PostConstruct
    public void init() {
        // 加载动态statement的api配置
        initStatementConfig();
        // 循环注册配置的statement信息
        statementConfigMap.forEach((k,v)->{
            registerConfig(v);
        });
    }

    private void initStatementConfig() {
        // 查询数据库配置的动态statement配置
        List<DynamicStatementConfig> statementConfigList = statementConfigService.getDataApiConfigList();
        statementConfigList.forEach(statementConfig -> {
            try {
                DynamicStatementConfigDTO statementConfigDTO = statementConfigService.convertConfigDTO(statementConfig);
                statementConfigMap.put(statementConfig.getMappingPath(), statementConfigDTO);
            } catch (Exception e) {
                log.error("加载动态statement配置出错, 请检查cofigId={}", statementConfig.getId(), e);
            }
        });
    }

    public void registerConfig(DynamicStatementConfigDTO configDTO){
        synchronized (this){
            try {
                // 注册controller
                registerController(configDTO);
                // 新增mappedStatement
                addMappedStatement(configDTO);
            }catch (Exception e){
                log.error("注册springMVC请求或新增Mybatis的mappedStatement失败, statementId={}", configDTO.getStatementId(), e);
            }
        }
    }

    /**
     * 添加mappedStatement
     */
    private void addMappedStatement(DynamicStatementConfigDTO configDTO){
        Configuration configuration = sqlSessionFactory.getConfiguration();
        List<ColumnDTO> columns = configDTO.getColumnsList();
        List<ResultMapping> resultMappings = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(columns)){
            for (ColumnDTO column : columns) {
                DataFieldTypeEnum fieldTypeEnum = DataFieldTypeEnum.matchByCode(column.getFieldType());
                Class<?> clazz;
                if (fieldTypeEnum == null) {
                    clazz = String.class;
                } else {
                    switch (fieldTypeEnum){
                        case NUMBER:
                            clazz = BigDecimal.class;
                            break;
                        case DATE:
                            clazz = Date.class;
                            break;
                        case BOOLEAN:
                            clazz = Boolean.class;
                            break;
                        default:
                            clazz = String.class;
                    }
                }
                // 构建返回列的信息
                ResultMapping resultMapping = new ResultMapping.Builder(configuration, column.getName(), column.getColumn(), clazz).build();
                resultMappings.add(resultMapping);
            }
        }
        mappedStatementBuilder.addMappedStatement(configDTO.getSqlScript(), configDTO.getStatementId(), Map.class, resultMappings);
    }

    /**
     * 注册controller到mapping
     */
    private void registerController(DynamicStatementConfigDTO configDTO){
        if(StringUtil.isBlank(configDTO.getMappingPath())){
            // 如果配置的接口请求路径为空, 则不进行注册
            return;
        }
        RequestMappingInfo requestMappingInfo = builderRequestMappingInfo(configDTO);
        requestMappingInfoMap.put(configDTO.getMappingPath(), requestMappingInfo);
        try {
            // 将请求路径映射到指定的controller的方法
            handlerMapping.registerMapping(requestMappingInfo, "dynamicStatementDataApiController",
                    DynamicStatementDataApiController.class.getDeclaredMethod("postJson", JSONObject.class));
        }catch (Exception e){
            log.error("动态映射statement请求接口执行失败, url={}", configDTO.getMappingPath(), e);
        }
    }

    /**
     * 构建requestMappingInfo
     */
    @SuppressWarnings(value = {"all"})
    private RequestMappingInfo builderRequestMappingInfo(DynamicStatementConfigDTO configDTO) {
        RequestMappingInfo.Builder requestMappingInfoBuilder = RequestMappingInfo.paths(configDTO.getMappingPath());
        //默认为POST请求
        RequestMethod requestMethod = RequestMethod.POST;
        MappingMethodEnum methodEnum = configDTO.getMethodEnum();
        if(methodEnum != null){
            requestMethod =  RequestMethod.valueOf(methodEnum.getCode());
        }
        requestMappingInfoBuilder.methods(requestMethod);
        //默认为 application/json;charset=UTF-8
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        String mappingProduces = configDTO.getMappingProduces();
        if(StringUtils.isNotBlank(mappingProduces)){
            mediaType = mappingProduces;
        }
        requestMappingInfoBuilder.produces(mediaType);
        List<String> requestParamsList = configDTO.getRequestParamsList();
        if(CollectionUtils.isNotEmpty(requestParamsList)){
            requestMappingInfoBuilder.params(requestParamsList.toArray(new String[requestParamsList.size()]));
        }
        return requestMappingInfoBuilder.build();
    }

    public void unregisterConfig(DynamicStatementConfigDTO configDTO){
        synchronized (this){
            // 注销controller
            unregisterController(configDTO.getMappingPath());
            // 移除mappedStatement
            removeMappedStatement(configDTO.getStatementId(), configDTO.getMappingPath());
        }
    }

    /**
     * 注销controller接口
     */
    private void unregisterController(String mappingPath) {
        RequestMappingInfo requestMappingInfo = requestMappingInfoMap.remove(mappingPath);
        if (Objects.isNull(requestMappingInfo)) {
            log.warn("注销动态statement的请求接口时, 未找到接口请求路径的映射url={}", mappingPath);
            return;
        }
        handlerMapping.unregisterMapping(requestMappingInfo);
    }

    /**
     * 移除mappedStatement
     */
    private void removeMappedStatement(String statementId, String mappingPath){
        statementConfigMap.remove(mappingPath);
        mappedStatementBuilder.removeMappedStatement(statementId);
    }

    /**
     * 根据接口请求路径获取配置信息
     */
    public DynamicStatementConfigDTO getConfigInfoByPath(String path){
        return statementConfigMap.get(path);
    }

    /**
     * 添加
     */
    public DynamicStatementConfigDTO addConfigMap(DynamicStatementConfigDTO configDTO){
        return statementConfigMap.put(configDTO.getMappingPath(), configDTO);
    }

    /**
     * 测试demo
     */
    private void testInitConfig(){
        List<DynamicStatementConfigDTO> configDTOList = new ArrayList<>();
        DynamicStatementConfigDTO configDTO = new DynamicStatementConfigDTO();
        configDTO.setApiNo("test0001");
        configDTO.setApiName("测试接口");
        configDTO.setMappingPath("/data/api/test");
        configDTO.setMappingProduces(MediaType.APPLICATION_JSON_UTF8_VALUE);
        configDTO.setMethodEnum(MappingMethodEnum.POST);
        configDTO.setSelectEnum(SelectTypeEnum.SELECT_ONE);
        configDTO.setStatementId("org.hf.boot.springboot.dao.dynamic.test1");
        ParameterDTO parameter1 = new ParameterDTO();
        parameter1.setName("userId");
        parameter1.setRequired(false);
        parameter1.setFieldType(DataFieldTypeEnum.NUMBER.getCode());
        ParameterDTO parameter2 = new ParameterDTO();
        parameter2.setName("userName");
        parameter2.setFieldType(DataFieldTypeEnum.STRING.getCode());
        parameter2.setRequired(false);
        ParameterDTO parameter3 = new ParameterDTO();
        parameter3.setName("userIdList");
        parameter3.setRequired(false);
        parameter3.setFieldType(DataFieldTypeEnum.ARRAY.getCode());
        parameter3.setArrayFieldType(DataFieldTypeEnum.NUMBER.getCode());
        List<ParameterDTO> parameterDTOS = Arrays.asList(parameter1, parameter2, parameter3);
        configDTO.setParametersList(parameterDTOS);
        String sqlScript = "select id, user_name from user_info \n" +
                "<where>\n" +
                "\t<if test=\"userId != null\">\n" +
                "\t\tand id =  #{userId}\n" +
                "\t</if>\n" +
                "\t<if test=\"userName != null and userName != ''\">\n" +
                "\t\tand user_name =  #{userName}\n" +
                "\t</if>\n" +
                "\t<if test=\"userIdList != null and userIdList.size() > 0\">\n" +
                "\t\tand id in \n" +
                "\t\t<foreach collection=\"userIdList\" open=\"(\" close=\")\" separator=\",\" item=\"userId\">\n" +
                "\t\t\t#{userId}\n" +
                "\t\t</foreach>\n" +
                "\t</if>\n" +
                "</where>";
        configDTO.setSqlScript(sqlScript);
        ColumnDTO column1 = new ColumnDTO();
        column1.setName("id");
        column1.setFieldType(DataFieldTypeEnum.NUMBER.getCode());
        column1.setColumn("id");
        ColumnDTO column2 = new ColumnDTO();
        column2.setName("userName");
        column2.setFieldType(DataFieldTypeEnum.STRING.getCode());
        column2.setColumn("user_name");
        List<ColumnDTO> columnDTOS = Arrays.asList(column1, column2);
        configDTO.setColumnsList(columnDTOS);
        configDTO.setDataSourceKey("master");
        statementConfigMap.put("/data/api/test", configDTO);
    }
}