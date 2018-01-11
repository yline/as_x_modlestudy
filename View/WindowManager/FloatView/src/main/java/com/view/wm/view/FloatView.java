package com.view.wm.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.view.wm.R;
import com.yline.utils.UIScreenUtil;

/**
 * 悬浮框内容
 *
 * @author yline 2018/1/11 -- 12:01
 * @version 1.0.0
 */
public class FloatView extends RelativeLayout {
    private int mAvatarSize = 150;

    private CircleImageView mImageView;

    private View.OnClickListener onViewClickListener;
    private OnUpdatePositionCallback onUpdatePositionCallback;

    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_float_wm, this, true);

        mAvatarSize = UIScreenUtil.dp2px(context, 50);
        mImageView = (CircleImageView) findViewById(R.id.view_float_circle);

        setOnTouchListener(new OnTouchListener() {
            float mTouchStartX, mTouchStartY;
            float mStartX, mStartY;
            float mLastX, mLastY;
            float x, y;
            int top;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect frame = new Rect();
                v.getWindowVisibleDisplayFrame(frame);
                top = frame.top;
                x = event.getRawX();
                y = event.getRawY() - top;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        mStartX = x;
                        mStartY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 更新浮动窗口位置参数
                        mLastX = (int) (x - mTouchStartX);
                        mLastY = (int) (y - mTouchStartY);
                        if (null != onUpdatePositionCallback) {
                            onUpdatePositionCallback.onUpdatePosition(v, mLastX, mLastY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // 更新浮动窗口位置参数
                        mLastX = (int) (x - mTouchStartX);
                        mLastY = (int) (y - mTouchStartY);
                        if (null != onUpdatePositionCallback) {
                            onUpdatePositionCallback.onUpdatePosition(v, mLastX, mLastY);
                        }
                        mTouchStartX = mTouchStartY = 0;
                        if (Math.abs(x - mStartX) < 10 && Math.abs(y - mStartY) < 10) {
                            if (null != onViewClickListener) {
                                onViewClickListener.onClick(v);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setOnViewClickListener(OnClickListener onClickListener) {
        this.onViewClickListener = onClickListener;
    }

    public void setOnUpdatePositionCallback(OnUpdatePositionCallback onUpdatePositionCallback) {
        this.onUpdatePositionCallback = onUpdatePositionCallback;
    }

    public void setImageResource(int imageResource) {
        if (null != mImageView) {
            ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
            if (null == layoutParams) {
                layoutParams = new ViewGroup.LayoutParams(mAvatarSize, mAvatarSize);
            }else {
                layoutParams.width = mAvatarSize;
                layoutParams.height = mAvatarSize;
            }

            mImageView.setLayoutParams(layoutParams);
            mImageView.setImageResource(imageResource);
        }
    }

    public interface OnUpdatePositionCallback {
        /**
         * @param view 控件
         * @param newX 新的X坐标
         * @param newY 新的Y坐标
         */
        void onUpdatePosition(View view, float newX, float newY);
    }
}
