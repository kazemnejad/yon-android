package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.SimpleSection;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

/**
 * Created by amirhosein on 7/24/17.
 */

public class SimpleSectionViewHolder extends RecyclerView.ViewHolder {

    private static final int[] CONTAINER_IDS = {
            R.id.container_1,
            R.id.container_3,
            R.id.container_2,
            R.id.container_4
    };

    private static final int[] TEXT_IDS = {
            R.id.text_1,
            R.id.text_2,
            R.id.text_3,
            R.id.text_4,
    };

    private static final int[] ICON_IDS = {
            R.id.img_1,
            R.id.img_2,
            R.id.img_3,
            R.id.img_4,
    };

    private Context context;
    private RxBus bus;

    private View[] containtes = {null, null, null, null};
    private TextView[] texts = {null, null, null, null};
    private ImageView[] icons = {null, null, null, null};

    private TextView title;

    public SimpleSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView);

        this.context = context;
        this.bus = bus;

        title = (TextView) itemView.findViewById(R.id.title);
        for (int i = 0; i < 4; i++) {
            containtes[i] = itemView.findViewById(CONTAINER_IDS[i]);
            texts[i] = (TextView) itemView.findViewById(TEXT_IDS[i]);
            icons[i] = (ImageView) itemView.findViewById(ICON_IDS[i]);
        }
    }

    public void bindContent(SimpleSection section) {
        title.setText(section.getTitle());

        for (int i = 0; i < 4; i++) {
            Restaurant rest = section.getRestaurants().get(i);
            if (rest == null)
                break;

            containtes[i].setVisibility(View.VISIBLE);
            texts[i].setText(rest.getName());

            int color = ContextCompat.getColor(context, R.color.solidPlaceHolder);
            GlideApp.with(context)
                    .load(rest.getAvatar())
                    .placeholder(color)
                    .circleCrop()
                    .into(icons[i]);
        }
    }
}
