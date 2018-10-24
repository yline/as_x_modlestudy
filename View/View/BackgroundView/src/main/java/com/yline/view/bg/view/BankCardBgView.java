package com.yline.view.bg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.yline.utils.UIScreenUtil;
import com.yline.view.bg.R;

public class BankCardBgView extends RelativeLayout {
	private final int defaultColor;
	private final int startAlpha;
	private final int endAlpha;
	
	private final int defaultRadius;
	private RectF bgRectF;
	
	private Bitmap sourceBitmap;
	
	private Paint paint;
	private LinearGradient linearGradient;
	
	public BankCardBgView(Context context) {
		this(context, null);
	}
	
	public BankCardBgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		defaultColor = ContextCompat.getColor(context, R.color.design_blue);
		
		startAlpha = 0xff; // 不透明
		endAlpha = 0x80; // 半透明
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		
		defaultRadius = UIScreenUtil.dp2px(context, 5);
		
		bgRectF = new RectF();
		setWillNotDraw(false);
		
		// 需要等界面绘制完毕，再调用一次
		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				notifyDataChanged();
			}
		});
	}
	
	public void setBitmapResource(int resource) {
		setBitmap(BitmapFactory.decodeResource(getResources(), resource));
	}
	
	public void setBitmap(Bitmap bitmap) {
		sourceBitmap = bitmap;
		notifyDataChanged();
	}
	
	/**
	 * 更新UI
	 */
	private void notifyDataChanged() {
		Bitmap bitmap = sourceBitmap;
		if (null == bitmap || bitmap.isRecycled()) {
			return;
		}
		
		Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
			@Override
			public void onGenerated(@NonNull Palette palette) {
				int vibrantColor = palette.getDominantColor(defaultColor); // 返回主要颜色
				
				int red = Color.red(vibrantColor);
				int green = Color.green(vibrantColor);
				int blue = Color.blue(vibrantColor);
				
				int startColor = Color.argb(startAlpha, red, green, blue);
				int endColor = Color.argb(endAlpha, red, green, blue);
				
				linearGradient = new LinearGradient(0, getHeight(), getWidth(), 0, startColor, endColor, Shader.TileMode.CLAMP);
				paint.setShader(linearGradient);
				
				invalidate();
			}
		});
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = getWidth();
		int height = getHeight();
		
		// 绘制背景圆圈 + 渐变颜色
		bgRectF.set(0, 0, width, height);
		canvas.drawRoundRect(bgRectF, defaultRadius, defaultRadius, paint);
	}
}





