package io.yon.android.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.yon.android.R;
import io.yon.android.model.RecommendationList;
import io.yon.android.util.RxBus;
import io.yon.android.view.adapter.Adapter;
import io.yon.android.view.widget.snapHelper.GravitySnapHelper;

/**
 * Created by amirhosein on 7/22/17.
 */

public class CompactRecommendationsViewHolder extends ViewHolder<List<RecommendationList>> {

    private static final int ANIMATION_DURATION = 150;

    private Adapter<RecommendationList, ItemRecommendationViewHolder> adapter;
    private TextView tabLeft, tabRight;

    private int lastTabPosition = 0;
    private float xTranslationAmount;

    public static Factory<CompactRecommendationsViewHolder> getFactory() {
        return (inflater, parent, context, bus) -> new CompactRecommendationsViewHolder(
                inflater.inflate(R.layout.compact_recommendations, parent, false),
                context,
                bus
        );
    }

    public CompactRecommendationsViewHolder(View itemView, Context context, RxBus bus) {
        super(itemView, context, bus);
        xTranslationAmount = context.getResources().getDimension(R.dimen.recommendation_tab_translation_x);
    }

    @Override
    protected void findViews() {
        tabLeft = (TextView) findViewById(R.id.tab_title1);
        tabRight = (TextView) findViewById(R.id.tab_title2);
    }

    @Override
    protected void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setInitialPrefetchItemCount(3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter<>(getContext(), null, getBus(), ItemRecommendationViewHolder.getFactory());
        recyclerView.setAdapter(adapter);

        new GravitySnapHelper(Gravity.END, false, this::handleTabChange).attachToRecyclerView(recyclerView);
    }

    @Override
    public void bindContent(List<RecommendationList> lst) {
        adapter.setDataAndNotify(lst);
        handleTabChange(0);
    }

    protected void handleTabChange(int newTabPosition) {
        Log.d("s", String.valueOf(newTabPosition));

        if (newTabPosition > lastTabPosition)
            tabNext(newTabPosition);
        else if (newTabPosition < lastTabPosition)
            tabPrevious(newTabPosition);
        else if (lastTabPosition == 0 && newTabPosition == 0)
            tabNext(0, true);

        lastTabPosition = newTabPosition;
    }

    private void tabNext(int tabPosition) {
        tabNext(tabPosition, false);
    }

    private void tabNext(int tabPosition, boolean noAnim) {
        tabLeft.setText(adapter.getData().get(tabPosition).getTitle());
        tabRight.setText(adapter.getData().get(lastTabPosition).getTitle());

        tabLeft.setX(-xTranslationAmount);
        tabLeft.setAlpha(0f);

        tabRight.setX(0f);
        tabRight.setAlpha(1f);

        if (noAnim)
            return;

        tabRight.animate()
                .translationX(xTranslationAmount)
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        tabLeft.animate()
                .translationX(0)
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    private void tabPrevious(int tabPosition) {
        tabRight.setText(adapter.getData().get(tabPosition).getTitle());
        tabLeft.setText(adapter.getData().get(lastTabPosition).getTitle());

        tabRight.setX(xTranslationAmount);
        tabRight.setAlpha(0f);

        tabLeft.setX(0f);
        tabLeft.setAlpha(1f);


        tabLeft.animate()
                .translationX(-xTranslationAmount)
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        tabRight.animate()
                .translationX(0)
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }
}
