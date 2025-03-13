package beans.ioc;

public class Service {
    private Dependency dependency;

    // Add no-argument constructor
    public Service() {
    }

    public Service(Dependency dependency) {

        this.dependency = dependency;
    }

    public void setDependency(Dependency dependency) {

        this.dependency = dependency;
    }

    public void hello()
    {
        dependency.hello();
    }
}