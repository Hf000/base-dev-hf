package org.hf.application.custom.framework.dao.impl;

import org.hf.application.custom.framework.dao.AccountDao;

/**
 * <p> dao数据查询接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:24
 */
public class AccountDaoImpl implements AccountDao {

    @Override
    public String one() {
        System.out.println("Dao查询！");
        return "ok";
    }
}
