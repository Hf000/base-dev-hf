package org.hf.springboot.web.controller;

import javafx.scene.control.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.mybatis.pagehelper.ControllerPagination;
import org.hf.springboot.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hf
 * @since 2021-11-02 17:18
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("page")
    @ControllerPagination
    public Object testPaginationAnnotation(@RequestParam("pageNo") String pageNo, @RequestParam("pageSize") String pageSize) {
        log.info("接收到的参数===>pageNo:{}, pageSize:{}", pageNo, pageSize);
        return userService.list();
    }

}
