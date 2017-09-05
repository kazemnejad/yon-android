package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import io.yon.android.R;
import io.yon.android.contract.RestaurantListContract;
import io.yon.android.model.Banner;
import io.yon.android.presenter.RestaurantListPresenter;
import io.yon.android.view.GlideApp;
import io.yon.android.view.RestaurantListItemConfig;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by amirhosein on 8/28/2017 AD.
 */

public class TheRestaurantListActivity extends RestaurantListActivity implements RestaurantListContract.View {

    private Banner banner;

    private TextView description;
    private ImageView bannerImage;

    private RestaurantListPresenter presenter;

    public static void start(Context context, Banner banner) {
        context.startActivity(
                new Intent(context, TheRestaurantListActivity.class)
                        .putExtra("banner", Parcels.wrap(banner))
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_the_restaurant_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        banner = Parcels.unwrap(getIntent().getParcelableExtra("banner"));
//        banner = new Banner();
//        banner.setTitle("تخفیف ویژه به مناسبت هفته‌ی دفاع مقدس");
//        banner.setBannerUrl("http://162.243.174.32/banners/banner1.jpg");
//        banner.setTargetListDescription("لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. ");

        setTitle(banner.getTitle());
        setDisplayHomeAsUpEnabled(true);

        initView();

        presenter = ViewModelProviders.of(this).get(RestaurantListPresenter.class);
        presenter.bindView(this);

        presenter.loadRestaurantList(banner.getTargetListId());
    }

    @Override
    protected void findViews() {
        super.findViews();
        description = (TextView) findViewById(R.id.description);
        bannerImage = (ImageView) findViewById(R.id.banner);
    }

    @Override
    protected void onBtnRetryClick() {
        presenter.loadRestaurantList(banner.getTargetListId());
    }

    @Override
    protected RestaurantListItemConfig getConfig() {
        return super.getConfig().setShowTags(true);
    }

    protected void initView() {
        GlideApp.with(this)
                .load(banner.getBannerUrl())
                .placeholder(R.color.solidPlaceHolder)
                .centerCrop()
                .transition(withCrossFade())
                .into(bannerImage);

        setDescription(banner.getTargetListDescription() != null ? banner.getTargetListDescription() : banner.getSubTitle());
    }

    protected void setDescription(String text) {
        description.append(fromHtml(text));
        description.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        else
            return Html.fromHtml(source);
    }
}
