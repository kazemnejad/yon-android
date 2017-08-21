package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.HashMap;

import io.reactivex.Observable;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.Restaurant;
import io.yon.android.repository.Lce;
import io.yon.android.repository.ReservationRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationPresenter extends Presenter implements
        ReservationContract.Presenter,
        ReservationContract.TableAvailabilitiesPresenter {

    private PersianCalendar selectedDateTime;
    private int currentStep = Step.DateSelect;
    private int guestCount = -1;
    private int lastGuestCountAdapterPosition = -1;
    private Restaurant restaurant;

    private ReservationContract.TableView tableView;

    private Observable<Lce<HashMap<String, Boolean>>> availabilitiesObservable;

    public ReservationPresenter(Application application) {
        super(application);
    }

    @Override
    public void setCurrentStep(int step) {
        currentStep = step;
    }

    @Override
    public int getCurrentStep() {
        return currentStep;
    }

    @Override
    public void setSelectedDateTime(PersianCalendar dateTime) {
        selectedDateTime = dateTime;
    }

    @Override
    public PersianCalendar getSelectedDateTime() {
        return selectedDateTime;
    }

    @Override
    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    @Override
    public int getGuestCount() {
        return guestCount;
    }

    public int getLastGuestCountAdapterPosition() {
        return lastGuestCountAdapterPosition;
    }

    public void setLastGuestCountAdapterPosition(int lastGuestCountAdapterPosition) {
        this.lastGuestCountAdapterPosition = lastGuestCountAdapterPosition;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void bindView(MvpView view) {

    }

    public void bindTableView(ReservationContract.TableView view) {
        tableView = view;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        tableView = null;
    }

    @Override
    public void loadTableAvailabilities() {
        if (availabilitiesObservable == null)
            availabilitiesObservable = ReservationRepository.getInstance()
                    .getTableAvailabilities()
                    .compose(RxUtils.applySchedulers())
                    .cache();

        availabilitiesObservable.takeWhile(LifecycleBinder.notDestroyed(tableView))
                .compose(LifecycleBinder.subscribeWhenReady(tableView, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                tableView.showLoading();
                            else if (lce.hasError()) {
                                availabilitiesObservable = null;
                                tableView.showError(lce.getError());
                            } else
                                tableView.showTableAvailabilities(lce.getData());
                        }
                )));
    }

    public void loadTableAvailabilities(boolean skipCache) {
        if (skipCache)
            availabilitiesObservable = null;

        loadTableAvailabilities();
    }

    public static class Step {
        public static final int DateSelect = 3;
        public static final int TimeSelect = 2;
        public static final int GuestCount = 1;
        public static final int TableSelect = 0;
        public static final int Final = 328;
    }
}
