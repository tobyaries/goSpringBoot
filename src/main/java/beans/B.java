package main.java.beans;

public class B {
    private A a;

    public B() {
        // 添加无参构造函数
    }

    public void setA(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}