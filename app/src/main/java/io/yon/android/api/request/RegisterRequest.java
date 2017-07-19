package io.yon.android.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 6/8/17.
 */

public class RegisterRequest {
    private String fname;
    private String lname;
    private String email;
    private String password;

    public RegisterRequest(String fname, String lname, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    @JsonProperty("fname")
    public String getFname() {
        return fname;
    }

    @JsonProperty("lname")
    public String getLname() {
        return lname;
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
