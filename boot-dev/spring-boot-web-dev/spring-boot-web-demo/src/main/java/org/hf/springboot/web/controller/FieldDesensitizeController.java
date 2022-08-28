package org.hf.springboot.web.controller;

import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.springboot.web.pojo.vo.DesensitizeVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 响应字段脱敏测试 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/28 19:19
 */
@RestController
@RequestMapping("desensitize")
public class FieldDesensitizeController {

    @GetMapping("field")
    public ResponseVO<DesensitizeVO> test() {
        DesensitizeVO desensitizeVO = new DesensitizeVO();
        desensitizeVO.setId(100001L);
        desensitizeVO.setName("赵五六");
        desensitizeVO.setDescription("启飞科技有限公司");
        desensitizeVO.setPhone("17683743094");
        desensitizeVO.setFixPhone("07165845789");
        desensitizeVO.setEmail("17683743094@163.com");
        desensitizeVO.setBankCard("634893475924759234");
        desensitizeVO.setDress("广东省深圳市盐田区");
        desensitizeVO.setIdCard("421023199408140459");
        desensitizeVO.setOfficerNumber("075534972");
        desensitizeVO.setPassport("E134235322");
        desensitizeVO.setLicence("472904789729435");
        desensitizeVO.setReserveFund("352454325234");
        desensitizeVO.setSocialSecurity("345723495234333");
        desensitizeVO.setResidencePermit("12454642333");
        desensitizeVO.setMac("00-OD-5E-FD-D1-B3");
        desensitizeVO.setIp("192.168.15.99");
        desensitizeVO.setIemi("1457022784564650");
        desensitizeVO.setIdfa("1B484567-CC78-3561-84K5-DDE6F4546785");
        desensitizeVO.setCarNo("粤BJ4578");
        desensitizeVO.setCarIdentificationNumber("HGK45CH4727894214");
        return ResponseUtil.success(desensitizeVO);
    }
}