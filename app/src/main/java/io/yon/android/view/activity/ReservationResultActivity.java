package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.parceler.Parcels;

import java.util.List;
import java.util.TimeZone;

import io.yon.android.R;
import io.yon.android.model.Map;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.Reservation;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.model.User;
import io.yon.android.presenter.ReservationPresenter;
import io.yon.android.util.Auth;
import io.yon.android.util.ViewUtils;
import io.yon.android.util.calendar.LanguageUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;
import io.yon.android.view.dialog.MapViewDialog;
import io.yon.android.view.widget.EmailInputView;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/24/2017 AD.
 */

public class ReservationResultActivity extends Activity {

    private View successfulReservationLabel, successfulReservationIcon;
    private ImageView restaurantIcon, userAvatar;
    private TextView userName, phoneNumber, month, monthDay, weekDay, time, labelGuestCount, btnShowSelectedTable, note;

    private EmailInputView emailInputView;
    private EditText etInvitationText;

    private Button btnSend;
    private ImageButton btnDone;

    private Reservation mReservation;
    private Restaurant mRestaurant;

    private ReservationPresenter presenter;

    public static void start(Context context, Restaurant restaurant, Reservation reservation) {
        context.startActivity(
                new Intent(context, ReservationResultActivity.class)
                        .putExtra("rest", Parcels.wrap(restaurant))
                        .putExtra("res", Parcels.wrap(reservation))
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reservation_result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("rest"));
        mReservation = Parcels.unwrap(getIntent().getParcelableExtra("res"));

        presenter = ViewModelProviders.of(this).get(ReservationPresenter.class);

        initView();
    }

    @Override
    protected void findViews() {
        successfulReservationLabel = findViewById(R.id.label_successful_reservation);
        successfulReservationIcon = findViewById(R.id.ic_done);
        restaurantIcon = (ImageView) findViewById(R.id.restaurant_icon);
        userAvatar = (ImageView) findViewById(R.id.user_avatar);
        userName = (TextView) findViewById(R.id.user_name);
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        month = (TextView) findViewById(R.id.month);
        monthDay = (TextView) findViewById(R.id.day_num);
        weekDay = (TextView) findViewById(R.id.weekday);
        time = (TextView) findViewById(R.id.time);
        labelGuestCount = (TextView) findViewById(R.id.label_guest_count);
        btnShowSelectedTable = (TextView) findViewById(R.id.selected_table);
        note = (TextView) findViewById(R.id.note);
        btnDone = (ImageButton) findViewById(R.id.toolbar_icon_right);
        btnSend = (Button) findViewById(R.id.btn_send);
        emailInputView = (EmailInputView) findViewById(R.id.email_input);
        etInvitationText = (EditText) findViewById(R.id.et_invitation_text);
    }

    private void initView() {
        btnDone.setOnClickListener(v -> sendInvitationAndFinish());
        btnSend.setOnClickListener(v -> sendInvitationAndFinish());

        animateSuccessfulReservation();

        PersianCalendar datetime = new PersianCalendar(mReservation.getDatetime() * 1000L);
        datetime.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        Table selectedTable = mReservation.getTable();
        int guestCount = mReservation.getGuestCount();
        User user = Auth.user(getApplicationContext());

        month.setText(datetime.getPersianMonthName());
        monthDay.setText(LanguageUtils.getPersianNumbers(String.valueOf(datetime.getPersianDay())));
        weekDay.setText(datetime.getPersianWeekDayName());

//        time.setText(LanguageUtils.getPersianNumbers(datetime.getPersianTime()));
        OpenTimeSlot timeSlot = new OpenTimeSlot();
        timeSlot.setDatetime(datetime);
        time.setText(LanguageUtils.getPersianNumbers(timeSlot.getTimeLabel()));
        labelGuestCount.setText(
                getString(R.string.table_for) + " " + LanguageUtils.getPersianNumbers(String.valueOf(guestCount)) + " " + getString(R.string.person)
        );

        userName.setText(user.getFirstName() + " " + user.getLastName());
        phoneNumber.setText(LanguageUtils.getPersianNumbers(String.valueOf(user.getPhoneNumber())));

        if (selectedTable != null) {
            selectedTable.setName("B3");
            btnShowSelectedTable.setClickable(true);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            btnShowSelectedTable.setText(getString(R.string.table) + " " + selectedTable.getName() + " - " + getString(R.string.view) + "");
            btnShowSelectedTable.setOnClickListener(v ->
                    new MapViewDialog(this, findMapByTable(selectedTable, mRestaurant.getMaps()), selectedTable)
                            .show());
        } else {
            btnShowSelectedTable.setClickable(false);
            btnShowSelectedTable.setTextColor(ContextCompat.getColor(this, R.color.black_54));
            btnShowSelectedTable.setText(R.string.no_selected_table);
        }


        ColorGenerator generator = ColorGenerator.MATERIAL;
        Drawable placeHolder = TextDrawable.builder()
                .buildRound(
                        user.getFirstName().length() > 0 ? String.valueOf(user.getFirstName().charAt(0)) : "",
                        generator.getColor(user.getEmail())
                );

        GlideApp.with(this)
                .asBitmap()
                .load(user.getAvatar())
                .placeholder(placeHolder)
                .circleCrop()
                .into(userAvatar);

        GlideApp.with(this)
                .asBitmap()
                .load("http://162.243.174.32/restaurant_avatars/1166.jpeg")
                .placeholder(R.color.solidPlaceHolder)
                .centerCrop()
                .transform(new RoundedCornersTransformation(this, 30, 0))
                .transition(withCrossFade())
                .into(restaurantIcon);

        note.setText(mReservation.getNote());
    }

    private void animateSuccessfulReservation() {
        successfulReservationLabel.setTranslationX(ViewUtils.px(this, 40));
        successfulReservationLabel.setAlpha(0f);

//        successfulReservationIcon.setTranslationY(ViewUtils.dp(this, 100));
//        successfulReservationIcon.setAlpha(0f);

        successfulReservationIcon.setScaleX(0.1f);
        successfulReservationIcon.setScaleY(0.1f);
        successfulReservationIcon.setAlpha(0.1f);

        successfulReservationLabel.animate()
                .translationX(0)
                .alpha(1f)
                .setStartDelay(200)
                .setDuration(500)
                .setInterpolator(new FastOutSlowInInterpolator());

        successfulReservationIcon.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1f)
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator());
    }

    private Map findMapByTable(Table target, List<Map> maps) {
        for (Map map : maps)
            for (Table table : map.getTables())
                if (target.getId().equals(table.getId()))
                    return map;


        return maps.size() > 0 ? maps.get(0) : null;
    }

    private void sendInvitationAndFinish() {
        String text = etInvitationText.getText().toString();
        List<String> emails = emailInputView.getObjects();
        if (text.length() == 0 || emails.size() == 0) {
            finish();
            return;
        }

        presenter.saveInvitation(mReservation, emails, text);
        finish();
    }
}
