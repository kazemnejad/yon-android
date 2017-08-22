package io.yon.android.presenter;

import android.app.Application;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import java.util.HashMap;

import io.reactivex.Observable;
import io.yon.android.contract.ReservationContract;
import io.yon.android.model.Restaurant;
import io.yon.android.model.Table;
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
        ReservationContract.ForbiddenTablesPresenter {

    private Restaurant restaurant;
    private PersianCalendar selectedDateTime;
    private int currentStep = Step.DateSelect;
    private int guestCount = -1;
    private int lastGuestCountAdapterPosition = -1;
    private Table selectedTable = null;

    private long forbiddenTableLastRequestTime;

    private ReservationContract.TableView tableView;

    private Observable<Lce<HashMap<String, Boolean>>> forbiddenObservable;

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
        this.selectedTable = null;
        loadForbiddenTables();
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

    public Table getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(Table selectedTable) {
        this.selectedTable = selectedTable;
    }

    @Override
    public void bindView(MvpView view) {}

    public void bindTableView(ReservationContract.TableView view) {
        tableView = view;
    }

    @Override
    public void loadForbiddenTables() {
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
        tableView = null;
    }

    public static class Step {
        public static final int DateSelect = 4;
        public static final int TimeSelect = 3;
        public static final int GuestCount = 2;
        public static final int TableSelect = 1;
        public static final int Confirm = 0;
    }
}
