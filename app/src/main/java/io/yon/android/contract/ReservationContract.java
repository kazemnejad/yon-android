package io.yon.android.contract;

import java.util.HashMap;

import io.yon.android.api.response.BasicResponse;
import io.yon.android.util.calendar.PersianCalendar;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationContract extends Contract {
    public interface Presenter {
        void setCurrentStep(int step);

        int getCurrentStep();

        void setSelectedDateTime(PersianCalendar dateTime);

        PersianCalendar getSelectedDateTime();

        void setGuestCount(int guestCount);

        int getGuestCount();

        void saveReservation();

        void loadPendingSaveReservation();
    }

    public interface ForbiddenTablesPresenter {
        void loadForbiddenTables();
    }

    public interface TimeView extends MvpView {

    }

    public interface ConfirmView extends MvpView {
        void showSummery();

        void showLoading();

        void showError(Throwable e);

        void handleResponse(Response<BasicResponse> response);
    }

    public interface TableView extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void showForbiddenTables(HashMap<String, Boolean> forbiddenTables);
    }
}
