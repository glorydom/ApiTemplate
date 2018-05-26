package com.zheng.upms.server.controller.dto;

public class UserLogin {
    private String username;
    private String password;
    private String readme;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
