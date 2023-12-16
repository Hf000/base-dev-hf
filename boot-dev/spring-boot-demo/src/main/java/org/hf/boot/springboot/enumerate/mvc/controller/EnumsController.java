package org.hf.boot.springboot.enumerate.mvc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.enumerate.CommonEnum;
import org.hf.boot.springboot.enumerate.NewsStatus;
import org.hf.boot.springboot.enumerate.mvc.CommonEnumRegistry;
import org.hf.boot.springboot.enumerate.mvc.pojo.CommonEnumVO;
import org.hf.boot.springboot.enumerate.repository.jpa.JpaNewsEntity;
import org.hf.boot.springboot.enumerate.repository.jpa.JpaNewsEntityRepository;
import org.hf.boot.springboot.enumerate.repository.mybatis.MyBatisNewsEntity;
import org.hf.boot.springboot.enumerate.repository.mybatis.MyBatisNewsMapper;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "自定义枚举转换及查询处理枚举相关接口")
@RestController
@RequestMapping("enums")
public class EnumsController {

    @Autowired
    private JpaNewsEntityRepository jpaNewsEntityRepository;

    @Autowired
    private MyBatisNewsMapper myBatisNewsMapper;

    @Autowired(required = false)
    private CommonEnumRegistry commonEnumRegistry;

    @ApiOperation("根据枚举项name获取对应的枚举项详情, @RequestParam传参")
    @GetMapping("paramToEnum")
    public Result<CommonEnumVO> paramToEnum(@RequestParam("newsStatus") NewsStatus newsStatus){
        return Result.success(CommonEnumVO.from(newsStatus));
    }

    @ApiOperation("根据枚举项name获取对应的枚举项详情, @PathVariable传参")
    @GetMapping("pathToEnum/{newsStatus}")
    public Result<CommonEnumVO> pathToEnum(@PathVariable("newsStatus") NewsStatus newsStatus){
        return Result.success(CommonEnumVO.from(newsStatus));
    }

    @ApiOperation("根据枚举项name获取对应的枚举项详情, @RequestBody的json格式传参")
    @PostMapping("jsonToEnum")
    public Result<CommonEnumVO> jsonToEnum(@RequestBody NewsStatusRequestBody newsStatusRequestBody){
        return Result.success(CommonEnumVO.from(newsStatusRequestBody.getNewsStatus()));
    }

    @ApiOperation("获取指定枚举对象的所有枚举项")
    @GetMapping("bodyToJson")
    public Result<NewsStatusResponseBody> bodyToJson(){
        NewsStatusResponseBody newsStatusResponseBody = new NewsStatusResponseBody();
        newsStatusResponseBody.setNewsStatus(Arrays.asList(NewsStatus.values()));
        return Result.success(newsStatusResponseBody);
    }

    @Data
    public static class NewsStatusRequestBody {
        private NewsStatus newsStatus;
    }

    @Data
    public static class NewsStatusResponseBody {
        private List<NewsStatus> newsStatus;
    }

    @ApiOperation("根据id使用spring data jpa查询数据库并将枚举类型字段装换成枚举对象返回")
    @GetMapping("findDatabaseJpa/{id}")
    public Result<JpaNewsEntity> findDatabaseJpa(@PathVariable("id") Integer id){
        JpaNewsEntity referenceById = jpaNewsEntityRepository.findById(id).orElse(null);
        return Result.success(referenceById);
    }

    @ApiOperation("根据id使用mybatis查询数据库并将枚举类型字段装换成枚举对象返回")
    @GetMapping("findDatabaseMybatis/{id}")
    public Result<MyBatisNewsEntity> findDatabaseMybatis(@PathVariable("id") Integer id){
        MyBatisNewsEntity byId = this.myBatisNewsMapper.findById(id);
        return Result.success(byId);
    }

    @ApiOperation("获取所有枚举信息")
    @GetMapping("/all")
    public Result<Map<String, List<CommonEnumVO>>> allEnums(){
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        Map<String, List<CommonEnumVO>> dictVo = Maps.newHashMapWithExpectedSize(dict.size());
        for (Map.Entry<String, List<CommonEnum>> entry : dict.entrySet()){
            dictVo.put(entry.getKey(), CommonEnumVO.from(entry.getValue()));
        }
        return Result.success(dictVo);
    }

    @ApiOperation("获取所有枚举类名称")
    @GetMapping("/types")
    public Result<List<String>> enumTypes(){
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        return Result.success(Lists.newArrayList(dict.keySet()));
    }

    @ApiOperation("根据枚举类名称获取所有的枚举值")
    @GetMapping("/{type}")
    public Result<List<CommonEnumVO>> dictByType(@PathVariable("type") String type){
        Map<String, List<CommonEnum>> dict = this.commonEnumRegistry.getNameDict();
        List<CommonEnum> commonEnums = dict.get(type);
        return Result.success(CommonEnumVO.from(commonEnums));
    }
}