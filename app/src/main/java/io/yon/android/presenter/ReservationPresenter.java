package io.yon.android.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.widget.Toast;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.OpenTimeSlotSection;
import io.yon.android.model.Reservation;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
import io.yon.android.repository.Lce;
import io.yon.android.repository.ReservationRepository;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationPresenter extends Presenter implements ReservationContract.Presenter {

    private Restaurant restaurant;
    private PersianCalendar selectedDateTime;
    private int currentStep = Step.DateSelect;
    private int guestCount = -1;
    private int lastGuestCountAdapterPosition = -1;
    private Table selectedTable = null;
    private String noteToRestaurant;
    private OpenTimeSlot selectedTimeSlot;
    private boolean containError = false;

    private long forbiddenTableLastRequestTime;
    private long openHoursLastRequestTime;

    private ReservationContract.TimeView timeView;
    private ReservationContract.TableView tableView;
    private ReservationContract.ConfirmView confirmView;

    private Observable<Lce<List<OpenTimeSlotSection>>> openHourObservable;
    private Observable<Lce<HashMap<String, Boolean>>> forbiddenObservable;
    private Observable<Lce<Response<BasicResponse>>> saveReservationObservable;

    public ReservationPresenter(Application application) {
        super(application);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setCurrentStep(int step) {
        currentStep = step;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public PersianCalendar getSelectedDateTime() {
        return selectedDateTime;
    }

    public void setSelectedDateTime(PersianCalendar dateTime) {
        RestaurantRepository.getInstance().getRestaurantOpenHours(getApplication(), null);
        if (selectedDateTime == null || dateTime == null || selectedDateTime.getTimeInMillis() != dateTime.getTimeInMillis()) {
            selectedDateTime = dateTime;

            setContainError(false);
            selectedTimeSlot = null;
            selectedTable = null;
            loadOpenHours(true);
            loadForbiddenTables(true);
            forceShowSummery();
        }
    }

    public OpenTimeSlot getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(OpenTimeSlot selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;

        setContainError(false);
        selectedTable = null;
        loadForbiddenTables(true);
        forceShowSummery();
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
        this.selectedTable = null;

        setContainError(false);
        loadForbiddenTables(true);
        forceShowSummery();
    }

    public int getLastGuestCountAdapterPosition() {
        return lastGuestCountAdapterPosition;
    }

    public void setLastGuestCountAdapterPosition(int lastGuestCountAdapterPosition) {
        this.lastGuestCountAdapterPosition = lastGuestCountAdapterPosition;
    }

    public Table getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(Table selectedTable) {
        this.selectedTable = selectedTable;

        setContainError(false);
        forceShowSummery();
    }

    public boolean isContainError() {
        return containError;
    }

    public void setContainError(boolean containError) {
        this.containError = containError;
    }

    public String getNoteToRestaurant() {
        return noteToRestaurant;
    }

    public void setNoteToRestaurant(String noteToRestaurant) {
        this.noteToRestaurant = noteToRestaurant;
    }

    @Override
    public void bindView(MvpView view) {}

    public void bindTimeView(ReservationContract.TimeView view) {
        timeView = view;
    }

    public void bindTableView(ReservationContract.TableView view) {
        tableView = view;
    }

    public void bindConfirmView(ReservationContract.ConfirmView view) {
        confirmView = view;
    }

    @Override
    public void loadOpenHours() {
        if (timeView == null)
            return;

        if (selectedDateTime == null)
            return;

        if (openHourObservable == null)
            openHourObservable = RestaurantRepository.getInstance()
                    .getRestaurantOpenHours(getApplication(), selectedDateTime)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        final long requestTime = System.currentTimeMillis();
        openHoursLastRequestTime = requestTime;

        openHourObservable.takeWhile(LifecycleBinder.notDestroyed(timeView))
                .compose(LifecycleBinder.subscribeWhenReady(timeView, new Lce.Observer<>(
                        lce -> {
                            if (requestTime < openHoursLastRequestTime)
                                return;

                            if (lce.isLoading())
                                timeView.showLoading();
                            else if (lce.hasError()) {
                                openHourObservable = null;
                                timeView.showError(lce.getError());
                            } else
                                timeView.showOpenHours(lce.getData());
                        }
                )));
    }

    public void loadOpenHours(boolean skipCache) {
        if (skipCache)
            openHourObservable = null;

        loadOpenHours();
    }

    @Override
    public void loadForbiddenTables() {
        if (tableView == null)
            return;

        if (selectedDateTime == null || selectedTimeSlot == null || guestCount == -1)
            return;

        if (forbiddenObservable == null)
            forbiddenObservable = ReservationRepository.getInstance()
                    .getForbiddenTables()
                    .compose(RxUtils.applySchedulers())
                    .cache();

        final long requestTime = System.currentTimeMillis();
        forbiddenTableLastRequestTime = requestTime;

        forbiddenObservable.takeWhile(LifecycleBinder.notDestroyed(tableView))
                .compose(LifecycleBinder.subscribeWhenReady(tableView, new Lce.Observer<>(
                        lce -> {
                            if (requestTime < forbiddenTableLastRequestTime)
                                return;

                            if (lce.isLoading())
                                tableView.showLoading();
                            else if (lce.hasError()) {
                                forbiddenObservable = null;
                                tableView.showError(lce.getError());
                            } else {
                                HashMap<String, Boolean> forbiddenTables = new HashMap<>(lce.getData());
                                addTablesWithSmallerCapacity(forbiddenTables);
                                tableView.showForbiddenTables(forbiddenTables);
                            }
                        }
                )));
    }

    public void loadForbiddenTables(boolean skipCache) {
        if (skipCache)
            forbiddenObservable = null;

        loadForbiddenTables();
    }

    @Override
    public void saveReservation() {
        Reservation res = buildReservationObj();
        saveReservationObservable = ReservationRepository.getInstance()
                .saveReservation(res)
                .compose(RxUtils.applySchedulers())
                .cache();
        loadPendingSaveReservation();
    }

    @Override
    public void loadPendingSaveReservation() {
        if (saveReservationObservable == null)
            return;

        saveReservationObservable.takeWhile(LifecycleBinder.notDestroyed(confirmView))
                .compose(LifecycleBinder.subscribeWhenReady(confirmView, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                confirmView.showLoading();
                            else if (lce.hasError()) {
                                saveReservationObservable = null;
                                confirmView.showError(lce.getError());
                            } else {
                                saveReservationObservable = null;
                                confirmView.handleResponse(lce.getData());
                            }
                        }
                )));
    }

    @Override
    public void saveInvitation(Reservation reservation, List<String> emails, String text) {
        ReservationRepository.getInstance()
                .saveInvitation(reservation, emails, text)
                .compose(RxUtils.applySchedulers())
                .subscribe(new Lce.Observer<>(
                        lce -> {
                            Context context = getApplication();
                            if (lce.isLoading() || lce.hasError() || context == null)
                                return;

                            Toast.makeText(context, "دعوتنامه با موفقیت ارسال شد.", Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    public Reservation buildReservationObj() {
        if (selectedDateTime == null || selectedTimeSlot == null || guestCount == -1)
            return null;

        Reservation r = new Reservation();
        r.setDatetime(selectedTimeSlot.getDatetime().getTimeInMillis() / 1000L);
        r.setGuestCount(guestCount);
        r.setTable(selectedTable);
        r.setNote(noteToRestaurant);

        return r;
    }

    private void forceShowSummery() {
        if (confirmView == null)
            return;

        boolean isDestroyed = confirmView.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED;
        if (!isDestroyed)
            confirmView.showSummery();
    }

    private void addTablesWithSmallerCapacity(HashMap<String, Boolean> forbiddenTable) {
        restaurant.getMaps()
                .forEach(map -> map.getTables()
                        .forEach(table -> {
                            if (table.getCount() < getGuestCount())
                                forbiddenTable.put(table.getId(), true);
                        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timeView = null;
        tableView = null;
        confirmView = null;
    }

    public static class Step {
        public static final int DateSelect = 4;
        public static final int TimeSelect = 3;
        public static final int GuestCount = 2;
        public static final int TableSelect = 1;
        public static final int Confirm = 0;
    }
}
