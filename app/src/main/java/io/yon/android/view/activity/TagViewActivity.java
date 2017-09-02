package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.parceler.Parcels;

import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.TagContract;
import io.yon.android.model.Tag;
import io.yon.android.presenter.TagPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.RestaurantListItemConfig;
import io.yon.android.view.dialog.TagSelectDialog;
import io.yon.android.view.widget.LiteralAppBarStateChangeListener;

/**
 * Created by amirhosein on 8/29/2017 AD.
 */

public class TagViewActivity extends RestaurantListActivity implements TagContract.View {

    private List<Tag> allTags;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView sampleBackground;
    private View dimmer;
    private TextView toolbarTitle;
    private ImageButton toolbarRightBtn;
    private FlexboxLayout tagsContainer;

    private TagPresenter presenter;

    public static void start(Context context, Tag tag) {
        context.startActivity(
                new Intent(context, TagViewActivity.class)
                        .putExtra("tag", Parcels.wrap(tag))
        );
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tag_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        presenter = ViewModelProviders.of(this).get(TagPresenter.class);
        presenter.bindView(this);

        Tag tag = Parcels.unwrap(getIntent().getParcelableExtra("tag"));
//        Tag tag = new Tag();
//        tag.setName("پیتزا");
//        tag.setSlug("pizza");
        presenter.setInitialTag(tag);

        initView();

        presenter.loadRestaurants(presenter.getSelectedTags());
        presenter.loadTags();
    }

    @Override
    protected void findViews() {
        super.findViews();
        tagsContainer = (FlexboxLayout) findViewById(R.id.tags_container);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        sampleBackground = (ImageView) findViewById(R.id.sample_background);
        dimmer = findViewById(R.id.dimmer);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text_main);
        toolbarRightBtn = (ImageButton) findViewById(R.id.toolbar_icon_right);
    }

    private void initView() {
        renderSelectedTags();
        renderSelectedTagsToTitle();

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new LiteralAppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED)
                    toolbarTitle.animate()
                            .alpha(1f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(200);
                else {
                    toolbarTitle.setAlpha(0f);
                    toolbarTitle.setTranslationY(getToolbarHeight());
                }
            }
        });

        updateAppbarBackgroundDimensions(0);

        toolbarRightBtn.setOnClickListener(v -> finish());
    }

    protected void renderSelectedTags() {
        tagsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < presenter.getSelectedTags().size(); i++)
            tagsContainer.addView(createView(inflater, i));

        if (allTags != null && presenter.getSelectedTags().size() != allTags.size()) {
            View view = inflater.inflate(R.layout.item_add_tag, tagsContainer, false);
            view.setOnClickListener(this::handleAddTagButtonClick);
            tagsContainer.addView(view);
        }

        updateAppbarBackgroundDimensions(tagsContainer.getHeight());
    }

    protected View createView(LayoutInflater inflater, int position) {
        Tag tag = presenter.getSelectedTags().get(position);

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.appbar_item_tag_label, tagsContainer, false);

        TextView tagLabelView = (TextView) view.findViewById(R.id.label);
        tagLabelView.setText(tag.getName());

        View btnRemove = view.findViewById(R.id.btn_remove);
        btnRemove.setTag(position);
        btnRemove.setOnClickListener(this::handleRemoveTagClick);

        return view;
    }

    private void renderSelectedTagsToTitle() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (Tag tag : presenter.getSelectedTags()) {
            builder.append(prefix);
            prefix = "، ";
            builder.append(tag.getName());
        }

        toolbarTitle.setText(builder.toString());
    }

    protected void handleAddTagButtonClick(View v) {
        new TagSelectDialog(this, allTags, presenter.getSelectedTags())
                .setOnTagSelectListener(this::addNewTag)
                .show();
    }

    protected void handleRemoveTagClick(View v) {
        try {
            int index = (int) v.getTag();
            presenter.getSelectedTags().remove(index);
            presenter.loadRestaurants(presenter.getSelectedTags(), true);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        renderSelectedTags();
        renderSelectedTagsToTitle();
    }

    private void addNewTag(Tag tag) {
        presenter.getSelectedTags().add(tag);
        presenter.loadRestaurants(presenter.getSelectedTags(), true);

        renderSelectedTags();
        renderSelectedTagsToTitle();
    }

    protected void updateAppbarBackgroundDimensions(int oldHeight) {
        tagsContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tagsContainer.getHeight() != oldHeight) {
                    ViewUtils.removeOnGlobalLayoutListener(tagsContainer.getViewTreeObserver(), this);

                    int height = tagsContainer.getHeight();
                    if (height == 0)
                        height = collapsingToolbarLayout.getHeight();
                    else
                        height += ViewUtils.px(TagViewActivity.this, 36.5f);

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sampleBackground.getLayoutParams();
                    params.height = height;

                    sampleBackground.setLayoutParams(params);
                    dimmer.setLayoutParams(params);
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showTags(List<Tag> tags) {
        allTags = tags;
        renderSelectedTags();
    }

    @Override
    protected RestaurantListItemConfig getConfig() {
        return super.getConfig();
    }
}
