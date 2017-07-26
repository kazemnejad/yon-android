package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.Adapter;

/**
 * Created by amirhosein on 7/22/17.
 */

public class CompactRecommendationsViewHolder extends ViewHolder<List<RecommendationList>> {

    public static Factory<CompactRecommendationsViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new CompactRecommendationsViewHolder(
                inflater.inflate(R.layout.compact_recommendations, parent, false),
                context,
                bus
        );
    }

    private Adapter<RecommendationList, ItemRecommendationViewHolder> adapter;

    public CompactRecommendationsViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    protected void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setInitialPrefetchItemCount(3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter<>(getContext(), null, getBus(), ItemRecommendationViewHolder.getFactory());
        recyclerView.setAdapter(adapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void bindContent(List<RecommendationList> lst) {
        adapter.setDataAndNotify(lst);
    }
}
