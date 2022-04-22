package mytest.misc;

public class Test {

    public static void main(String[] args) {
        A a = new A();
        new Thread(() -> a.foo(10)).start();
        new Thread(() -> a.bar(10)).start();
    }
}
