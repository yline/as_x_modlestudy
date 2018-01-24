package com.view.recycler.floats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.decoration.CommonLinearDecoration;

import java.util.HashMap;
import java.util.Map;

public class FloatLinearItemDecoration extends CommonLinearDecoration {
    private Map<Integer, String> keys = new HashMap<>();

    private Context sContext;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;

    public FloatLinearItemDecoration(Context context) {
        super(context);

        this.sContext = context;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        initTextPaint(mTextPaint);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        initBackgroundPaint(mBackgroundPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!isShowHeadFloating()) {
            return;
        }

        int firstVisiblePos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        int realPosition = firstVisiblePos - getHeadNumber();
        if (realPosition == RecyclerView.NO_POSITION) {
            return;
        }

        String title = getTitle(realPosition);
        if (TextUtils.isEmpty(title)) {
            return;
        }

        boolean flag = false;
        String nextTitle = getTitle(realPosition + 1);
        if (!TextUtils.isEmpty(nextTitle) && !title.equals(nextTitle)) {
            // 说明是当前组最后一个元素，但不一定碰撞了
            View child = parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView;

            if (child.getTop() + child.getMeasuredHeight() < getTitleHeight()) {
                // 进一步检测碰撞
                c.save();// 保存画布当前的状态
                flag = true;
                c.translate(0, child.getTop() + child.getMeasuredHeight() - getTitleHeight());//负的代表向上
            }
        }

        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getBottom() - parent.getPaddingBottom();
        c.drawRect(parentLeft, parentTop, parentRight, parentTop + getTitleHeight(), mBackgroundPaint);
        Rect bgRect = new Rect(parentLeft, parentTop, parentRight, parentTop + getTitleHeight());
        drawVerticalText(c, title, bgRect, mTextPaint);

        if (flag) // 还原画布为初始状态
        {
            c.restore();
        }
    }

    @Override
    protected boolean isDrawDivide(RecyclerView.Adapter adapter, int totalCount, int currentPosition) {
//        return super.isDrawDivide(adapter, totalCount, currentPosition);
        // 头部
        if (getHeadNumber() > currentPosition) {
            return false;
        }

        // 底部
        if (currentPosition > totalCount - 1 - getFootNumber()) {
            return false;
        }

        return true;
    }

    @Override
    protected void setVerticalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
//        super.setVerticalOffsets(outRect, parent, currentPosition);
        currentPosition = currentPosition - getHeadNumber();
        if (keys.containsKey(currentPosition)) {
            outRect.set(0, getTitleHeight(), 0, 0);
        } else {
            outRect.set(0, sDivider.getIntrinsicHeight(), 0, 0);
        }
    }

    @Override
    protected void setHorizontalOffsets(Rect outRect, RecyclerView parent, int currentPosition) {
//        super.setHorizontalOffsets(outRect, parent, currentPosition);
        currentPosition = currentPosition - getHeadNumber();
        if (keys.containsKey(currentPosition)) {
            outRect.set(getTitleHeight(), 0, 0, 0);
        } else {
            outRect.set(sDivider.getIntrinsicWidth(), 0, 0, 0);
        }
    }

    @Override
    protected void drawVerticalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
//        super.drawVerticalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
        currentPosition = currentPosition - getHeadNumber();
        if (keys.containsKey(currentPosition)) {
            c.drawRect(childLeft, childTop - getTitleHeight(), childRight, childTop, mBackgroundPaint);

            Rect bgRect = new Rect(childLeft, childTop - getTitleHeight(), childRight, childTop);
            drawVerticalText(c, keys.get(currentPosition), bgRect, mTextPaint);
        } else {
            sDivider.setBounds(childLeft, childTop - sDivider.getIntrinsicHeight(), childRight, childTop);
            sDivider.draw(c);
        }
    }

    @Override
    protected void drawHorizontalDivider(Canvas c, RecyclerView parent, int currentPosition, int childLeft, int childTop, int childRight, int childBottom) {
//        super.drawHorizontalDivider(c, parent, currentPosition, childLeft, childTop, childRight, childBottom);
        if (keys.containsKey(currentPosition)) {
            c.drawRect(childLeft, childTop, childLeft - sDivider.getIntrinsicWidth(), childBottom, mBackgroundPaint);
            c.drawText(keys.get(currentPosition), childLeft, childTop, mTextPaint);
        } else {
            sDivider.setBounds(childLeft, childTop, childLeft - sDivider.getIntrinsicWidth(), childBottom);
            sDivider.draw(c);
        }
    }

    /**
     * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     *
     * @param position
     * @return
     */
    private String getTitle(int position) {
        while (position >= 0) {
            if (keys.containsKey(position)) {
                return keys.get(position);
            }
            position--;
        }
        return null;
    }

    public void setKeys(Map<Integer, String> keys) {
        this.keys.clear();
        this.keys.putAll(keys);
    }

    public void initTextPaint(Paint textPaint) {
        textPaint.setTextSize(UIScreenUtil.dp2px(sContext, 18));
        textPaint.setColor(Color.BLACK);
    }

    public void initBackgroundPaint(Paint backgroundPaint) {
        backgroundPaint.setColor(0xFFF2F2F2);
    }

    /**
     * 绘制文本
     *
     * @param c
     * @param content
     * @param bgRect
     * @param textPaint
     */
    public void drawVerticalText(Canvas c, String content, Rect bgRect, Paint textPaint) {
        int textLeft = bgRect.left + UIScreenUtil.dp2px(sContext, 14);
        int textBottom = bgRect.centerY() + ((int) textPaint.getTextSize() >> 1);

        c.drawText(content, textLeft, textBottom, textPaint);
    }

    /**
     * 确定头部有几个不绘制分割线
     *
     * @return
     */
    protected int getHeadNumber() {
        return 0;
    }

    /**
     * 确定底部有几个不绘制分割线
     *
     * @return
     */
    protected int getFootNumber() {
        return 0;
    }

    public int getTitleHeight() {
        return UIScreenUtil.dp2px(sContext, 42);
    }

    public boolean isShowHeadFloating() {
        return true;
    }
}
