package com.view.popupwindow.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.view.popupwindow.R;
import com.yline.view.recycler.holder.ViewHolder;

/**
 * 顶部，列表
 *
 * @author yline 2018/2/7 -- 10:36
 * @version 1.0.0
 */
public class TopMenuWidget {
    private PopupWindow mPopupWindow;
    private ViewHolder mContentHolder;

    public TopMenuWidget(Context context) {
        initContentView(context);
    }

    private void initContentView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_menu_top, null);
        mContentHolder = new ViewHolder(contentView);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initViewClick();
    }

    private void initViewClick() {
        mContentHolder.get(R.id.view_menu_top_tv).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mContentHolder.get(R.id.view_menu_top_tv).getHeight() != 0) {
                    translate(mContentHolder.get(R.id.view_menu_top_tv));
                }
            }
        });

        mContentHolder.setOnClickListener(R.id.view_menu_top_mask, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void translate(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    public void showAsDropDown(View anchor, String content) {
        if (null != mPopupWindow && !mPopupWindow.isShowing()) {
            TextView textView = mContentHolder.get(R.id.view_menu_top_tv);
            textView.setText(content);
            mPopupWindow.showAsDropDown(anchor);
        }
    }

    public void dismiss() {
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}
