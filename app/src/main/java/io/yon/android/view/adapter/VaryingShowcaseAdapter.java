package io.yon.android.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.BannersViewHolder;
import io.yon.android.view.adapter.viewholder.RecommendationListsViewHolder;

/**
 * Created by amirhosein on 7/22/17.
 */

public class VaryingShowcaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BANNERS = 0;
    private static final int RECOMMENDATIONS = 1;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private RxBus rxBus;

    public VaryingShowcaseAdapter(ArrayList<Object> data, RxBus bus) {
        mData = data;
        rxBus = bus;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case BANNERS:
                view = inflater.inflate(R.layout.banners, parent, false);
                viewHolder = new BannersViewHolder(view, rxBus);
                break;

            case RECOMMENDATIONS:
                view = inflater.inflate(R.layout.banners, parent, false);
                viewHolder = new RecommendationListsViewHolder(view, rxBus);
                break;

            case 5:
                view = inflater.inflate(R.layout.dummy_vh, parent, false);
                viewHolder = new DummyViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof List) {
            if (((List) item).size() > 0 && ((List) item).get(0) instanceof Banner)
                return BANNERS;

            if (((List) item).size() > 0 && ((List) item).get(0) instanceof RecommendationList)
                return RECOMMENDATIONS;

        }

        return 5;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannersViewHolder)
            ((BannersViewHolder) holder).bindContent((List<Banner>) mData.get(position));
        else if (holder instanceof RecommendationListsViewHolder)
            ((RecommendationListsViewHolder) holder).bindContent((List<RecommendationList>) mData.get(position));
    }

    public static class DummyViewHolder extends RecyclerView.ViewHolder {
        public DummyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
