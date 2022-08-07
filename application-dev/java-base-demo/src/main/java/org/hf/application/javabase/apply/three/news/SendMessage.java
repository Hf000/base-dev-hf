package org.hf.application.javabase.apply.three.news;


public abstract class SendMessage {

    public static void main(String[] args) {
        Customer commonCustomer = new CommonCustomer();
        commonCustomer.setName("王五");
        commonCustomer.setPhone("13670001111");
        Customer vipCustomer= new VipCustomer();
        vipCustomer.setName("赵六");
        vipCustomer.setPhone("13670001155");

        send(vipCustomer);
        send(commonCustomer);
    }

    //给VIP客户发消息
    public static void send(Customer customer){
        System.out.println(customer.sefInfo());
    }
}
