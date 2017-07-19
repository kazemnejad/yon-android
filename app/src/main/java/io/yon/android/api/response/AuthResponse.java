package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.yon.android.model.User;

/**
 * Created by amirhosein on 6/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse extends BasicResponse {
    private String token;
    private User user;

    public AuthResponse() {}

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }
}
