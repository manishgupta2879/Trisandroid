package com.pcs.tim.myapplication;
public class RequestTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String expiryDate;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public RequestTokenResponse(String accessToken, String refreshToken, String expiryDate ){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }
}
