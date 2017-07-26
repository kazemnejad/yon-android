package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.yon.android.R;
import io.yon.android.model.RecommendationList;
import io.yon.android.model.Restaurant;
import io.yon.android.util.RxBus;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.widget.NonScrollingLayoutManager;

/**
 * Created by amirhosein on 7/25/17.
 */

public class ItemRecommendationViewHolder extends ViewHolder<RecommendationList> {

    private Adapter<Restaurant, ItemSimpleRestaurantViewHolder> adapter;

    public static Factory<ItemRecommendationViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemRecommendationViewHolder(
                inflater.inflate(R.layout.item_recommendation, parent, false),
                context,
                bus
        );
    }

    public ItemRecommendationViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new NonScrollingLayoutManager(getContext());
        layoutManager.setInitialPrefetchItemCount(3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter<>(getContext(), null, getBus(), ItemSimpleRestaurantViewHolder.getFactory());
        recyclerView.setAdapter(adapter);

        int screenWidthDp = ViewUtils.dp(getContext(), ViewUtils.getScreenWidth(getContext()));

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewUtils.px(getContext(), screenWidthDp - 54),
                params.height
        ));

    }

    @Override
    public void bindContent(RecommendationList lst) {
        adapter.setDataAndNotify(lst.getRestaurants());
    }
}
