package io.yon.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import io.yon.android.model.Restaurant;
import io.yon.android.model.SearchResultSection;
import io.yon.android.model.Tag;
import io.yon.android.model.Zone;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemSearchResultRestaurantViewHolder;
import io.yon.android.view.adapter.viewholder.ItemSearchResultSectionViewHolder;
import io.yon.android.view.adapter.viewholder.ItemSearchResultTagViewHolder;
import io.yon.android.view.adapter.viewholder.ItemSearchResultZoneViewHolder;
import io.yon.android.view.adapter.viewholder.ViewHolder;

/**
 * Created by amirhosein on 8/30/2017 AD.
 */

public class SearchResultAdapter extends SectionedRecyclerViewAdapter<
        ItemSearchResultSectionViewHolder,
        ViewHolder,
        ViewHolder> {

    private static final int TAG = 0;
    private static final int ZONE = 1;
    private static final int RESTAURANT = 2;

    private Context context;
    private RxBus bus;
    private List<SearchResultSection> sections;

    public SearchResultAdapter(Context context, List<SearchResultSection> sections, RxBus bus) {
        this.context = context;
        this.sections = sections;
        this.bus = bus;
    }

    @Override
    protected int getSectionCount() {
        return sections != null ? sections.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int index) {
        return sections != null ? sections.get(index).getItems().size() : 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected ItemSearchResultSectionViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return ItemSearchResultSectionViewHolder.getFactory().create(LayoutInflater.from(context), parent, context, bus);
    }

    @Override
    protected ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case TAG:
                viewHolder = ItemSearchResultTagViewHolder.getFactory().create(inflater, parent, context, bus);
                break;

            case ZONE:
                viewHolder = ItemSearchResultZoneViewHolder.getFactory().create(inflater, parent, context, bus);
                break;

            case RESTAURANT:
                viewHolder = ItemSearchResultRestaurantViewHolder.getFactory().create(inflater, parent, context, bus);
                break;
        }

        return viewHolder;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ItemSearchResultSectionViewHolder holder, int index) {
        holder.bindContent(sections.get(index));
    }

    @Override
    protected void onBindSectionFooterViewHolder(ViewHolder holder, int section) {}

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int section, int position) {
        Object item = sections.get(section).getItems().get(position);
        if (holder instanceof ItemSearchResultTagViewHolder)
            ((ItemSearchResultTagViewHolder) holder).bindContent((Tag) item);
        else if (holder instanceof ItemSearchResultZoneViewHolder)
            ((ItemSearchResultZoneViewHolder) holder).bindContent((Zone) item);
        else if (holder instanceof ItemSearchResultRestaurantViewHolder)
            ((ItemSearchResultRestaurantViewHolder) holder).bindContent((Restaurant) item);
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        if (sections.get(section).getItems().get(position) instanceof Tag)
            return TAG;
        else if (sections.get(section).getItems().get(position) instanceof Zone)
            return ZONE;
        else if (sections.get(section).getItems().get(position) instanceof Restaurant)
            return RESTAURANT;

        return 10;
    }

    public void setData(List<SearchResultSection> lst) {
        sections = lst;
    }

    public List<SearchResultSection> getData() {
        return sections;
    }
}
