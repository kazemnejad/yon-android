package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    int id;
    String userName;
    String text;
    float rate;
    long datetime = -1;
    String datetimeStr;

    public UserReview() {}

    public UserReview(String userName, String text, float rate, long datetime) {
        this.userName = userName;
        this.text = text;
        this.rate = rate;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getRate() {
        return rate;
    }

    public float getRtlRate() {
        return 5f - rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
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
