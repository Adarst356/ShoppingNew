package com.devdroid.shoppingnew.modelclass;

public class UserRequest {
    private  String name;
    private String email;
    private String password;
    private String avatar = "";

    public UserRequest(String avatar, String email, String name, String password) {
        this.avatar = avatar;
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
