package com.view.popupwindow.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

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
        mContentHolder.setOnClickListener(R.id.view_menu_top_mask, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showAsDropDown(View anchor, String content) {
        if (null != mPopupWindow && !mPopupWindow.isShowing()) {
            mContentHolder.setText(R.id.view_menu_top_tv, content);
            mPopupWindow.showAsDropDown(anchor);
        }
    }

    public void dismiss() {
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}
