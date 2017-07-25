package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;

/**
 * Created by amirhosein on 7/22/17.
 */

public class CompactRecommendationsViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private RxBus bus;

    private RecyclerView recyclerView;
    private Adapter adapter;

    public CompactRecommendationsViewHolder(View itemView, RxBus rxBus) {
        super(itemView);

        context = itemView.getContext().getApplicationContext();
        bus = rxBus;

        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setInitialPrefetchItemCount(3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(context, new ArrayList<>(), rxBus);
        recyclerView.setAdapter(adapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    public void bindContent(List<RecommendationList> recommendationLists) {
        adapter.updateData(recommendationLists);
        adapter.notifyDataSetChanged();
    }

    public static class Adapter extends RecyclerView.Adapter<ItemRecommendationViewHolder> {
        private Context context;
        private RxBus bus;

        private List<RecommendationList> mData;

        public Adapter(Context context, List<RecommendationList> data, RxBus bus) {
            this.context = context;
            this.bus = bus;
            this.mData = data;
        }

        @Override
        public ItemRecommendationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemRecommendationViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false),
                    context,
                    bus
            );
        }

        @Override
        public void onBindViewHolder(ItemRecommendationViewHolder holder, int position) {
            holder.bindContent(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void updateData(List<RecommendationList> data) {
            mData = data;
        }
    }
}
