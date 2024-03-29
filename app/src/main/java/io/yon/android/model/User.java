package io.yon.android.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import io.yon.android.Config;

/**
 * Created by amirhosein on 7/18/17.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    int id;

    String email;
    String firstName;
    String lastName;

    int cash = 0;
    int point = 0;

    String avatar;
    String phoneNumber = "09120242570";

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void save(Context context) {
        if (context == null)
            return;

        Config.getUser(context).edit()
                .putString(Config.Field.FirstName, firstName)
                .putString(Config.Field.LastName, lastName)
                .putString(Config.Field.Email, email)
                .putString(Config.Field.Avatar, avatar)
                .putInt(Config.Field.UserPoint, point)
                .apply();
    }
}
