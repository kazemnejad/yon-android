package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Tag;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 7/30/17.
 */

public class ItemRecommendedTagViewHolder extends ViewHolder<Tag> {

    private final ColorDrawable placeHolder;
    private RelativeLayout container;
    private TextView title, subTitle;
    private ImageView icon;

    public static Factory<ItemRecommendedTagViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemRecommendedTagViewHolder(
                inflater.inflate(R.layout.item_recommended_tag, parent, false),
                context,
                bus
        );
    }

    public ItemRecommendedTagViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        placeHolder = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.solidPlaceHolder));
    }

    @Override
    protected void findViews() {
        container = (RelativeLayout) findViewById(R.id.container);
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
    }

    @Override
    public void bindContent(Tag tag) {
        title.setText(tag.getName());
        subTitle.setText("بولشت‌ترین تگ");

        GlideApp.with(getContext())
                .load(tag.getAvatarUrl())
                .placeholder(placeHolder)
                .centerCrop()
                .transition(withCrossFade())
                .into(icon);

    }
}
