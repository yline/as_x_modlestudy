package com.view.vscroll.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.view.vscroll.R;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * 垂直滚动的TextView
 *
 * @author yline 2018/1/19 -- 14:40
 * @version 1.0.0
 */
public class VerticalScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final int DURATION_ANIM = 800; // 执行动画的时间
    public static final int DURATION_SPEED = 3000; // 动画的间隔时间

    // mInAnimation,mOutUp分别构成向下翻页的进出动画
    private Rotate3dAnimation mInAnimation;
    private Rotate3dAnimation mOutAnimation;
    private OnTextClickListener mOnTextClickListener;

    private List<String> mScrollList;
    private int mScrollNumber;
    private VerticalScrollHandler mVScrollHandler;

    public VerticalScrollTextView(Context context) {
        this(context, null);
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        mScrollNumber = 0;
        mScrollList = Arrays.asList(getResources().getStringArray(R.array.scroll_text));
    }

    // 初始化
    private void init() {
        setFactory(this);

        mInAnimation = createAnimation(true);
        mOutAnimation = createAnimation(false);

        setInAnimation(mInAnimation); // 当View显示时动画资源ID
        setOutAnimation(mOutAnimation); // 当View隐藏是动画资源ID

        mVScrollHandler = new VerticalScrollHandler(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnTextClickListener) {
                    String textStr = mScrollList.get((mScrollNumber - 1) % (mScrollList.size()));
                    Log.v("xxx-", "text = " + textStr);
                    mOnTextClickListener.onTextClick(v, textStr);
                }
            }
        });
    }

    /**
     * 开启滚动
     */
    public void startAnimation() {
        mVScrollHandler.sendEmptyMessage(VerticalScrollHandler.CODE_START);
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        mVScrollHandler.sendEmptyMessage(VerticalScrollTextView.DURATION_SPEED);
        if (null != mVScrollHandler) {
            mVScrollHandler.removeCallbacks(null);
        }
    }

    public void setOnTextClickListener(OnTextClickListener listener) {
        this.mOnTextClickListener = listener;
    }

    /**
     * 创建Rotate3dAnimation
     */
    private Rotate3dAnimation createAnimation(boolean turnIn) {
        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn);
        rotation.setDuration(DURATION_ANIM); // 执行动画的时间
        rotation.setFillAfter(false); // 是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator()); // 设置加速模式
        return rotation;
    }

    // 这里返回的TextView，就是我们看到的View,可以设置自己想要的效果
    @Override
    public View makeView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.textview_item_vertical_scroll, null);
    }

    /**
     * 滚动到下一个
     */
    private void scrollToNext() {
        //显示动画
        if (getInAnimation() != mInAnimation) {
            setInAnimation(mInAnimation);
        }

        //隐藏动画
        if (getOutAnimation() != mOutAnimation) {
            setOutAnimation(mOutAnimation);
        }
    }

    private void setText() {
        String textStr = mScrollList.get(mScrollNumber % (mScrollList.size()));
        setText(textStr);

        mScrollNumber++;
    }

    public interface OnTextClickListener {
        /**
         * 点击View
         *
         * @param view 点击到的控件
         * @param text 点击的内容
         */
        void onTextClick(View view, String text);
    }

    private static class VerticalScrollHandler extends Handler {
        private static final int CODE_START = 50;
        private static final int CODE_SCROLL = 100;
        private static final int CODE_STOP = 200;

        private boolean mIsContinueScroll;
        private final WeakReference<VerticalScrollTextView> mVScrollTvReference;

        private VerticalScrollHandler(VerticalScrollTextView textView) {
            this.mIsContinueScroll = false;
            this.mVScrollTvReference = new WeakReference<>(textView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            VerticalScrollTextView vScrollTextView = mVScrollTvReference.get();
            if (msg.what == CODE_START) {
                mIsContinueScroll = true;

                vScrollTextView.setText();
                sendEmptyMessageDelayed(CODE_SCROLL, VerticalScrollTextView.DURATION_SPEED);
            } else if (msg.what == CODE_SCROLL) {
                vScrollTextView.scrollToNext();
                vScrollTextView.setText();
                if (mIsContinueScroll) {
                    sendEmptyMessageDelayed(CODE_SCROLL, VerticalScrollTextView.DURATION_SPEED);
                }
            } else {
                mIsContinueScroll = false;
            }
        }
    }

    /**
     * 负责视图的，滚动
     */
    private class Rotate3dAnimation extends Animation {
        private final Camera mCamera;// 用来保存初始Camera
        private final boolean mIsTurnIn; // 移动方向
        private final float mCenterX; // Camara X的移动量
        private float mCenterY; // Camara Y的移动量

        /**
         * @param isTurnIn 是否是，向上滚动
         */
        Rotate3dAnimation(boolean isTurnIn) {
            this.mIsTurnIn = isTurnIn;
            this.mCamera = new Camera();
            this.mCenterX = 0;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);

            // 用来记录，初始Camera 高度 宽度
            mCenterY = getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float centerY = mCenterY;
            // 获取转换矩阵
            final Matrix matrix = t.getMatrix();

            mCamera.save();
            if (mIsTurnIn) {
                // 进入时候，0 -> (derection * mCenterY * (interpolatedTime - 1.0f))[负值] -> 0
                mCamera.translate(0.0f, mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                // 退出时候，0 -> (derection * mCenterY * (interpolatedTime - 1.0f))[正值] -> 0
                mCamera.translate(0.0f, mCenterY * (interpolatedTime), 0.0f);
            }
            mCamera.getMatrix(matrix);
            mCamera.restore();

            matrix.preTranslate(-mCenterX, -centerY);
            matrix.postTranslate(mCenterX, centerY);
        }
    }
}
