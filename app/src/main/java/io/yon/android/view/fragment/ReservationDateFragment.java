package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.presenter.RestaurantPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.activity.ReservationActivity;
import io.yon.android.view.activity.ReservationBuilderController;
import io.yon.android.view.adapter.MonthAdapter;
import io.yon.android.view.widget.date.CalendarDay;
import io.yon.android.view.widget.date.DatePickerController;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationDateFragment extends Fragment implements DatePickerController {

    private RecyclerView recyclerView;
    private MonthAdapter adapter;

    private TextView firstSelectedDay;
    private TextView secondSelectedDay;

    private Button btnSelectTime;

    private int translationDistance;

    private String lastSelectedDayLabel;

    final private PersianCalendar today = new PersianCalendar(System.currentTimeMillis());

    private ReservationBuilderController mController;
    private ReservationPresenter presenter;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_date;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mController = getParentActivity();
        presenter = ViewModelProviders.of(getActivity()).get(ReservationPresenter.class);

        initView();
    }

    @Override
    protected void findViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        firstSelectedDay = (TextView) v.findViewById(R.id.first);
        secondSelectedDay = (TextView) v.findViewById(R.id.second);
        btnSelectTime = (Button) v.findViewById(R.id.btn_go_select_time);
    }

    private void initView() {
        today.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        adapter = new MonthAdapter(getContext(), Arrays.asList(getNextTwoMonth()), this);
        recyclerView.setAdapter(adapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerView);

        PersianCalendar selectedDay = presenter.getSelectedDateTime();
        if (selectedDay == null) {
            presenter.setSelectedDateTime(today);
            selectedDay = presenter.getSelectedDateTime();
        }

        adapter.setSelectedDay(new CalendarDay(selectedDay.getPersianYear(), selectedDay.getPersianMonth(), selectedDay.getPersianDay()));
        lastSelectedDayLabel = LanguageUtils.getPersianNumbers(selectedDay.getPersianLongDate());
        secondSelectedDay.setText(lastSelectedDayLabel);

        translationDistance = ViewUtils.px(getContext(), 50);

        btnSelectTime.setOnClickListener(v -> mController.next());
    }

    @Override
    public void onYearSelected(int year) {

    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        CalendarDay selectedDay = new CalendarDay(year, month, day);
        adapter.setSelectedDay(selectedDay);
        adapter.notifyDataSetChanged();

        PersianCalendar selectedDayCalender = new PersianCalendar();
        selectedDayCalender.setPersianDate(year, month, day);
        selectDay(selectedDayCalender);
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
        nextMonth.add(PersianCalendar.MONTH, 2);

        return nextMonth;
    }

    @Override
    public void tryVibrate() {

    }

    private PersianCalendar[] getNextTwoMonth() {
        PersianCalendar nextMonth = new PersianCalendar(System.currentTimeMillis());
        nextMonth.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        nextMonth.add(PersianCalendar.MONTH, 1);

        return new PersianCalendar[]{
                today,
                nextMonth
        };
    }

    public void selectDay(PersianCalendar day) {
        presenter.setSelectedDateTime(day);

        animateSelectedDayLabel(LanguageUtils.getPersianNumbers(day.getPersianLongDate()));
        lastSelectedDayLabel = LanguageUtils.getPersianNumbers(day.getPersianLongDate());
    }

    public void animateSelectedDayLabel(String selectedDayLabel) {
        firstSelectedDay.setTranslationY(-translationDistance);
        firstSelectedDay.setAlpha(0);
        firstSelectedDay.setText(selectedDayLabel);

        secondSelectedDay.setTranslationY(0);
        secondSelectedDay.setAlpha(1f);
        secondSelectedDay.setText(lastSelectedDayLabel);

        firstSelectedDay.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        secondSelectedDay.animate()
                .alpha(0f)
                .translationY(translationDistance)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
