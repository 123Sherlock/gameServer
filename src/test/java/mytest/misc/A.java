package mytest.misc;

public class A {

    private Integer id;

    public Boolean state;

    private String name;

    public void foo(int n) {
        for (int i = 0; i < n; i++) {
//            synchronized (this) {
//                notify();
                System.out.print("foo");
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void bar(int n) {
        for (int i = 0; i < n; i++) {
//            synchronized (this) {
//                notify();
                System.out.println("bar");
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public A() {
    }

    public A(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
