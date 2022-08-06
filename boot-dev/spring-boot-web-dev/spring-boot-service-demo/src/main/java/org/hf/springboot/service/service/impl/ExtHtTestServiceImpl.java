package org.hf.springboot.service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.config.replacebean.CustomBeanReplace;
import org.hf.springboot.service.service.HtTestService;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 15:44
 */
@Slf4j
@CustomBeanReplace(value = "htTestServiceImpl")
//@Service
public class ExtHtTestServiceImpl implements HtTestService {
    @Override
    public void testMethod() {
        log.info("替换的测试类");
    }
}
