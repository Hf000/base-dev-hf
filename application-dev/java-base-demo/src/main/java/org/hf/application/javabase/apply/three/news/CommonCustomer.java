package org.hf.application.javabase.apply.three.news;


public class CommonCustomer extends Customer{

    @Override
    public String sefInfo() {
        return "给普通客户发消息，客户名字：" + super.getName() + ",客户手机号："+super.getPhone();
    }
}
