package io.yon.android.contract;

import java.util.HashMap;
import java.util.List;

import io.yon.android.api.response.BasicResponse;
import io.yon.android.model.OpenTimeSlotSection;
import io.yon.android.model.Reservation;
import io.yon.android.view.LceView;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 8/20/2017 AD.
 */

public class ReservationContract extends Contract {
    public interface Presenter {

        void loadOpenHours();

        void loadForbiddenTables();

        void saveReservation();

        void loadPendingSaveReservation();

        void saveInvitation(Reservation reservation, List<String> emails, String text);
    }

    public interface CancelPresenter {
        void cancelReservation(Reservation reservation);
    }

    public interface TimeView extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void showOpenHours(List<OpenTimeSlotSection> openTimeSlots);
    }

    public interface ConfirmView extends MvpView {
        void showSummery();

        void showLoading();

        void showError(Throwable e);

        void handleResponse(Response<Reservation> response);
    }

    public interface TableView extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void showForbiddenTables(HashMap<String, Boolean> forbiddenTables);
    }

    public interface CancelView extends LceView<Response<BasicResponse>> {}
}
