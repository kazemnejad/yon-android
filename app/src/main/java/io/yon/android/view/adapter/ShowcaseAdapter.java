package io.yon.android.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.model.RecommendationList;
import io.yon.android.model.SimpleSection;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.BannersViewHolder;
import io.yon.android.view.adapter.viewholder.CompactRecommendationsViewHolder;
import io.yon.android.view.adapter.viewholder.RecommendedTags;
import io.yon.android.view.adapter.viewholder.SimpleSectionViewHolder;
import io.yon.android.view.adapter.viewholder.ViewHolder;

/**
 * Created by amirhosein on 7/22/17.
 */

public class ShowcaseAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int BANNERS = 0;
    private static final int RECOMMENDATIONS = 1;
    private static final int SIMPLE_SECTION = 2;
    private static final int TAGS = 3;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private RxBus bus;
    private Context context;

    public ShowcaseAdapter(Context context, RxBus bus, ArrayList<Object> data) {
        this.mData = data;
        this.bus = bus;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case BANNERS:
                return BannersViewHolder.getFactory().create(inflater, parent, context, bus);

            case RECOMMENDATIONS:
                return CompactRecommendationsViewHolder.getFactory().create(inflater, parent, context, bus);

            case SIMPLE_SECTION:
                return SimpleSectionViewHolder.getFactory().create(inflater, parent, context, bus);

            case TAGS:
                return RecommendedTags.getFactory().create(inflater, parent, context, bus);

            case 5:
                return new DummyViewHolder(inflater.inflate(R.layout.dummy_vh, parent, false), null);
        }

        return null;
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

            if (((List) item).size() > 0 && ((List) item).get(0) instanceof Tag)
                return TAGS;
        }

        if (item instanceof SimpleSection)
            return SIMPLE_SECTION;

        return 5;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof BannersViewHolder)
            ((BannersViewHolder) holder).bindContent((List<Banner>) mData.get(position));
        else if (holder instanceof CompactRecommendationsViewHolder)
            ((CompactRecommendationsViewHolder) holder).bindContent((List<RecommendationList>) mData.get(position));
        else if (holder instanceof SimpleSectionViewHolder)
            ((SimpleSectionViewHolder) holder).bindContent((SimpleSection) mData.get(position));
        else if (holder instanceof RecommendedTags)
            ((RecommendedTags) holder).bindContent((List<Tag>) mData.get(position));
    }

    public static class DummyViewHolder extends ViewHolder<Object> {

        public DummyViewHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }
}
