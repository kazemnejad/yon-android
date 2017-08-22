package io.yon.android.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

import io.yon.android.util.ViewUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.widget.date.CalendarDay;
import io.yon.android.view.widget.date.DatePickerController;
import io.yon.android.view.widget.date.MonthView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ItemMonthViewHolder extends RecyclerView.ViewHolder implements MonthView.OnDayClickListener {

    private MonthView monthView;
    private DatePickerController controller;

    public ItemMonthViewHolder(View itemView, DatePickerController controller) {
        super(itemView);

        this.monthView = (MonthView) itemView;
        this.controller = controller;

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewUtils.px(itemView.getContext(), 300),
                RecyclerView.LayoutParams.MATCH_PARENT
        );
        params.setMargins(
                ViewUtils.px(itemView.getContext(), 15),
                0,
                ViewUtils.px(itemView.getContext(), 10),
                0
        );

        monthView.setLayoutParams(params);
        monthView.setClickable(true);
        monthView.setOnDayClickListener(this);
    }

    public void bindContent(PersianCalendar month, CalendarDay selectedDay) {
        HashMap<String, Integer> drawingParams = new HashMap<>();
        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay != null ? selectedDay.day : -1);
        drawingParams.put(MonthView.VIEW_PARAMS_YEAR, month.getPersianYear());
        drawingParams.put(MonthView.VIEW_PARAMS_MONTH, month.getPersianMonth());
        drawingParams.put(MonthView.VIEW_PARAMS_WEEK_START, controller.getFirstDayOfWeek());

        monthView.setMonthParams(drawingParams);
        monthView.invalidate();
    }

    @Override
    public void onDayClick(MonthView view, CalendarDay day) {
        if (day != null)
            onDayTapped(day);
    }

    protected void onDayTapped(CalendarDay day) {
        controller.tryVibrate();
        controller.onDayOfMonthSelected(day.year, day.month, day.day);
    }
}
