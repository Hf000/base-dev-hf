package org.hf.application.javabase.apply.three.old;


public class CommonCustomer {

    private String name;

    private String phone;

    public CommonCustomer() {
    }

    public CommonCustomer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String sefInfo() {
        return "给普通客户发消息，客户名字：" + name + ",客户手机号："+phone;
    }
}
