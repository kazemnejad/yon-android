package io.yon.android.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 6/8/17.
 */

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
}
