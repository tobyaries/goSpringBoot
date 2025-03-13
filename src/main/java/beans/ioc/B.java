package beans.ioc;

public class B {
    private A a;

    public B() {
        // Add no-argument constructor
    }

    public void setA(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}