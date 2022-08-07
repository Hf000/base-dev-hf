package org.hf.application.javabase.apply.jdk8.method;

public class Demo {

    public static void main(String[] args) {

        DemoServiceImpl demoServiceImpl = new DemoServiceImpl();

        System.out.println(demoServiceImpl.defaultMethod());

        System.out.println(DemoService.staticMethod());

        DemoService demoService = new DemoService() {
            @Override
            public String abstractMethod() {
                return null;
            }

            @Override
            public String defaultMethod() {
                return null;
            }
        };
    }
}
