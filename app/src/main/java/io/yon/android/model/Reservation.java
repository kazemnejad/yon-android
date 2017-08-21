package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation extends Model {
    Table table;
    long datetime = -1;
    int guestCount = 0;

    public Reservation() {}

    public Reservation(Table table, long datetime, int guestCount) {
        this.table = table;
        this.datetime = datetime;
        this.guestCount = guestCount;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }
}
