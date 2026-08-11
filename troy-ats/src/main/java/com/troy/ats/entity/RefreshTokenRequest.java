package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("grant_type")
    private String grantType;

    public RefreshTokenRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}