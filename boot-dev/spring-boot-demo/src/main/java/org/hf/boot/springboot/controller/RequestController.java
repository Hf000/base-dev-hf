package org.hf.boot.springboot.controller;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 请求示例 </p>
 * @author hufei
 * @date 2022/8/13 8:50
*/
@Slf4j
@RestController
public class RequestController {

    @ApiOperation("与请求方式无关;直接定义接收参数,请求参数必须与定义的参数名一致;1.可以接收拼接在路径上的参数" +
            "url?name=hf&userId=123,2.也可以接收form-data类型的传参,3.可以接收x-www-form-urlencoded类型的传参(不支持GET请求" +
            "),4.不支持JSON传参,5.如果是字符串类型入参传递多个会用\",\"分隔")
    @RequestMapping(value = "/test1")
    public Result<Map<String, Object>> test1(String name, Integer userId) {
        log.info("params:{}-{}", name, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("userId", userId);
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;接收路径上的参数,请求的参数必须与@RequestParam注解的值名称一致;1.可以接收拼接在路径上的参数" +
            "url?name=hf&userId=123,2.也可以接收form-data类型的传参,3.可以接收x-www-form-urlencoded类型的传参(不支持GET请求" +
            "),4.不支持JSON传参,5.如果是字符串类型入参传递多个会用\",\"分隔")
    @RequestMapping(value = "/test2")
    public Result<Map<String, Object>> test2(@RequestParam("name") String name, @RequestParam("userId") Integer userId) {
        log.info("params:{}-{}", name, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("userId", userId);
        return Result.success(result);
    }

    @ApiOperation("接收body中的请求对象,一般采用json方式参数, 推荐使用POST请求")
    @RequestMapping(value = "/test3")
    public Result<Map<String, Object>> test3(@RequestBody Req req) {
        log.info("params:{}-{}", req.getName(), req.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("name", req.getName());
        result.put("userId", req.getUserId());
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;直接定义接收参数实体,请求参数必须与定义实体的属性名一致;1.可以接收拼接在路径上的参数" +
            "url?name=hf&userId=123,2.也可以接收form-data类型的传参,3.可以接收x-www-form-urlencoded类型的传参(不支持GET请求" +
            "),4.不支持JSON传参,5.如果是字符串类型入参传递多个会用\",\"分隔")
    @RequestMapping(value = "/test4")
    public Result<Map<String, Object>> test4(Req req) {
        log.info("params:{}-{}", req.getName(), req.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("name", req.getName());
        result.put("userId", req.getUserId());
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;直接定义接收参数实体,请求参数必须与定义实体的属性名一致,@ModelAttribute(\"req\")注解主要作用" +
            "是自动将数据暴露为模型数据用于视图页面展示时使用,比如此处注解value为user,前端视图就可以通过${user.name}来获取绑定的" +
            "命令对象的属性;1.可以接收拼接在路径上的参数url?name=hf&userId=123,2.也可以接收form-data类型的传参,3.可以接收" +
            "x-www-form-urlencoded类型的传参(不支持GET请求),4.不支持JSON传参,5.如果是字符串类型入参传递多个会用\",\"分隔")
    @RequestMapping(value = "/test5")
    public Result<Map<String, Object>> test5(@ModelAttribute("req") Req req) {
        log.info("params:{}-{}", req.getName(), req.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("name", req.getName());
        result.put("userId", req.getUserId());
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;直接定义接收参数实体,请求参数必须与获取的属性名一致,1.可以接收拼接在路径上的参数" +
            "url?name=hf&userId=123,2.也可以接收form-data类型的传参,3.可以接收x-www-form-urlencoded类型的传参(不支持GET请求" +
            "),4.不支持JSON传参,5.如果某个参数传入多次只会获取到第一个")
    @RequestMapping(value = "/test6")
    public Result<Map<String, Object>> test6(HttpServletRequest request) {
        log.info("params:{}-{}", request.getParameter("name"), request.getParameter("userId"));
        Map<String, Object> result = new HashMap<>();
        result.put("name", request.getParameter("name"));
        result.put("userId", request.getParameter("userId"));
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;RestFul风格传参,请求路径上的参数位置需要和后端的接收位置一致,1.可以接收拼接在路径上的参数" +
            "url/hf/123,或者通过设置是否必传属性值来实现不传某个入参url/123")
    @RequestMapping(value = {"/test7/{name}/{userId}", "/test/{userId}"})
    public Result<Map<String, Object>> test7(@PathVariable(value = "name", required = false) String name, @PathVariable("userId") Integer userId) {
        log.info("params:{}-{}", name, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("userId", userId);
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;接收数组可以根据参数名称用','分隔例如:url?nameList=hufei,abc;也可以通过拼接多个同名的入参例如:" +
            "url?nameList=hufei&nameList=abc;但是不可以既用','分隔又用多个同名参数传参,如果String类型的入参既用','分隔又用多个" +
            "同名入参传参就会将用','分隔的参数当做一个集合元素")
    @RequestMapping(value = {"/test8"})
    public Result<Map<String, Object>> test8(Req req) {
        Map<String, Object> result = new HashMap<>();
        result.put("nameList", req.getNameList());
        result.put("userIdList", req.getUserIdList());
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;接收数组可以根据参数名称用','分隔例如:url?nameList=hufei,abc;也可以通过拼接多个同名的入参例如:" +
            "url?nameList=hufei&nameList=abc;但是不可以既用','分隔又用多个同名参数传参,如果String类型的入参既用','分隔又用多个" +
            "同名入参传参就会将用','分隔的参数当做一个集合元素")
    @RequestMapping(value = {"/test9"})
    public Result<Map<String, Object>> test9(@RequestParam("nameList") List<String> nameList, @RequestParam("userIdList") List<Integer> userIdList) {
        Map<String, Object> result = new HashMap<>();
        result.put("nameList", nameList);
        result.put("userIdList", userIdList);
        return Result.success(result);
    }

    @ApiOperation("与请求方式无关;通过body传参,格式类型为JSON")
    @RequestMapping(value = {"/test10"})
    public Result<Map<String, Object>> test10(@RequestBody Map<String, Object> map) {
        return Result.success(map);
    }

    @ApiOperation("与请求方式无关;@RequestPart注解用在multipart/form-data表单提交请求的方法上,可以接收复杂的请求域（像json、" +
            "xml）,可以将json字符串解析成对象(请求时需要将content type设置为application/json);@RequestParam注解只能接收" +
            "String类型的key-value形式的值")
    @RequestMapping(value = {"/test11"})
    public Result<Req> test11(@RequestPart("file") MultipartFile file, @RequestParam("name") String name,
                              @RequestPart("reqJson") Req req, @RequestParam("newFile") MultipartFile newFile) {
        return Result.success(req);
    }

    @ApiOperation("与请求方式无关,推荐使用POST请求;文件上传的几种接收请求参数的方式")
    @RequestMapping(value = {"/test12"})
    public Result<Req> test12(@RequestPart("file") MultipartFile file, @RequestParam("newFile") MultipartFile newFile,
                              HttpServletRequest request, Req req) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile fileName = multipartRequest.getFile("fileName");
        MultipartFile multipartFile = req.getMultipartFile();
        return Result.success(req);
    }

    @Data
    private static class Req {
        private Integer userId;
        private String name;
        private List<String> nameList;
        private List<Integer> userIdList;
        private MultipartFile multipartFile;
    }
}
