package io.yon.android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.util.calendar.TimeZones;
import io.yon.android.view.adapter.MonthAdapter;
import io.yon.android.view.widget.date.CalendarDay;
import io.yon.android.view.widget.date.DatePickerController;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationDateFragment extends Fragment implements DatePickerController {
    private RecyclerView recyclerView;
    private MonthAdapter adapter;

    final private PersianCalendar today = new PersianCalendar(System.currentTimeMillis());


    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_date;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    @Override
    protected void findViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
    }

    private void initView() {
        today.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        adapter = new MonthAdapter(getContext(), Arrays.asList(getNextTwoMonth()), this);
        recyclerView.setAdapter(adapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void onYearSelected(int year) {

    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        CalendarDay selectedDay = new CalendarDay(year, month, day);
        adapter.setSelectedDay(selectedDay);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void registerOnDateChangedListener(OnDateChangedListener listener) {

    }

    @Override
    public void unregisterOnDateChangedListener(OnDateChangedListener listener) {

    }

    @Override
    public CalendarDay getSelectedDay() {
        return null;
    }

    @Override
    public boolean isThemeDark() {
        return false;
    }

    @Override
    public PersianCalendar[] getHighlightedDays() {
        return null;
    }

    @Override
    public PersianCalendar[] getSelectableDays() {
        return null;
    }

    @Override
    public int getFirstDayOfWeek() {
        return PersianCalendar.SATURDAY;
    }

    @Override
    public int getMinYear() {
        return today.getPersianYear();
    }

    @Override
    public int getMaxYear() {
        return today.getPersianYear();
    }

    @Override
    public PersianCalendar getMinDate() {
        return today;
    }

    @Override
    public PersianCalendar getMaxDate() {
        PersianCalendar nextMonth = new PersianCalendar(System.currentTimeMillis());
        nextMonth.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        nextMonth.addPersianDate(Calendar.MONTH, 2);

        return nextMonth;
    }

    @Override
    public void tryVibrate() {

    }

    private PersianCalendar[] getNextTwoMonth() {
        PersianCalendar nextMonth = new PersianCalendar(System.currentTimeMillis());
        nextMonth.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        nextMonth.addPersianDate(Calendar.MONTH, 1);

        return new PersianCalendar[]{
                today,
                nextMonth
        };
    }
}
