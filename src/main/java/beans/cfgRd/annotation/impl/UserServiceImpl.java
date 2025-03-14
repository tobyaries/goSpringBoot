package beans.cfgRd.annotation.impl;

import beans.cfgRd.annotation.UserRepository;
import beans.cfgRd.annotation.interfaces.UserService;
import core.cfgRd.annotation.Autowired;
import core.cfgRd.annotation.Component;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public void process() {
        System.out.println("Processing user service...");
        userRepository.save();
    }
}