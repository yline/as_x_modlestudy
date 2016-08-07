package com.dm.command.activity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.design.pattern.command.draw.R;
import com.dm.command.brush.CircleBrush;
import com.dm.command.brush.IBrush;
import com.dm.command.brush.NormalBrush;
import com.dm.command.command.drawer.DrawPath;
import com.dm.command.receiver.DrawCanvas;
import com.yline.base.BaseActivity;

/**
 * 命令模式:
 * 将一个请求封装成一个对象(DrawPath),从而让用户使用不同的请求把客户端参数化;
 * 对请求排队或者记录请求日志,以及支持可撤销的操作
 * 优点:
 * 耦合性低、控制性好、扩展性强
 * 缺点:
 * 类膨胀、实时性有时并不是特别好
 * 该工程分析:
 * package com.dm.command.activity;     Activity入口
 * package com.dm.command.invoker;      invoker命令分发、记录
 * package com.dm.command.command.drawer;   command命令整合、实现
 * package com.dm.command.receiver;     命令接收者,只是这个工程中,它集成了invoker,因此又设计到了装饰模式
 * package com.dm.command.brush;        不属于这个工程的设计模式:Brush类包,采用的是策略模式
 */
@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends BaseActivity implements View.OnClickListener
{
	private DrawCanvas mCanvas; // 绘制画布

	private DrawPath mPath; // 路径绘制命令

	private Paint mPaint; // 画笔对象

	private IBrush mBrush; // 笔触对象

	private Button btnRedo, btnUndo; // 重做、撤销按钮

	private Button btnRed, btnGreen, btnBlue, btnNormal, btnCircle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		mCanvas = (DrawCanvas) findViewById(R.id.drawcanvas);

		btnRedo = (Button) findViewById(R.id.btn_redo);
		btnUndo = (Button) findViewById(R.id.btn_undo);

		btnRed = (Button) findViewById(R.id.btn_red);
		btnGreen = (Button) findViewById(R.id.btn_green);
		btnBlue = (Button) findViewById(R.id.btn_blue);

		btnNormal = (Button) findViewById(R.id.btn_normal);
		btnCircle = (Button) findViewById(R.id.btn_circle);
	}

	private void initData()
	{
		btnRedo.setEnabled(false);
		btnUndo.setEnabled(false);

		mPaint = new Paint();
		mPaint.setColor(0xFFFFFFFF);
		mPaint.setStrokeWidth(3);

		mBrush = new NormalBrush();

		btnRedo.setOnClickListener(this);
		btnUndo.setOnClickListener(this);

		btnRed.setOnClickListener(this);
		btnGreen.setOnClickListener(this);
		btnBlue.setOnClickListener(this);

		btnNormal.setOnClickListener(this);
		btnCircle.setOnClickListener(this);

		mCanvas.setOnTouchListener(new DrawTouchListener());
	}

	private class DrawTouchListener implements View.OnTouchListener
	{
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					mPath = new DrawPath();
					mPath.setPaint(mPaint);
					mPath.setPath(new Path());
					mBrush.down(mPath.getPath(), event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					mBrush.move(mPath.getPath(), event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					mBrush.up(mPath.getPath(), event.getX(), event.getY());

					mCanvas.add(mPath);
					mCanvas.isDrawing = true; // 设置为true之后,才能够执行命令
					btnUndo.setEnabled(true);
					btnRedo.setEnabled(false);
					break;
			}
			return true;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_red:
				mPaint = new Paint();
				mPaint.setStrokeWidth(3);
				mPaint.setColor(0xFFFF0000);
				break;
			case R.id.btn_green:
				mPaint = new Paint();
				mPaint.setStrokeWidth(3);
				mPaint.setColor(0xFF00FF00);
				break;
			case R.id.btn_blue:
				mPaint = new Paint();
				mPaint.setStrokeWidth(3);
				mPaint.setColor(0xFF0000FF);
				break;

			case R.id.btn_normal:
				mBrush = new NormalBrush();
				break;
			case R.id.btn_circle:
				mBrush = new CircleBrush();
				break;

			case R.id.btn_undo:
				mCanvas.undo();
				if (!mCanvas.canUndo())
				{
					btnUndo.setEnabled(false);
				}
				btnRedo.setEnabled(true);
				break;
			case R.id.btn_redo:
				mCanvas.redo();
				if (!mCanvas.canUndo())
				{
					btnRedo.setEnabled(false);
				}
				btnUndo.setEnabled(true);
				break;
		}
	}
}
