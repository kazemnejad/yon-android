package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 7/22/17.
 */

public class ItemBannerViewHolder extends ViewHolder<Banner> {

    private ImageView banner;
    private TextView title;
    private TextView subTitle;

    public static Factory<ItemBannerViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemBannerViewHolder(
                inflater.inflate(R.layout.item_banner, parent, false),
                context,
                bus
        );
    }

    public ItemBannerViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        banner = (ImageView) findViewById(R.id.banner);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.subtitle);
    }

    @Override
    public void bindContent(Banner b) {
        title.setText(b.getTitle());
        subTitle.setText(b.getSubTitle());

        int color = ContextCompat.getColor(getContext(), R.color.solidPlaceHolder);
        GlideApp.with(getContext())
                .load(b.getBannerUrl())
                .placeholder(new ColorDrawable(color))
                .centerCrop()
                .transition(withCrossFade())
                .into(banner);
    }
}
