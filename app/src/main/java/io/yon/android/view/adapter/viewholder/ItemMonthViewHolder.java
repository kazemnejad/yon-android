package io.yon.android.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.adapter.MonthAdapter;
import io.yon.android.view.widget.date.CalendarDay;
import io.yon.android.view.widget.date.DatePickerController;
import io.yon.android.view.widget.date.MonthView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ItemMonthViewHolder extends RecyclerView.ViewHolder implements MonthView.OnDayClickListener {

    private MonthView monthView;
    private DatePickerController controller;
    private CalendarDay mSelectedDay;

    protected static int WEEK_7_OVERHANG_HEIGHT = 7;
    protected static final int MONTHS_IN_YEAR = 12;

    protected int year;

    public ItemMonthViewHolder(View itemView, DatePickerController controller) {
        super(itemView);

        this.monthView = (MonthView) itemView;
        this.controller = controller;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        monthView.setLayoutParams(params);
        monthView.setClickable(true);
        monthView.setOnDayClickListener(this);

        PersianCalendar calendar = new PersianCalendar(System.currentTimeMillis());
        year = calendar.getPersianYear();
        mSelectedDay = new CalendarDay(System.currentTimeMillis());
    }

    public void bindContent(int month, CalendarDay selectedDay) {
        HashMap<String, Integer> drawingParams = new HashMap<>();
        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay != null ? selectedDay.day : -1);
        drawingParams.put(MonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(MonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(MonthView.VIEW_PARAMS_WEEK_START, controller.getFirstDayOfWeek());

        monthView.setMonthParams(drawingParams);
        monthView.invalidate();
    }

    @Override
    public void onDayClick(MonthView view, CalendarDay day) {
        if (day != null) {
            onDayTapped(day);
        }
    }

    protected void onDayTapped(CalendarDay day) {
        controller.tryVibrate();
        controller.onDayOfMonthSelected(day.year, day.month, day.day);
    }

    private boolean isSelectedDayInMonth(int year, int month) {
        return mSelectedDay.year == year && mSelectedDay.month == month;
    }
}
