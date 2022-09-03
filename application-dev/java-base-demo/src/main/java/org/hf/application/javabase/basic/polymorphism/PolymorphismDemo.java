package org.hf.application.javabase.basic.polymorphism;

/**
 * <p> 多态: 编译看左边, 运行看右边 </p>
 * java三大特性:封装、继承、多态
 * 多态的三个必要条件: 1.继承, 2.重写, 3.父类引用指向子类对象
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:22
 */
public class PolymorphismDemo {

    public static void main(String[] args) {
        // 以Cat对象调用show方法
        show(new Cat());
        // 以Dog对象调用show方法
        show(new Dog());
        // 向上转型
        Animal animal = new Cat();
        animal.eat();
        // 向下转型
        Cat cat = (Cat)animal;
        cat.work();
    }

    public static void show(Animal animal) {
        animal.eat();
        if (animal instanceof Cat) {
            Cat cat = (Cat)animal;
            cat.work();
        } else if (animal instanceof Dog) {
            Dog dog = (Dog)animal;
            dog.work();
        }
    }

}

abstract class Animal {
    abstract void eat();
}

class Cat extends Animal {

    @Override
    void eat() {
        System.out.println("吃鱼");
    }

    public void work() {
        System.out.println("抓老鼠");
    }
}

class Dog extends Animal {

    @Override
    void eat() {
        System.out.println("吃肉");
    }

    public void work() {
        System.out.println("看家");
    }
}