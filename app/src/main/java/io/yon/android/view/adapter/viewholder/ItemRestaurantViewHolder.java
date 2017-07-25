package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 7/25/17.
 */

public class ItemRestaurantViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    public ItemRestaurantViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

    }
}
