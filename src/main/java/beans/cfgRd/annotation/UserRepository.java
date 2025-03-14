package beans.cfgRd.annotation;

import core.cfgRd.annotation.Component;

@Component
public class UserRepository {
    public void save() {
        System.out.println("Saving user data...");
    }
}