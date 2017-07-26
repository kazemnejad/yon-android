package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Restaurant;
import io.yon.android.model.SimpleSection;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;

/**
 * Created by amirhosein on 7/24/17.
 */

public class SimpleSectionViewHolder extends ViewHolder<SimpleSection> {

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

    private View[] containers;
    private TextView[] texts;
    private ImageView[] icons;

    private TextView title;

    public static Factory<SimpleSectionViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new SimpleSectionViewHolder(
                inflater.inflate(R.layout.simple_section, parent, false),
                context,
                bus
        );
    }

    public SimpleSectionViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        containers = new View[]{null, null, null, null};
        texts = new TextView[]{null, null, null, null};
        icons = new ImageView[]{null, null, null, null};

        title = (TextView) findViewById(R.id.title);
        for (int i = 0; i < 4; i++) {
            containers[i] = findViewById(CONTAINER_IDS[i]);
            texts[i] = (TextView) findViewById(TEXT_IDS[i]);
            icons[i] = (ImageView) findViewById(ICON_IDS[i]);
        }
    }

    @Override
    public void bindContent(SimpleSection section) {
        title.setText(section.getTitle());

        for (int i = 0; i < 4; i++) {
            Restaurant rest = section.getRestaurants().get(i);
            if (rest == null)
                break;

            containers[i].setVisibility(View.VISIBLE);
            texts[i].setText(rest.getName());

            int color = ContextCompat.getColor(getContext(), R.color.solidPlaceHolder);
            GlideApp.with(getContext())
                    .load(rest.getAvatarUrl())
                    .placeholder(color)
                    .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                    .into(icons[i]);
        }
    }
}
