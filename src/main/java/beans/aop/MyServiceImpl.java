package beans.aop;

public class MyServiceImpl implements MyService {
    public void doSomething() {
        System.out.println("MyService.doSomething() is called.");
    }

    public String doSomethingWithResult() {
        System.out.println("MyService.doSomethingWithResult() is called.");
        return "Result";
    }

    public void doSomethingWithException() {
        System.out.println("MyService.doSomethingWithException() is called.");
        throw new RuntimeException("Test Exception");
    }
}