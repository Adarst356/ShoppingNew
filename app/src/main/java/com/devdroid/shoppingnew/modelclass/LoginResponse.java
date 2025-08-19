package com.devdroid.shoppingnew.modelclass;

public class LoginResponse {
    private String access_token;
    private String refresh_token;


    public LoginResponse(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }


    public String getAccessToken() {
        return access_token;
    }


    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }


    public String getRefreshToken() {
        return refresh_token;
    }


    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
