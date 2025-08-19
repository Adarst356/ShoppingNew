package com.devdroid.shoppingnew.modelclass;

public class UserResponse {
    private int id;
    private String name;
    private String password;
    private String email;
    private String avatar;



    public UserResponse(String avatar, String email, int id, String name, String password) {
        this.avatar = avatar;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
