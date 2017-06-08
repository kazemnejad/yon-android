package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 6/8/17.
 */

public class AuthResponse extends BasicResponse {
    private String token;

    public AuthResponse() {}

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
