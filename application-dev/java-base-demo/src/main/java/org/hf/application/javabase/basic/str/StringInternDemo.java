package org.hf.application.javabase.basic.str;

/**
 * <p> 字符串比较
 * intern()方法, 将字符串对象的引用直接指向常量池
 * jdk1.6及之前, 字符串常量池在方法区
 * jdk1.7及之后, 字符串常量池在堆内存的方法区(和运行时常量池区分开了)
 * </p>
 */
public class StringInternDemo {

    public static void main(String[] args) {
        String a = "123";
        String b = "123";
        System.out.println("字符串比较");
        System.out.println(a==b);                               // true
        System.out.println(a.equals(b));                        // true
        String str1 = new String("123");
        String str2 = new String("123");
        System.out.println("字符串对象比较");
        System.out.println(str1 == str2);                       // false
        System.out.println(str1.equals(str2));                  // true
        System.out.println("字符串和字符串对象比较");
        System.out.println(str1.equals(a));                     // true
        System.out.println(str1 == a);                          // false
        System.out.println(str1.intern() == a);                 // true
        System.out.println(str1.intern().equals(a));            // true
        System.out.println(str1.intern() == str2.intern());     // true
        System.out.println(str1.intern().equals(str2.intern()));// true
    }

}
