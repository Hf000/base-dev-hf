package org.hf.application.mongodb.springdata.test;

import org.hf.application.mongodb.springdata.dao.BarrageDao;
import org.hf.application.mongodb.springdata.pojo.entity.Barrage;
import org.hf.application.mongodb.springdata.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/8/6 21:07
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-MongoDB.xml"})
public class BarrageDaoTest {

    @Autowired
    private BarrageDao barrageDao;

    @Test
    public void testSave() {//保存一条数据
        Barrage barrage = new Barrage();
        barrage.setId(1);
        barrage.setContent("测试一下");
        barrage.setTime(DateUtil.getCurrentTime());
        barrageDao.save(barrage);
    }

    @Test
    public void testUpdate() {//更新
        Barrage barrage = new Barrage();
        barrage.setId(7);
        barrage.setContent("测试一下007");
        barrage.setTime(DateUtil.getCurrentTime());
        barrageDao.save(barrage);
    }

    @Test
    public void testDeleete() {//删除
        barrageDao.deleteById(1);
    }

    @Test
    public void testMakeData() {
        Barrage barrage = null;
        for (int i = 1; i <= 10; i++) {
            barrage = new Barrage();
            barrage.setId(i);
            barrage.setContent("测试一下00"+i);
            barrage.setTime(DateUtil.getCurrentTime());
            barrageDao.save(barrage);
        }
    }

    @Test
    public void testfindAll() {//查询所有
        List<Barrage> list = barrageDao.findAll();
        for (Barrage barrage : list) {
            System.out.println(barrage);
        }
    }

    @Test
    public void testfindById() {//根据id查询
        Optional<Barrage> byId = barrageDao.findById(5);
        System.out.println(byId.get());
    }

    @Test
    public void testfindByCondition() {
        Sort sort = Sort.by(Sort.Order.desc("time"));//设置排序条件
        PageRequest page = PageRequest.of(1, 5, sort);//分页
        Page<Barrage> all = barrageDao.findAll(page);
        for (Barrage barrage : all) {
            System.out.println(barrage);
        }
    }

    @Test
    public void testfindByContextLike() {
        List<Barrage> list = barrageDao.findByContentLike("001");
        for (Barrage barrage : list) {
            System.out.println(barrage);
        }
    }

    @Test
    public void testfindByTimeGreaterThan() {
        List<Barrage> list = barrageDao.findByTimeGreaterThan("2020-09-29 05:21:22");
        for (Barrage barrage : list) {
            System.out.println(barrage);
        }
    }

}
