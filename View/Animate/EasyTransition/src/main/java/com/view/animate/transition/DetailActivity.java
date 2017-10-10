package com.view.animate.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // pre init some views and data
        initViews();

        // if re-initialized, do not play any anim
        long transitionDuration = 800;
        if (null != savedInstanceState) {
            transitionDuration = 0;
        }

        EasyTransition.enter(this, transitionDuration, new DecelerateInterpolator(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    private void initViews() {
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void onBackPressed() {
        EasyTransition.exit(DetailActivity.this, 800, new DecelerateInterpolator());
    }
}
