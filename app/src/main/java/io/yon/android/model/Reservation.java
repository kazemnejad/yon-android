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
    int currentStep = Step.DateSelect;

    public Reservation() {}

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

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public static class Step {
        public static final int DateSelect = 367;
        public static final int TimeSelect = 932;
        public static final int TableSelect = 213;
        public static final int GuestCount = 239;
        public static final int Final = 328;
    }
}
