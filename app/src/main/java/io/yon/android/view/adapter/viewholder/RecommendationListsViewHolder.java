package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 7/22/17.
 */

public class RecommendationListsViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private RxBus bus;

    public RecommendationListsViewHolder(View itemView, RxBus rxBus) {
        super(itemView);

        context = itemView.getContext().getApplicationContext();
        bus = rxBus;
    }

    public void bindContent(List<RecommendationList> recommendationLists) {

    }
}
