package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Zone;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 7/30/17.
 */

public class ItemRecommendedZoneViewHolder extends ViewHolder<Zone> {

    private final int placeHolderColor;
    private RelativeLayout container;
    private TextView title, subTitle;
    private ImageView icon;

    public static Factory<ItemRecommendedZoneViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemRecommendedZoneViewHolder(
                inflater.inflate(R.layout.item_recommended_zone, parent, false),
                context,
                bus
        );
    }

    public ItemRecommendedZoneViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        placeHolderColor = ContextCompat.getColor(getContext(), R.color.solidPlaceHolder);
    }

    @Override
    protected void findViews() {
        container = (RelativeLayout) findViewById(R.id.container);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        icon = (ImageView) findViewById(R.id.icon);
    }

    @Override
    public void bindContent(Zone zone) {
        title.setText(zone.getName());

        GlideApp.with(getContext())
                .load(zone.getAvatarUrl())
                .placeholder(new ColorDrawable(placeHolderColor))
                .centerCrop()
                .transition(withCrossFade())
                .into(icon);
    }
}
