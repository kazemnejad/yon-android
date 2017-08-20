package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.adapter.viewholder.ItemMonthViewHolder;
import io.yon.android.view.widget.date.CalendarDay;
import io.yon.android.view.widget.date.DatePickerController;
import io.yon.android.view.widget.date.SimpleMonthView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class MonthAdapter extends RecyclerView.Adapter<ItemMonthViewHolder> {

    private final Context context;
    private final List<PersianCalendar> data;
    private final DatePickerController controller;

    private CalendarDay mSelectedDay;

    public MonthAdapter(Context context, List<PersianCalendar> data, DatePickerController controller) {
        this.context = context;
        this.data = data;
        this.controller = controller;
        mSelectedDay = new CalendarDay(System.currentTimeMillis());
    }

    @Override
    public ItemMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemMonthViewHolder(new SimpleMonthView(context, null, controller), controller);
    }

    @Override
    public void onBindViewHolder(ItemMonthViewHolder holder, int position) {
        CalendarDay day = null;
        if (isSelectedDayInMonth(data.get(position)))
            day = mSelectedDay;

        holder.bindContent(data.get(position), day);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    private boolean isSelectedDayInMonth(PersianCalendar day) {
        return mSelectedDay.year == day.getPersianYear() && mSelectedDay.month == day.getPersianMonth();
    }

    public void setSelectedDay(CalendarDay mSelectedDay) {
        this.mSelectedDay = mSelectedDay;
    }
}
