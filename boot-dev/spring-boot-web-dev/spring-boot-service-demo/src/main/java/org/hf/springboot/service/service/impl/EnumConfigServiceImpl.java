package org.hf.springboot.service.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.config.enumerate.JavaEnumCacheManager;
import org.hf.common.config.enumerate.NameValueEnum;
import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.springboot.service.exception.BusinessException;
import org.hf.springboot.service.pojo.bo.EnumInfoBO;
import org.hf.springboot.service.pojo.bo.EnumQueryBO;
import org.hf.springboot.service.pojo.entity.EnumInfo;
import org.hf.springboot.service.service.EnumConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p> 枚举service实现类  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 14:08
 */
@Slf4j
@Service
public class EnumConfigServiceImpl implements EnumConfigService {

    @Override
    public Map<String, List<EnumInfoBO>> getEnumByGroups(EnumQueryBO enumQueryBO) {
        return getScopedConfig(enumQueryBO);
    }

    private Map<String, List<EnumInfoBO>> getScopedConfig(EnumQueryBO enumQueryBO) {
        Map<String, List<EnumInfoBO>> map = Maps.newHashMap();
        if (null == enumQueryBO) {
            return map;
        }
        List<String> groups = enumQueryBO.getGroups();
        for (String group : groups) {
            Class<? extends NameValueEnum<?>> enumByGroup = JavaEnumCacheManager.get().findEnumByGroup(group);
            if (null == enumByGroup) {
                // 没找到就去数据库查询
                Map<String, List<EnumInfoBO>> dataBaseConfig = getDataBaseConfig(enumQueryBO);
                map.putAll(dataBaseConfig);
            } else {
                //查到了, 并且是否去数据库查询的标志为true才去数据库查询
                boolean configurableEnum = NameValueEnum.isConfigurableEnum(enumByGroup);
                if (configurableEnum) {
                    Map<String, List<EnumInfoBO>> dataBaseConfig = getDataBaseConfig(enumQueryBO);
                    map.putAll(dataBaseConfig);
                } else {
                    List<NameValueEnum<?>> enums = JavaEnumCacheManager.get().findByGroup(group);
                    if (CollectionUtil.isNotEmpty(enums)) {
                        List<EnumInfoBO> dataList = new ArrayList<>();
                        for (NameValueEnum<?> nv : enums) {
                            EnumInfoBO bo = new EnumInfoBO();
                            bo.setGroup(group);
                            bo.setValue(nv.getValue());
                            bo.setLabel(nv.getName());
                            bo.setParentCode(nv.getParent());
                            dataList.add(bo);
                        }
                        map.put(group, dataList);
                    }
                }
            }
        }
        return map;
    }

    private Map<String, List<EnumInfoBO>> getDataBaseConfig(EnumQueryBO enumQueryBO) {
        Map<String, List<EnumInfoBO>> map = Maps.newHashMap();
        enumQueryBO.getGroups().forEach(group -> {
            map.put(group, getOptionItems(group, enumQueryBO.getLanguage()));
        });
        return map;
    }

    private List<EnumInfoBO> getOptionItems(String group, String language) {
        if (StringUtils.isBlank(group)) {
            return Collections.emptyList();
        }
        List<EnumInfo> list = null;
        try {
            // 从数据库查询枚举数据
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.SYSTEMERROR.getCode(), "查询异常");
        }
        List<EnumInfoBO> dataList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                //数据转换entity转BO
            });
        }
        return dataList;
    }

}
