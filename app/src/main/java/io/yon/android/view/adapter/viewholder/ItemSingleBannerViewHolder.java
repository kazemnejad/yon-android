package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.model.Banner;
import io.yon.android.util.RxBus;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RoundedCornersTransformation;
import io.yon.android.view.widget.BitmapViewGroupTarget;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 7/31/17.
 */

public class ItemSingleBannerViewHolder extends ViewHolder<Banner> implements View.OnClickListener {

    private View star, gradientContainer;
    private CardView card;
    private ImageView icon;
    private RelativeLayout container;
    private TextView title, subTitle, rate;

    private Banner currentBanner;

    public static Factory<ItemSingleBannerViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new ItemSingleBannerViewHolder(
                inflater.inflate(R.layout.item_single_banner, parent, false),
                context,
                bus
        );
    }

    public ItemSingleBannerViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
    }

    @Override
    protected void findViews() {
        container = (RelativeLayout) findViewById(R.id.container);
        card = (CardView) findViewById(R.id.card);
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.sub_title);
        rate = (TextView) findViewById(R.id.rate);
        star = findViewById(R.id.star);
        gradientContainer = findViewById(R.id.gradient_container);
    }

    @Override
    protected void initViews() {
        card.setOnClickListener(this);
    }

    @Override
    public void bindContent(Banner banner) {
        currentBanner = banner;
        title.setText(banner.getTitle());
        subTitle.setText(banner.getSubTitle());
        if (banner.getRate() != -1) {
            rate.setText(banner.getRateLabel());
            rate.setVisibility(View.VISIBLE);
            star.setVisibility(View.VISIBLE);
        }

        ViewCompat.setBackground(gradientContainer, getGradientDrawable(banner.getColorCode()));

        GlideApp.with(getContext())
                .load(banner.getBannerUrl())
                .placeholder(R.color.solidPlaceHolder)
                .centerCrop()
                .transition(withCrossFade())
                .into(new BitmapViewGroupTarget(container));

        GlideApp.with(getContext())
                .asBitmap()
                .load(banner.getIconUrl())
                .placeholder(R.color.solidPlaceHolder)
                .centerCrop()
                .transform(new RoundedCornersTransformation(getContext(), 30, 0))
                .into(icon);
    }

    private static GradientDrawable getGradientDrawable(String colorCode) {
        int color = Color.parseColor(colorCode);
        int transparentColor = Color.argb(80, 0, 0, 0);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{color, transparentColor});
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setGradientRadius(1000);
        drawable.setGradientCenter(1f, 0.5f);

        return drawable;
    }

    @Override
    public void onClick(View v) {
        try {
            getBus().send(currentBanner);
        } catch (Exception ignored) {
        }
    }
}
