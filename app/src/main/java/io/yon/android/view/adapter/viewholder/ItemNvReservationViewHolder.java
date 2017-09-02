package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.model.Reservation;
import io.yon.android.util.RxBus;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;

/**
 * Created by amirhosein on 9/2/2017 AD.
 */

public class ItemNvReservationViewHolder extends ViewHolder<Reservation> implements View.OnClickListener {

    private TextView monthDay, month, weekDay, restaurantName, guestCount, time;
    private Button btnCancelReservation;

    public static Factory<ItemNvReservationViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemNvReservationViewHolder(
                inflater.inflate(R.layout.item_navigation_menu_reservation, parent, false),
                context,
                bus
        );
    }

    public ItemNvReservationViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        month = (TextView) findViewById(R.id.month);
        monthDay = (TextView) findViewById(R.id.day_num);
        weekDay = (TextView) findViewById(R.id.weekday);
        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        guestCount = (TextView) findViewById(R.id.label_guest_count);
        time = (TextView) findViewById(R.id.time);
        btnCancelReservation = (Button) findViewById(R.id.btn_cancel_reservation);
    }

    @Override
    protected void initViews() {
        getItemView().setOnClickListener(this);

//        btnCancelReservation.setOnClickListener(v -> getBus().send("cancel_reservation"));
    }

    @Override
    public void bindContent(Reservation r) {
        PersianCalendar calendar = new PersianCalendar(r.getDatetime() * 1000L);
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));

        month.setText(calendar.getPersianMonthName());
        monthDay.setText(LanguageUtils.getPersianNumbers(String.valueOf(calendar.getPersianDay())));
        weekDay.setText(calendar.getPersianWeekDayName());

        time.setText(LanguageUtils.getPersianNumbers(calendar.getPersianTimeCompact()));
        guestCount.setText(LanguageUtils.getPersianNumbers(getContext().getString(R.string.guest_count_label, r.getGuestCount())));
        restaurantName.setText(r.getRestaurant().getName());
    }

    @Override
    public void onClick(View v) {
        try {
            getBus().send(getParentAdapter().getData().get(getAdapterPosition()));
        } catch (Exception ignored) {

        }
    }
}
