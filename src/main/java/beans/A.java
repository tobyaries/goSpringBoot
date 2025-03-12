package main.java.beans;

public class A {
    private B b;

    public A() {
        // 添加无参构造函数
    }

    public void setB(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }
}