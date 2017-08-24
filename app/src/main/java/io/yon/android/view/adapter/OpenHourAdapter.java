package io.yon.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import io.yon.android.model.OpenTimeSlot;
import io.yon.android.model.OpenTimeSlotSection;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemOpenTimeSlotSectionViewHolder;
import io.yon.android.view.adapter.viewholder.ItemOpenTimeSlotViewHolder;
import io.yon.android.view.adapter.viewholder.ViewHolder;

/**
 * Created by amirhosein on 8/24/2017 AD.
 */

public class OpenHourAdapter extends SectionedRecyclerViewAdapter<
        ItemOpenTimeSlotSectionViewHolder,
        ItemOpenTimeSlotViewHolder,
        ViewHolder> {

    private Context context;
    private RxBus bus;
    private List<OpenTimeSlotSection> sections;
    private OpenTimeSlot selectedTimeSlot;

    private int lastSelectedPosition = -1;

    public OpenHourAdapter(Context context, List<OpenTimeSlotSection> sections, RxBus bus) {
        this(context, sections, bus, null);
    }

    public OpenHourAdapter(Context context, List<OpenTimeSlotSection> sections, RxBus bus, OpenTimeSlot selectedTimeSlot) {
        this.context = context;
        this.sections = sections;
        this.bus = bus;
        this.selectedTimeSlot = selectedTimeSlot;
    }

    @Override
    protected int getSectionCount() {
        return sections != null ? sections.size() : 0;
    }

    @Override
    protected int getItemCountForSection(int index) {
        return sections.get(index).getOpenTimeSlots().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected ItemOpenTimeSlotSectionViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return ItemOpenTimeSlotSectionViewHolder.getFactory().create(LayoutInflater.from(context), parent, context, bus);
    }

    @Override
    protected ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ItemOpenTimeSlotViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return ItemOpenTimeSlotViewHolder.getFactory().create(LayoutInflater.from(context), parent, context, bus);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ItemOpenTimeSlotSectionViewHolder holder, int position) {
        OpenTimeSlotSection section = sections.get(position);
        holder.bindContent(section.getLabel());
    }

    @Override
    protected void onBindSectionFooterViewHolder(ViewHolder holder, int position) {}

    @Override
    protected void onBindItemViewHolder(ItemOpenTimeSlotViewHolder holder, int section, int position) {
        OpenTimeSlot slot = sections.get(section).getOpenTimeSlots().get(position);
        holder.bindContent(slot, selectedTimeSlot != null && slot.getHour() == selectedTimeSlot.getHour());
    }

    public void setSelectedTimeSlot(OpenTimeSlot selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public List<OpenTimeSlotSection> getData() {
        return sections;
    }

    public void activate(int position) {
        if (lastSelectedPosition != -1)
            notifyItemChanged(lastSelectedPosition);

        notifyItemChanged(position);
        lastSelectedPosition = position;
    }
}
