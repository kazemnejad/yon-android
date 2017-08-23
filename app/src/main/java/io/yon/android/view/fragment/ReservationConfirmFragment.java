package io.yon.android.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import io.yon.android.R;

/**
 * Created by amirhosein on 8/19/2017 AD.
 */

public class ReservationConfirmFragment extends Fragment {

    private ImageView userAvatar;

    @Override
    protected int getResourceLayoutId() {
        return R.layout.fragment_reservation_confitm;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        userAvatar.setImageDrawable(
                TextDrawable.builder()
                        .buildRound("ا", generator.getRandomColor())
        );
    }

    @Override
    protected void findViews(View v) {
        userAvatar = (ImageView) v.findViewById(R.id.user_avatar);
    }


}
