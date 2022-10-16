package org.hf.application.javabase.thread.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConccuentMapTest {

    Map map = new ConcurrentHashMap();

    public void f1(){

        map.put("userid",123);

    }


    public static void main(String[] args) {



    }
}
