package main.java.beans.users;

public class UserRegister implements Register {
    @Override
    public String registerUser(String username, String password) {
        System.out.println("Registering user: " + username);
        // 用户注册逻辑
        return "Result";
    }
}
