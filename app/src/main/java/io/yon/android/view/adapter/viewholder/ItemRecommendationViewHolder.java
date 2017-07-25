package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import io.yon.android.R;
import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.RestaurantsAdapter;
import io.yon.android.view.widget.NonScrollingLayoutManager;

/**
 * Created by amirhosein on 7/25/17.
 */

public class ItemRecommendationViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RxBus bus;

    private RecyclerView recyclerView;
    private RestaurantsAdapter adapter;

    public ItemRecommendationViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new NonScrollingLayoutManager(context);
        layoutManager.setInitialPrefetchItemCount(3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RestaurantsAdapter(context, new ArrayList<>(), bus);
        recyclerView.setAdapter(adapter);
    }

    public void bindContent(RecommendationList recomList) {
        adapter.updateData(recomList.getRestaurants());
        adapter.notifyDataSetChanged();
    }
}
