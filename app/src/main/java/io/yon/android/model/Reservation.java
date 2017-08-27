package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation extends Model {
    int id;
    Table table;
    long datetime = -1;
    int guestCount = 0;
    String note;

    public Reservation() {}

    public Reservation(Table table, long datetime, int guestCount) {
        this.table = table;
        this.datetime = datetime;
        this.guestCount = guestCount;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @JsonProperty("datetime")
    public long getDatetime() {
        return datetime;
    }

    @JsonProperty("datetime")
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    @JsonProperty("guest_count")
    public int getGuestCount() {
        return guestCount;
    }

    @JsonProperty("guest_count")
    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    @JsonProperty("description")
    public String getNote() {
        return note;
    }

    @JsonProperty("description")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("table_id")
    public String getTableId() {
        return table != null ? table.getId() : null;
    }

    @JsonProperty("table_id")
    public void setTableId(String tableId) {
        this.table = new Table(tableId);
    }
}
