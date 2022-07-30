//package org.hf.application.mybatis.plus.springboot.controller;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.hf.common.web.mybatis.pagehelper.ControllerPagination;
//import org.hf.common.web.pojo.vo.ResponseVO;
//import org.hf.common.web.utils.ResponseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * <p> 测试pageHelper </p>
// *
// * @author hufei
// * @version 1.0.0
// * @date 2021/10/15 16:55
// */
//@Slf4j
//@RestController
//@RequestMapping("pageHelper")
//public class PageHelperController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("test")
//    public ResponseVO<PageInfo<User>> findListUser() {
//        PageInfo<User> listUser = userService.findListUser();
//        User user = userService.findById(1);
//        System.out.println("==========================================");
//        log.info(user.toString());
//        System.out.println("==========================================");
//        return ResponseUtil.success(listUser);
//    }
//
//    @GetMapping("test/list")
//    public ResponseVO<PageInfo<User>> findList() {
//        PageInfo<User> pageInfo = null;
//        try {
//            //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
//            PageHelper.startPage(2,3);
//            //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页
//            List<User> cityList = userService.findAll();
//            //3.使用PageInfo包装查询后的结果,3是连续显示的条数
//            pageInfo = new PageInfo<User>(cityList, 3);
//        }finally {
//            //清理 ThreadLocal 存储的分页参数,保证线程安全
//            PageHelper.clearPage();
//        }
//        return ResponseUtil.success(pageInfo);
//    }
//
//    @RequestMapping("test/a")
//    public ResponseVO<Void> test(@RequestParam("file") String file, @RequestParam("filename") String fileName) {
//        log.info("接收到的参数===>file:{}, filename:{}", file, fileName);
//        return ResponseUtil.success();
//    }
//
//    @GetMapping("test/page")
//    @ControllerPagination
//    public Object testPaginationAnnotation(@RequestParam("pageNo") String pageNo, @RequestParam("pageSize") String pageSize) {
//        log.info("接收到的参数===>pageNo:{}, pageSize:{}", pageNo, pageSize);
//        return userService.findAll();
//    }
//
//}
