package io.yon.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.MenuSection;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemEatableViewHolder;
import io.yon.android.view.adapter.viewholder.ItemMenuSectionViewHolder;

/**
 * Created by amirhosein on 8/14/2017 AD.
 */

public class RestaurantMenuAdapter extends SectioningAdapter {
    private Context context;
    private RxBus bus;
    private List<MenuSection> menuData;

    public RestaurantMenuAdapter(Context context, List<MenuSection> menuData, RxBus bus) {
        this.context = context;
        this.menuData = menuData;
        this.bus = bus;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new ItemEatableViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_eatable, parent, false),
                context,
                bus
        );
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return new ItemMenuSectionViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_menu_section, parent, false)
        );
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public int getNumberOfSections() {
        return menuData != null ? menuData.size() : 0;
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return menuData.get(sectionIndex).getEatables() != null ? menuData.get(sectionIndex).getEatables().size() : 0;
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        if (viewHolder instanceof ItemEatableViewHolder)
            ((ItemEatableViewHolder) viewHolder).bindContent(
                    menuData.get(sectionIndex)
                            .getEatables()
                            .get(itemIndex)
            );
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        if (viewHolder instanceof ItemMenuSectionViewHolder)
            ((ItemMenuSectionViewHolder) viewHolder).bindContent(menuData.get(sectionIndex));
    }
}
