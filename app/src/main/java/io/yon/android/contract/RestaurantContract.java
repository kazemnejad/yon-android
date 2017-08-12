package io.yon.android.contract;

import io.yon.android.model.Restaurant;
import io.yon.android.view.MvpView;

/**
 * Created by amirhosein on 8/10/2017 AD.
 */

public class RestaurantContract extends Contract {
    public interface Presenter {
        void loadRestaurant(int id);
        void loadRestaurantMenu(int id);
        void loadRestaurantReview(int id);
    }

    public interface View extends MvpView{
        void showLoading();
        void showError(Throwable e);
        void showRestaurant(Restaurant restaurant);
        void showRestaurantMenu();
        void showRestaurantReview();
    }
}