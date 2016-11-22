package com.listview.eye.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.listview.eye.R;

public class EyeView extends ImageView
{

	private Paint mPaint;

	private float backgroundprogress;

	private boolean isAnimate;

	private int rotateProgress;

	private Handler mHandler = new Handler();

	// 以下与 绘制Bitmap相关
	private Bitmap originBitmap1, originBitmap2;

	private float scale;

	private int maxWidth, maxHeight;

	private int minWidth, minHeight;

	public EyeView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		rotateProgress = 0;
		backgroundprogress = 0.0f;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		initOriginBitmap();
	}

	private void initOriginBitmap()
	{
		originBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.eye_gray_1);
		originBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.eye_gray_2);

		scale = (float) originBitmap1.getWidth() / (float) getWidth();
		maxWidth = (int) (originBitmap1.getWidth() / scale);
		maxHeight = (int) (originBitmap1.getHeight() / scale);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		minWidth = (int) (this.getWidth() * backgroundprogress);
		minHeight = (int) (this.getHeight() * backgroundprogress);

		if (minWidth > 1 && minHeight > 1)
		{
			Bitmap bitmap = getBitmap();
			canvas.drawBitmap(bitmap, 0, 0, null);
			bitmap.recycle();
		}
	}

	/**
	 * 更新 融合后的Bitmap图片
	 * @return
	 */
	public Bitmap getBitmap()
	{
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		int maskSize = 1;
		if (backgroundprogress > 0.3f)
		{
			maskSize = (int) (maxHeight * (backgroundprogress - 0.3) / 0.7);
		}

		Canvas canvas = new Canvas();
		Bitmap resultBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);

		// background create
		Bitmap tempBitmap1 = Bitmap.createScaledBitmap(originBitmap1, (int) (maxWidth), (int) (maxHeight), true);

		// mask,circle,draw
		Bitmap maskBitmap = Bitmap.createBitmap(tempBitmap1.getWidth(), tempBitmap1.getWidth(), Config.ARGB_8888);
		canvas.setBitmap(maskBitmap);
		canvas.drawCircle(maskBitmap.getWidth() / 2, maskBitmap.getHeight() / 2, maskSize, mPaint);

		// background draw
		canvas.setBitmap(resultBitmap);
		canvas.drawBitmap(tempBitmap1,
				(getWidth() - tempBitmap1.getWidth()) / 2,
				(getHeight() - tempBitmap1.getHeight()) / 2,
				paint);

		// xFermode mask and background
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawBitmap(maskBitmap,
				(getWidth() - maskBitmap.getWidth()) / 2,
				(getHeight() - maskBitmap.getHeight()) / 2,
				paint);
		paint.setXfermode(null);

		// eye
		float scaleProgress = backgroundprogress / 0.3f;
		scaleProgress = scaleProgress > 1.0f ? 1.0f : scaleProgress;

		Bitmap tempBitmap2 =
				Bitmap.createScaledBitmap(originBitmap2,
						(int) (maxWidth * scaleProgress),
						(int) (maxHeight * scaleProgress),
						true);
		Matrix matrix = new Matrix();
		matrix.postRotate(rotateProgress);
		tempBitmap2 =
				Bitmap.createBitmap(tempBitmap2, 0, 0, tempBitmap2.getWidth(), tempBitmap2.getHeight(), matrix, false);
		canvas.drawBitmap(tempBitmap2,
				(getWidth() - tempBitmap2.getWidth()) / 2,
				(getHeight() - tempBitmap2.getHeight()) / 2,
				paint);

		tempBitmap1.recycle();
		tempBitmap2.recycle();
		maskBitmap.recycle();
		return resultBitmap;
	}

	public void setProgress(float progress)
	{
		this.backgroundprogress = progress;
		this.invalidate();
	}

	public void startAnimate()
	{
		if (!isAnimate)
		{
			isAnimate = true;
			mHandler.post(mRunnable);
		}
	}

	public void stopAnimate()
	{
		isAnimate = false;
		mHandler.removeCallbacks(mRunnable);
		rotateProgress = 0;
	}

	public Runnable mRunnable = new Runnable()
	{

		@Override
		public void run()
		{
			rotateProgress += 10;
			if (rotateProgress > 360)
			{
				rotateProgress = 0;
			}

			if (isAnimate)
			{
				mHandler.postDelayed(this, 10);
			}
			EyeView.this.invalidate();
		}
	};

}
