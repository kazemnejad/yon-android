package io.yon.android.model;

import java.util.Calendar;
import java.util.TimeZone;

import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;

/**
 * Created by amirhosein on 8/23/2017 AD.
 */

public class OpenTimeSlot extends Model {
    String label;
    PersianCalendar datetime;
    int hour;
    boolean enable;

    public OpenTimeSlot() {}

    public OpenTimeSlot(String label, PersianCalendar datetime) {
        this.label = label;
        this.datetime = datetime;
    }

    public OpenTimeSlot(int baseUnixTimestamp, int hourInSeconds) {
        hour = hourInSeconds;

        datetime = new PersianCalendar();
        datetime.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        datetime.setTimeInMillis((baseUnixTimestamp + hourInSeconds) * 1000L);

        label = datetime.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + String.valueOf(datetime.get(Calendar.HOUR_OF_DAY)) : String.valueOf(datetime.get(Calendar.HOUR_OF_DAY));
        label += ":";
        label += datetime.get(Calendar.MINUTE) < 10 ? "0" + String.valueOf(datetime.get(Calendar.MINUTE)) : String.valueOf(datetime.get(Calendar.MINUTE));

        label = LanguageUtils.getPersianNumbers(label);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PersianCalendar getDatetime() {
        return datetime;
    }

    public void setDatetime(PersianCalendar datetime) {
        this.datetime = datetime;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
