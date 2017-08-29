package io.yon.android.view.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.viewholder.ItemSimpleTagLabelViewHolder;
import io.yon.android.view.adapter.viewholder.ViewHolder;

/**
 * Created by amirhosein on 8/29/2017 AD.
 */

public class SimpleTagSelectAdapter extends Adapter<Tag, ItemSimpleTagLabelViewHolder> {

    private HashMap<String, String> selectedTags = new HashMap<>();

    public SimpleTagSelectAdapter(Context context, List<Tag> data, List<Tag> selectedTags, RxBus bus, ViewHolder.Factory<ItemSimpleTagLabelViewHolder> factory) {
        super(context, data, bus, factory);

        if (selectedTags != null)
            for (Tag tag : selectedTags)
                this.selectedTags.put(tag.getSlug(), "");
    }

    @Override
    public void onBindViewHolder(ItemSimpleTagLabelViewHolder holder, int position) {
        Tag tag = getData().get(position);
        holder.bindContent(tag, selectedTags.containsKey(tag.getSlug()));
    }
}
