package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.TimeZone;

import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;

/**
 * Created by amirhosein on 8/14/2017 AD.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReview extends Model {
    String id;
    String userName;
    String text;
    float rate;
    long datetime = -1;
    String datetimeStr;
    User user;

    public UserReview() {}

    public UserReview(String userName, String text, float rate, long datetime) {
        this.userName = userName;
        this.text = text;
        this.rate = rate;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public float getRate() {
        return rate;
    }

    public float getRtlRate() {
        return 5f - rate;
    }

    @JsonProperty("rate")
    public void setRate(float rate) {
        this.rate = rate;
    }

    public long getDatetime() {
        return datetime;
    }

    @JsonProperty("datetime")
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDatetimeStr() {
        if (datetimeStr == null) {
            if (datetime == -1) {
                datetimeStr = "";
                return datetimeStr;
            }

            PersianCalendar persianCalendar = new PersianCalendar(datetime * 1000L);
            persianCalendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
//            datetimeStr = persianCalendar.getPersianLongDate();
            datetimeStr = LanguageUtils.getPersianNumbers(persianCalendar.getPersianShortDate());
        }

        return datetimeStr;
    }
}
