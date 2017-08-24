package io.yon.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amirhosein on 8/23/2017 AD.
 */

public class OpenTimeSlotSection extends Model {
    String label;
    List<OpenTimeSlot> openTimeSlots = new ArrayList<>();
    int startHour;
    int endHour;

    public OpenTimeSlotSection() {}

    public OpenTimeSlotSection(String label, int startHour, int endHour) {
        this.label = label;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<OpenTimeSlot> getOpenTimeSlots() {
        return openTimeSlots;
    }

    public void setOpenTimeSlots(List<OpenTimeSlot> openTimeSlots) {
        this.openTimeSlots = openTimeSlots;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
