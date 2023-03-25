package org.hf.application.mybatis.plus.springboot.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hf.MybatisPlusApplication;
import org.hf.application.mybatis.plus.springboot.mapper.UserInfoMapper;
import org.hf.application.mybatis.plus.springboot.pojo.entity.UserInfoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = MybatisPlusApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUserMapper {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void testInsert() {
        UserInfoEntity user = new UserInfoEntity();
        user.setEmail("2@.cn");
        user.setAge(301);
        user.setUserName("caocao1");
        user.setName("曹操1");
        user.setPassword("123456");

        int result = this.userInfoMapper.insert(user); //result数据库受影响的行数
        System.out.println("result => " + result);

        //获取自增长后的id值, 自增长后的id值会回填到user对象中
        System.out.println("id => " + user.getId());
    }

    @Test
    public void testSelectById() {
        UserInfoEntity user = this.userInfoMapper.selectById(2L);
        System.out.println(user);
    }

    @Test
    public void testUpdateById() {
        UserInfoEntity user = new UserInfoEntity();
        user.setId(1); //条件，根据id更新
        user.setAge(19); //更新的字段
        user.setPassword("666666");

        int result = this.userInfoMapper.updateById(user);
        System.out.println("result => " + result);
    }

    @Test
    public void testUpdate() {
        UserInfoEntity user = new UserInfoEntity();
        user.setAge(20); //更新的字段
        user.setPassword("8888888");

        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "zhangsan"); //匹配user_name = zhangsan 的用户数据

        //根据条件做更新
        int result = this.userInfoMapper.update(user, wrapper);
        System.out.println("result => " + result);
    }

    @Test
    public void testUpdate2() {

        UpdateWrapper<UserInfoEntity> wrapper = new UpdateWrapper<>();
        wrapper.set("age", 21).set("password", "999999") //更新的字段
                .eq("user_name", "zhangsan"); //更新的条件

        //根据条件做更新
        int result = this.userInfoMapper.update(null, wrapper);
        System.out.println("result => " + result);
    }

    @Test
    public void testDeleteById(){
        // 根据id删除数据
        int result = this.userInfoMapper.deleteById(9L);
        System.out.println("result => " + result);
    }

    @Test
    public void testDeleteByMap(){

        Map<String,Object> map = new HashMap<>();
        map.put("user_name", "zhangsan");
        map.put("password", "999999");

        // 根据map删除数据，多条件之间是and关系
        int result = this.userInfoMapper.deleteByMap(map);
        System.out.println("result => " + result);
    }

    @Test
    public void testDelete(){

        //用法一：
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_name", "caocao1")
//                .eq("password", "123456");

        //用法二：
        UserInfoEntity user = new UserInfoEntity();
        user.setPassword("123456");
        user.setUserName("caocao");

        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>(user);

        // 根据包装条件做删除
        int result = this.userInfoMapper.delete(wrapper);
        System.out.println("result => " + result);
    }

    @Test
    public void  testDeleteBatchIds(){
        // 根据id批量删除数据
        int result = this.userInfoMapper.deleteBatchIds(Arrays.asList(10L, 11L));
        System.out.println("result => " + result);
    }

    @Test
    public void testSelectBatchIds(){
        // 根据id批量查询数据
        List<UserInfoEntity> users = this.userInfoMapper.selectBatchIds(Arrays.asList(2L, 3L, 4L, 100L));
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectOne(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //查询条件
        wrapper.eq("password", "1234567");
        // 查询的数据超过一条时，会抛出异常
        UserInfoEntity user = this.userInfoMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    public void testSelectCount(){

        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.gt("age", 20); // 条件：年龄大于20岁的用户

        // 根据条件查询数据条数
        Long count = this.userInfoMapper.selectCount(wrapper);
        System.out.println("count => " + count);
    }

    @Test
    public void testSelectList(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //设置查询条件
        wrapper.like("email", "2@.cn");

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    // 测试分页查询
    @Test
    public void testSelectPage(){

        Page<UserInfoEntity> page = new Page<>(3,1); //查询第一页，查询1条数据

        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //设置查询条件
        wrapper.like("email", "2@.cn");

        IPage<UserInfoEntity> iPage = this.userInfoMapper.selectPage(page, wrapper);
        System.out.println("数据总条数： " + iPage.getTotal());
        System.out.println("数据总页数： " + iPage.getPages());
        System.out.println("当前页数： " + iPage.getCurrent());

        List<UserInfoEntity> records = iPage.getRecords();
        for (UserInfoEntity record : records) {
            System.out.println(record);
        }

    }

    /**
     * 自定义的方法
     */
    @Test
    public void testFindById(){
        UserInfoEntity user = this.userInfoMapper.findById(2L);
        System.out.println(user);
    }

    @Test
    public void testAllEq(){

        Map<String,Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", "20");
        params.put("password", null);

        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE password IS NULL AND name = ? AND age = ?
//        wrapper.allEq(params);
        //SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE name = ? AND age = ?
//        wrapper.allEq(params, false);

        //SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE age = ?
//        wrapper.allEq((k, v) -> (k.equals("age") || k.equals("id")) , params);
        //SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE name = ? AND age = ?
        wrapper.allEq((k, v) -> (k.equals("age") || k.equals("id") || k.equals("name")) , params);

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testEq() {
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();

        //SELECT id,user_name,password,name,age,email FROM tb_user WHERE password = ? AND age >= ? AND name IN (?,?,?)
        wrapper.eq("password", "123456")
                .ge("age", 20)
                .in("name", "李四", "王五", "赵六");

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }

    }

    @Test
    public void testLike(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        // SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE name LIKE ?
        // 参数：%五(String)
        wrapper.likeLeft("name", "五");

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testOrderByAgeDesc(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //按照年龄倒序排序
        // SELECT id,user_name,name,age,email AS mail FROM tb_user ORDER BY age DESC
        wrapper.orderByDesc("age");

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testOr(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        // SELECT id,user_name,name,age,email AS mail FROM tb_user WHERE name = ? OR age = ?
        wrapper.eq("name", "王五").or().eq("age", 21);

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelect(){
        QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
        //SELECT id,name,age FROM tb_user WHERE name = ? OR age = ?
        wrapper.eq("name", "王五")
                .or()
                .eq("age", 21)
                .select("id","name","age"); //指定查询的字段

        List<UserInfoEntity> users = this.userInfoMapper.selectList(wrapper);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

}
