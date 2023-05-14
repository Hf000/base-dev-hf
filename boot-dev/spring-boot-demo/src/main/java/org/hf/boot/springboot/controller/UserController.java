package org.hf.boot.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.pojo.dto.UserInfoReq;
import org.hf.boot.springboot.pojo.dto.UserReq;
import org.hf.boot.springboot.pojo.entity.User;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.hf.boot.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p> 用户控制处理器 </p>
 * @author hufei
 * @date 2022/8/13 9:02
*/
@Api(tags = "用户测试类")
@RestController
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @ApiOperation(value = "查询用户信息")
    @GetMapping("/find/{id}")
    public User getUserInfo(@PathVariable Long id) {
        if (id == null) {
            id = 0L;
        }
        return userServiceImpl.findUserInfo(id);
    }

    @RequestMapping("/save")
    public User saveUserInfo(String userName, String password, String name, Integer age, Integer sex, Date birthday,
                               String note, Date created, Date updated) {
        return userServiceImpl.saveUserInfo(userName, password, name, age, sex, birthday, note, created, updated);
    }

    @GetMapping("/findUser/{id}")
    public User findUserInfo(@PathVariable Long id) {
        if (id == null) {
            id = 0L;
        }
        return userServiceImpl.getUser(id);
    }

    @GetMapping("/findUserAll")
    public List<User> findUserInfo() {
        // userServiceImpl.selectAll();
        return userServiceImpl.getUserAll();
    }

    @ApiOperation("根据条件查询用户信息")
    @PostMapping("/findUserAllByCondition")
    public List<User> findUserAllByCondition(@RequestBody UserReq req) {
        return userServiceImpl.getUserAll();
    }

    @GetMapping("/findUserInfoNew")
    public Result<List<UserInfo>> findUserInfoNew() {
        Result<List<UserInfo>> result = new Result<>();
        result.setData(userServiceImpl.findUserInfoNew());
        return result;
    }

    @PostMapping("addUserInfo")
    public Result<Void> addUserInfo(@RequestBody UserInfoReq req) {
        userServiceImpl.addUserInfo(req);
        return new Result<>();
    }

    @PostMapping("addUserInfoAysnc")
    public Result<Void> addUserInfoAysnc(@RequestBody UserInfoReq req) {
        userServiceImpl.addUserInfoAysnc(req);
        return new Result<>();
    }

}
