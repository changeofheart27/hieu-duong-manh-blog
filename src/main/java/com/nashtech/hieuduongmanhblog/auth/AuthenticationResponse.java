package com.nashtech.hieuduongmanhblog.auth;

public class AuthenticationResponse {
    private String jwtToken;

    public AuthenticationResponse() {

    }

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
