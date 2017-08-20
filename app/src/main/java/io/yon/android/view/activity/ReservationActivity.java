package io.yon.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerController;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.date.SimpleDayPickerView;

import java.util.ArrayList;
import java.util.Arrays;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.adapter.MonthAdapter;
import io.yon.android.view.adapter.ReservationPagesAdapter;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationActivity extends Activity {

    private Restaurant mRestaurant;
    //    private final PersianCalendar mPersianCalendar = new PersianCalendar();
    private ArrayList<DatePickerDialog.OnDateChangedListener> mListeners = new ArrayList<>();
    private MonthAdapter adapter;
    private ViewPager viewPager;

    public static void start(Context context, Restaurant restaurant) {
        context.startActivity(new Intent(context, ReservationActivity.class)
                .putExtra("rest", restaurant)
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reservatiuon;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurant = new Restaurant();
        mRestaurant.setId(5);
        mRestaurant.setName(getString(R.string.app_name));
        mRestaurant.setRate(3.4f);
        mRestaurant.setPrice(4.9f);
//        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("rest"));

        setDisplayHomeAsUpEnabled(true);
        setTitle(mRestaurant.getName());

        initViews();
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(4);
//        list.add(5);
//
//        adapter = new MonthAdapter(this, list, new DatePickerController() {
//            @Override
//            public void onYearSelected(int year) {
//
//            }
//
//            @Override
//            public void onDayOfMonthSelected(int year, int month, int day) {
////                for(DatePickerDialog.OnDateChangedListener listener : mListeners) listener.onDateChanged();
//                com.mohamadamin.persianmaterialdatetimepicker.date.MonthAdapter.CalendarDay date = new com.mohamadamin.persianmaterialdatetimepicker.date.MonthAdapter.CalendarDay();
//                date.day = day;
//                date.month = month;
//                date.year = year;
//                adapter.setSelectedDay(date);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void registerOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
//                mListeners.add(listener);
//            }
//
//            @Override
//            public void unregisterOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
//
//            }
//
//            @Override
//            public com.mohamadamin.persianmaterialdatetimepicker.date.MonthAdapter.CalendarDay getSelectedDay() {
//                return new com.mohamadamin.persianmaterialdatetimepicker.date.MonthAdapter.CalendarDay();
//            }
//
//            @Override
//            public boolean isThemeDark() {
//                return false;
//            }
//
//            @Override
//            public com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar[] getHighlightedDays() {
//                return null;
//            }
//
//            @Override
//            public com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar[] getSelectableDays() {
//                return null;
//            }
//
//            @Override
//            public int getFirstDayOfWeek() {
//                return PersianCalendar.SATURDAY;
//            }
//
//            @Override
//            public int getMinYear() {
//                return 1396;
//            }
//
//            @Override
//            public int getMaxYear() {
//                return 1396;
//            }
//
//            @Override
//            public com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar getMinDate() {
//                return null;
//            }
//
//            @Override
//            public com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar getMaxDate() {
//                return null;
//            }
//
//            @Override
//            public void tryVibrate() {
//
//            }
//        });
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void findViews() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void initViews() {
        viewPager.setAdapter(new ReservationPagesAdapter(getSupportFragmentManager()));
    }
}
