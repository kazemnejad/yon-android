package io.yon.android.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import org.zakariya.stickyheaders.SectioningAdapter;

import io.yon.android.R;
import io.yon.android.model.MenuSection;

/**
 * Created by amirhosein on 8/14/2017 AD.
 */

public class ItemMenuSectionViewHolder extends SectioningAdapter.HeaderViewHolder {

    private TextView name;

    public ItemMenuSectionViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.name);
    }

    public void bindContent(MenuSection menuSection) {
        name.setText(menuSection.getName());
    }
}
