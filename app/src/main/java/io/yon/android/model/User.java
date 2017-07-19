package io.yon.android.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.yon.android.Config;

/**
 * Created by amirhosein on 7/18/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;

    private String email;
    private String firstName;
    private String lastName;

    private int cash = 0;
    private int point = 0;

    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("fname")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lname")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCash() {
        return cash;
    }

    @JsonProperty("cash")
    public void setCash(int cash) {
        this.cash = cash;
    }


    public int getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(int point) {
        this.point = point;
    }

    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void save(Context context) {
        if (context == null)
            return;

        Config.getUser(context).edit()
                .putString(Config.Field.FirstName, firstName)
                .putString(Config.Field.LastName, lastName)
                .putString(Config.Field.Email, email)
                .putString(Config.Field.Avatar, avatar)
                .apply();
    }
}
