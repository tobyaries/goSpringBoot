class Service {

    private Dependency dependency;

    // 构造函数注入
    public Service(Dependency dependency) {
        this.dependency = dependency;
    }
    public void hello() {
        dependency.hello();
    }
}
