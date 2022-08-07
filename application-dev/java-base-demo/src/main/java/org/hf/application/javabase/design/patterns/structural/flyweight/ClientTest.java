package org.hf.application.javabase.design.patterns.structural.flyweight;


public class ClientTest {

    public static void main(String[] args) {
        int extrinsic = 22;

        Flyweight flyweightX = FlyweightFactory.getFlyweight("X");
        flyweightX.operate(extrinsic);
        flyweightX.setIntrinsic("X-Noo1");
        System.out.println("Intrinsic:"+flyweightX.getIntrinsic());

        Flyweight flyweightY = FlyweightFactory.getFlyweight("Y");
        flyweightY.operate(extrinsic);

        Flyweight flyweightZ = FlyweightFactory.getFlyweight("Z");
        flyweightZ.operate(extrinsic);

        Flyweight flyweightReX = FlyweightFactory.getFlyweight("X");
        flyweightReX.operate(extrinsic);
        System.out.println("Intrinsic:"+flyweightReX.getIntrinsic());

        Flyweight unsharedFlyweight = new UnsharedConcreteFlyweight("X");
        unsharedFlyweight.operate(extrinsic);
    }
}
