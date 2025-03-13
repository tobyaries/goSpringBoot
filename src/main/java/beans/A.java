package beans;

/**
 * Bean A
 */
public class A {
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public void doSomething() {
        System.out.println("A is doing something, and using B: " + b.getClass().getSimpleName());
    }

    public void init() {
        System.out.println("A init method called.");
    }

    public void destroy() {
        System.out.println("A destroy method called.");
    }
}