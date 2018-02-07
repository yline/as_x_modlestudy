package com.view.popupwindow.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.view.popupwindow.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现下拉菜单 的基础工具类
 * 如果需要改变字体颜色，则重写item 资源文件
 *
 * @author yline 2017/3/7 -- 15:33
 * @version 1.0.0
 */
public class DropMenuWidget {
    // 默认位置
    private static final int CONTENT_VIEW_POSITION = 0;

    // 承载物
    private PopupWindow popupWindow;

    // window对应的view
    private LinearLayout popupView;

    // popupView 的两个内容
    private View contentView, maskView;

    // tab view
    private List<View> tabViewList;

    // content view
    private List<View> contentViewList;

    private boolean isOpened = false;

    private Context sContext;

    private TabLayout sTabLayout;

    // 进行高度适配
    private int maxContentHeight;

    public DropMenuWidget(Context context, TabLayout tabLayout) {
        this.sContext = context;
        this.sTabLayout = tabLayout;
    }

    public void show(List<String> header, List<View> viewList) {
        if (header.size() != viewList.size()) {
            throw new IllegalArgumentException("params not match, header.size() should be equal popupViews.size()");
        }

        setDropDownMenu(sContext, header, viewList);
    }

    public void updateTitle(int index, String title) {
        if (index >= sTabLayout.getTabCount()) {
            Log.e("TabLayout", "index = " + index + ", TabCount = " + sTabLayout.getChildCount());
            throw new IllegalArgumentException("index out of range");
        }

        View tabView = sTabLayout.getTabAt(index).getCustomView();
        TextView textView = (TextView) tabView.findViewById(R.id.tv_drop_down_menu);
        textView.setText(title);
    }

    public boolean isOpened() {
        return isOpened;
    }

    /**
     * 关闭Menu
     */
    public void closeMenu() {
        closeMenu(sTabLayout.getSelectedTabPosition());
    }

    /**
     * 关闭Menu,并更新状态
     *
     * @param position 更新状态位置
     */
    public void closeMenu(int position) {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();

            tabViewList.get(position).setSelected(false);
            isOpened = false;
        }
    }

    /**
     * @param context  上下文
     * @param header   标题信息
     * @param viewList pop 内容
     */
    private void setDropDownMenu(final Context context, List<String> header, List<View> viewList) {
        initTabView(context, sTabLayout, header);

        initPopupWindow(context);

        initContentViewList(viewList);
    }

    /**
     * 初始化Header
     *
     * @param context
     * @param tabLayout
     */
    private void initTabView(final Context context, final TabLayout tabLayout, List<String> header) {
        this.tabViewList = new ArrayList<>();

        for (int i = 0; i < header.size(); i++) {
            View tabView = addTab(context, header.get(i));
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));

            tabView.setSelected(false);
            tabViewList.add(tabView);
        }
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                addMenu(tabLayout, tab.getPosition());
            }

            // 该方法比上面那个方法先执行
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // close menu
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (isOpened) {
                    closeMenu(tab.getPosition());
                } else {
                    addMenu(tabLayout, tab.getPosition());
                }
            }
        });
    }

    public void addOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener listener) {
        this.sTabLayout.addOnTabSelectedListener(listener);
    }

    /**
     * 添加 tab
     *
     * @param context
     * @param text
     * @return
     */
    private View addTab(Context context, String text) {
        View tabView = LayoutInflater.from(context).inflate(getXItemId(), null);

        TextView textView = (TextView) tabView.findViewById(R.id.tv_drop_down_menu);
        textView.setText(text);
        return tabView;
    }

    private void initPopupWindow(Context context) {
        if (null == popupWindow) {
            popupView = new LinearLayout(context);
            popupView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupView.setOrientation(LinearLayout.VERTICAL);

            maskView = new View(context);
            maskView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            maskView.setBackgroundColor(getXMaskColor());
            maskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });

            contentView = new View(context);

            popupView.addView(contentView);
            popupView.addView(maskView);

            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 添加 menu
     *
     * @param tabLayout
     * @param position
     */
    private void addMenu(final TabLayout tabLayout, int position) {
        if (0 == maxContentHeight) {
            this.maxContentHeight = getScreenHeight(sContext) - getStatusHeight(sContext) - sTabLayout.getBottom();
        }

        if (null != popupWindow) {
            contentView = contentViewList.get(position);
            popupView.removeViewAt(CONTENT_VIEW_POSITION);
            popupView.addView(contentView, CONTENT_VIEW_POSITION);
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (contentView.getHeight() > maxContentHeight) {
                        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                        layoutParams.height = maxContentHeight;
                        contentView.setLayoutParams(layoutParams);
                    }
                }
            });

            tabViewList.get(position).setSelected(true);
            isOpened = true;

            if (!popupView.isShown()) {
                popupWindow.showAsDropDown(tabLayout);
            }
        }
    }

    /**
     * 初始化 contentView
     *
     * @param list
     */
    private void initContentViewList(List<View> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        this.contentViewList = list;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            Log.e("TAG", "ScreenUtil -> getStatusHeight Exception", e);
        }
        return statusHeight;
    }

    // 接下来为设置参数
    public int getXMaskColor() {
        return 0x88888888;
    }

    protected int getXItemId() {
        return R.layout.widget_menu_drop;
    }
}



