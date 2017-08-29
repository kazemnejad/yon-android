package io.yon.android.view.activity;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.yon.android.R;
import io.yon.android.contract.TestContract;
import io.yon.android.model.Tag;
import io.yon.android.presenter.TestPresenter;
import io.yon.android.repository.RestaurantRepository;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.dialog.TagSelectDialog;
import io.yon.android.view.widget.LiteralAppBarStateChangeListener;

/**
 * Created by amirhosein on 8/29/2017 AD.
 */

public class TagViewActivity extends RestaurantListActivity implements TestContract.View {

    private FlexboxLayout tagsContainer;

    private TextView toolbarTitle;

    private List<Tag> allTags;
    private List<Tag> selectedTags = new ArrayList<>();

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView sampleBackground;
    private View dimmer;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tag_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        initView();

        TestPresenter presenter = new TestPresenter(getApplication());
        presenter.bindView(this);
        presenter.loadTestRestaurantList();
    }

    @Override
    protected void findViews() {
        super.findViews();
        tagsContainer = (FlexboxLayout) findViewById(R.id.tags_container);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        sampleBackground = (ImageView) findViewById(R.id.sample_background);
        dimmer = findViewById(R.id.dimmer);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text_main);
    }

    private void initView() {
        Tag t1 = RestaurantRepository.makeTag("هندی۴۵");
        Tag t4 = RestaurantRepository.makeTag("هندی۵۱");
        Tag t2 = RestaurantRepository.makeTag("هندی۵۲");
        Tag t3 = RestaurantRepository.makeTag("هندی۵۳");

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(RestaurantRepository.makeTag("هندی"));
        tags.add(t1);
        tags.add(RestaurantRepository.makeTag("هندی۲"));
        tags.add(t3);
        tags.add(RestaurantRepository.makeTag("هندی۳"));
        tags.add(RestaurantRepository.makeTag("هندی۴"));
        tags.add(t2);
        tags.add(t4);

//        ArrayList<Tag> selected = new ArrayList<>();
        selectedTags.add(t1);
//        selected.add(t2);
//        selected.add(t3);
//        selected.add(t4);

        allTags = tags;
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
    }

    protected void renderSelectedTags() {
        tagsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < selectedTags.size(); i++)
            tagsContainer.addView(createView(inflater, i));

        if (selectedTags.size() != allTags.size()) {
            View view = inflater.inflate(R.layout.item_add_tag, tagsContainer, false);
            view.setOnClickListener(this::handleAddTagButtonClick);
            tagsContainer.addView(view);
        }

        collapsingToolbarLayout.invalidate();
        collapsingToolbarLayout.requestLayout();
        updateAppbarBackgroundDimensions(tagsContainer.getHeight());
    }

    protected View createView(LayoutInflater inflater, int position) {
        Tag tag = selectedTags.get(position);

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
        for (Tag tag : selectedTags) {
            builder.append(prefix);
            prefix = "، ";
            builder.append(tag.getName());
        }

        toolbarTitle.setText(builder.toString());
    }

    protected void handleAddTagButtonClick(View v) {
        new TagSelectDialog(this, allTags, selectedTags)
                .setOnTagSelectListener(this::addNewTag)
                .show();
    }

    protected void handleRemoveTagClick(View v) {
        try {
            int index = (int) v.getTag();
            selectedTags.remove(index);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        renderSelectedTags();
        renderSelectedTagsToTitle();
    }

    private void addNewTag(Tag tag) {
        selectedTags.add(tag);
        renderSelectedTags();
        renderSelectedTagsToTitle();
    }

    protected void updateAppbarBackgroundDimensions(int oldHeight) {
        tagsContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tagsContainer.getHeight() != oldHeight) {
                    ViewUtils.removeOnGlobalLayoutListener(tagsContainer.getViewTreeObserver(), this);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sampleBackground.getLayoutParams();
                    params.height = tagsContainer.getHeight() + ViewUtils.px(TagViewActivity.this, 36.5f);

                    sampleBackground.setLayoutParams(params);
                    dimmer.setLayoutParams(params);
                }
            }
        });
    }

    @Override
    public void showSomeDummy() {

    }
}
