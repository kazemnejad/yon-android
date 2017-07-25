package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemSimpleRestaurantViewHolder;

/**
 * Created by amirhosein on 7/25/17.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<ItemSimpleRestaurantViewHolder> {

    private Context context;
    private RxBus bus;

    private List<Restaurant> mData;

    public RestaurantsAdapter(Context context, List<Restaurant> data, RxBus bus) {
        this.context = context;
        this.bus = bus;
        this.mData = data;
    }

    @Override
    public ItemSimpleRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemSimpleRestaurantViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_simple_restaurant, parent, false),
                context,
                bus
        );
    }

    @Override
    public void onBindViewHolder(ItemSimpleRestaurantViewHolder holder, int position) {
        holder.bindContent(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<Restaurant> restaurants) {
        mData = restaurants;
    }
}
