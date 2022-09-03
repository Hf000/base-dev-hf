package org.hf.application.javabase.basic.generics;

/**
 * <p> 泛型demo </p>
 * <? extends T>: 是指"上界通配符",表示可以接收T这个类型的所有子类类型
 * <? super T>: 是指"下界通配符",表示可以接收T这个类型的所有父类类型
 * E - Element,常用在java.Collection里, 如:List,Iterator,Set
 * K,V - key,value,表示Map的键值对
 * N - Number,数字
 * T - Type,类型,如String,Integer等对象类型
 * ? - 通用泛型
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 15:27
 */
public class GenericsDemo {

    public static void main(String[] args) {
        // 上界通配符
        Generics<? extends Animal> generics = new Generics<Cat>();
        // 下界通配符
        Generics<? super Animal> gen = new Generics<Object>();
    }

}

interface GenericsInterface<T> {}

/**
 * 如果接口声明了泛型,那么实现类也必须声明泛型,否则接口只能声明具体的泛型类型
 * @param <T>
 */
class Generics<T> implements GenericsInterface<T> {
    /**
     * 这里的T同属于一个类型,根据入参来决定,对于声明了泛型的类的非静态方法不需要指定为泛型方法
     * @param t 入参
     * @return T 返回泛型
     */
    public T method(T t) {
        return t;
    }

    /**
     * 这里的T同属于一个类型,根据入参来决定,静态方法需要声明为泛型方法,因为静态方法不需要实例化调用,编译时无法推断出类型
     * @param t 入参
     * @param <T> 泛型方法的声明类型
     * @return T 返回泛型
     */
    public static <T> T staticMethod(T t) {
        return t;
    }
}

abstract class Animal{

}

class Cat extends Animal{

}
