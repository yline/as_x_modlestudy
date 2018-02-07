package com.view.popupwindow.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.view.popupwindow.R;

import java.util.List;

/**
 * 长按删除，弹框;
 * 弹框位置，未完善
 *
 * @author yline 2017/7/4 -- 16:41
 * @version 1.0.0
 */
public class DeleteMenuWidget {
    private PopupWindow popupWindow;

    private ViewGroup popupViewGroup;

    private Context sContext;

    private OnWidgetListener onWidgetListener;

    private float lastX = 0, lastY = 0;

    public DeleteMenuWidget(Context context) {
        this.sContext = context;
        initPopupWindow(context);
    }

    private void initPopupWindow(Context context) {
        if (null == popupWindow) {
            popupViewGroup = new LinearLayout(context);
            popupViewGroup.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            popupWindow = new PopupWindow(popupViewGroup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            // 设置点击外部,就可以消除
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(true);
        }
    }

    public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener listener) {
        popupWindow.setOnDismissListener(listener);
    }

    /**
     * @param dataList
     * @param anchor   the view on which to pin the popup window
     */
    public void showAsDropDown(final List<String> dataList, View anchor) {
        showAsDropDown(dataList, anchor, 0, 0);
    }

    /**
     * @param dataList
     * @param anchor   the view on which to pin the popup window
     * @param xOff     A horizontal offset from the anchor in pixels
     * @param yOff     A vertical offset from the anchor in pixels
     */
    public void showAsDropDown(final List<String> dataList, View anchor, int xOff, int yOff) {
        if (null != popupWindow) {
            popupViewGroup.removeAllViews(); // 清除之前的界面

            ListView contentListView = new ListView(sContext);
            contentListView.setAdapter(new ArrayAdapter<>(sContext, getXItemId(), android.R.id.text1, dataList));
            contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != onWidgetListener) {
                        onWidgetListener.onOptionSelected(view, position, dataList.get(position));
                    }
                    popupWindow.dismiss();
                }
            });
            popupViewGroup.addView(contentListView);

            if (!popupViewGroup.isShown()) {
                popupWindow.showAsDropDown(anchor, xOff, yOff);
            }
        }
    }

    public void showAtLocation(final List<String> dataList, final View parent) {
        // 获取目标view在屏幕中的
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAtLocation(dataList, parent, Gravity.LEFT | Gravity.TOP, (int) lastX, (int) lastY);
                return false;
            }
        });
    }

    /**
     * @param dataList
     * @param parent   a parent view to get the {@link View#getWindowToken()} token from
     * @param gravity  the gravity which controls the placement of the popup window
     * @param x        the popup's x location offset
     * @param y        the popup's y location offset
     */
    public void showAtLocation(final List<String> dataList, View parent, int gravity, int x, int y) {
        if (null != popupWindow) {
            popupViewGroup.removeAllViews(); // 清除之前的界面

            final ListView contentListView = new ListView(sContext) {
                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    int maxChildWidth = measureMaxWidth() + getPaddingLeft() + getPaddingRight();
                    super.onMeasure(MeasureSpec.makeMeasureSpec(maxChildWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
                }

                public int measureMaxWidth() {
                    int maxWidth = 0;
                    View view = null;
                    for (int i = 0; i < getAdapter().getCount(); i++) {
                        view = getAdapter().getView(i, view, this);
                        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                        if (view.getMeasuredWidth() > maxWidth) {
                            maxWidth = view.getMeasuredWidth();
                        }
                    }
                    return maxWidth;
                }
            };
            contentListView.setDividerHeight(0);
            contentListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contentListView.setAdapter(new ArrayAdapter<>(sContext, getXItemId(), android.R.id.text1, dataList));
            contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != onWidgetListener) {
                        onWidgetListener.onOptionSelected(view, position, dataList.get(position));
                    }
                    popupWindow.dismiss();
                }
            });
            popupViewGroup.addView(contentListView);

            if (!popupViewGroup.isShown()) {
                popupWindow.showAtLocation(parent, gravity, x, y);
            }
        }
    }

    public void setOnWidgetListener(OnWidgetListener onWidgetListener) {
        this.onWidgetListener = onWidgetListener;
    }

    /* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 重写 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
    protected int getXItemId() {
        return R.layout.widget_menu_delete;
    }

    public interface OnWidgetListener {
        /**
         * 选择某一项
         *
         * @param view     被选择的view
         * @param position 位置
         * @param content  内容
         */
        void onOptionSelected(View view, int position, String content);
    }
}
