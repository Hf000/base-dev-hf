package org.hf.boot.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.dao.UserInfoMapper;
import org.hf.boot.springboot.dynamic.datasource.CustomDynamicDataSource;
import org.hf.boot.springboot.dynamic.datasource.DataSourceContextHolder;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.hf.boot.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("多数据源测试controller")
@RestController
@RequestMapping("/dynamic/dataSource")
public class DynamicDataSourceController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "自定义选择数据源获取数据")
    @GetMapping("/getData/{datasourceName}/{id}")
    public Result<UserInfo> getMasterData(@PathVariable("datasourceName") String datasourceName, @PathVariable("id") Integer id) {
        DataSourceContextHolder.setDataSource(datasourceName);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        DataSourceContextHolder.removeDataSource();
        return Result.success(userInfo);
    }

    @ApiOperation(value = "通过默认的主数据源获取数据")
    @GetMapping("/getMasterData/{id}")
    public Result<UserInfo> getMasterData(@PathVariable("id") Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        return Result.success(userInfo);
    }

    @ApiOperation(value = "通过配置的从数据源获取数据")
    @GetMapping("/getSlaveData/{id}")
    @CustomDynamicDataSource("slave")
    public Result<UserInfo> getSlaveData(@PathVariable("id") Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        return Result.success(userInfo);
    }

    @ApiOperation(value = "通过配置的从数据源获取数据")
    @GetMapping("/saveData/{datasourceName}/{userName}")
    @CustomDynamicDataSource("slave")
    public Result<Void> saveData(@PathVariable("datasourceName") String datasourceName, @PathVariable("userName") String userName) {
        DataSourceContextHolder.setDataSource(datasourceName);
        userService.saveDynamicSourceData(userName);
        DataSourceContextHolder.removeDataSource();
        return Result.success(null);
    }
}