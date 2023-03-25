package org.hf.application.custom.framework.service.impl;

import org.hf.application.custom.framework.dao.AccountDao;
import org.hf.application.custom.framework.service.AccountService;

/**
 * <p> Service业务接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:26
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    @Override
    public String one() {
        System.out.println("AccountServiceImpl.one()方法执行");
        return accountDao.one();
    }
}
