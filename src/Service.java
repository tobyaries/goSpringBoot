class Service {
    private Dependency dependency;

    // 添加无参构造函数
    public Service() {
    }
    public Service(Dependency dependency) {
        this.dependency = dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public void hello() {
        dependency.hello();
    }
}