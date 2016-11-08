package com.device.bluetooth.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.device.bluetooth.BlueToothHelper;
import com.device.bluetooth.ClsUtils;
import com.device.bluetooth.R;
import com.device.bluetooth.receiver.BlueToothReceiver;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

import java.util.Set;

/**
 * Created by yline on 2016/11/7.
 */
public class BluetoothActivity extends BaseAppCompatActivity implements BlueToothReceiver.BluetoothBondCallback
{
	private static final String TAG = BluetoothActivity.class.getSimpleName();

	private static final int REQUEST_ENABLE_CODE = 0;

	private static final int WHAT_HANDLER_CODE = 0;

	private static final String DETECTION_CLOSE_STATE = "仅让已配对的设备检测到";

	private int surplusTime = 0;

	private static final int DEFAULT_SURPLUS_TIME = 120;

	private static final int MAX_SURPLUS_TIME = 3600;

	private String detectionOpenState = "";

	private DeviceAdapter pairedDeviceAdapter, scannedDeviceAdapter;

	private BlueToothHelper blueToothHelper;

	// 最大值 3600
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (surplusTime > 0)
			{
				surplusTime--;
				detectionOpenState = String.format("周围设备检测剩余时间(%02d:%02d)", surplusTime / 60, surplusTime % 60);
				tvDetection.setText(detectionOpenState);
				mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CODE, 1000);
			}
			else
			{
				tvDetection.setText(DETECTION_CLOSE_STATE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);

		initView();
		initData();

		switchBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				LogFileUtil.v(TAG, "switchBluetooth isChecked = " + isChecked);
				if (isChecked)
				{
					blueToothHelper.enable(BluetoothActivity.this, REQUEST_ENABLE_CODE);
				}
				else
				{
					blueToothHelper.disable();

					rlDetection.setVisibility(View.GONE);
					rlDeviceName.setVisibility(View.GONE);
					llDevice.setVisibility(View.GONE);

					btnSearch.setClickable(false);
				}
			}
		});

		switchDetection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				LogFileUtil.v(TAG, "switchDetection isChecked = " + isChecked);
				if (isChecked)
				{
					surplusTime = DEFAULT_SURPLUS_TIME;
					blueToothHelper.resetLimitTime(BluetoothActivity.this, DEFAULT_SURPLUS_TIME);
				}
				else
				{
					// 正确的做法应该是关闭可见性
					surplusTime = 0;
					blueToothHelper.resetLimitTime(BluetoothActivity.this, 0);
				}
				// 清空 输入内容
				etDetectionSet.setText("");
				// 开始倒计时
				mHandler.removeMessages(WHAT_HANDLER_CODE);
				mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CODE, 500);
			}
		});

		btnDetectionSet.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String time = etDetectionSet.getText().toString().trim();
				LogFileUtil.v(TAG, "btnDetectionSet time = " + time);
				if (!TextUtils.isEmpty(time))
				{
					int setTime = Integer.parseInt(time);
					if (setTime > MAX_SURPLUS_TIME)
					{
						surplusTime = DEFAULT_SURPLUS_TIME;
						blueToothHelper.resetLimitTime(BluetoothActivity.this, DEFAULT_SURPLUS_TIME);
					}
					else
					{
						surplusTime = setTime;
						blueToothHelper.resetLimitTime(BluetoothActivity.this, setTime);
					}

					// 清空 输入内容
					etDetectionSet.setText("");
					// 开始倒计时
					mHandler.removeMessages(WHAT_HANDLER_CODE);
					mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CODE, 500);
				}
			}
		});

		btnDeviceChange.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String name = etDeviceName.getText().toString().trim();
				LogFileUtil.v(TAG, "btnDeviceChange name = " + name);
				if (!TextUtils.isEmpty(name))
				{
					blueToothHelper.getBluetoothAdapter().setName(name);
					tvDeviceInfo.setText(blueToothHelper.getBluetoothAdapter().getName() + " - " + blueToothHelper.getBluetoothAdapter().getAddress());
					// 清空输入内容
					etDeviceName.setText("");
				}
			}
		});

		btnSearch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btnSearch");
				blueToothHelper.scanStart(BluetoothActivity.this);
			}
		});

		lvPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// 选择设备,到指定页面,发送信息
			}
		});

		lvPairedDevice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
				builder.setTitle("确认取消配对").setCancelable(true);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						BluetoothDevice device = (BluetoothDevice) pairedDeviceAdapter.getItem(position);
						try
						{
							boolean result = ClsUtils.removeBond(device.getClass(), device);
							LogFileUtil.v(TAG, "PositiveButton result = " + result);
						}
						catch (Exception e)
						{
							LogFileUtil.e(TAG, "lvPairedDevice tvItemDevice removeBond Exception", e);
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						LogFileUtil.v(TAG, "NegativeButton");
					}
				});
				builder.create().show();
				return false;
			}
		});

		lvScannedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				BluetoothDevice device = (BluetoothDevice) scannedDeviceAdapter.getItem(position);
				try
				{
					boolean result = ClsUtils.createBond(device.getClass(), device);
					LogFileUtil.v(TAG, "lvScannedDevice onItemClick result = " + result);
				}
				catch (Exception e)
				{
					LogFileUtil.e(TAG, "lvScannedDevice tvItemDevice createBond Exception", e);
				}
			}
		});
	}

	private RelativeLayout rlBluetooth, rlDetection, rlDeviceName;

	private LinearLayout llDevice, llProgress;

	private Button btnSearch, btnDeviceChange, btnDetectionSet;

	private TextView tvDeviceInfo, tvDetection;

	private EditText etDetectionSet, etDeviceName;

	private Switch switchBluetooth, switchDetection;

	private ListView lvPairedDevice, lvScannedDevice;

	private void initView()
	{
		rlBluetooth = (RelativeLayout) findViewById(R.id.rl_bluetooth);
		switchBluetooth = (Switch) findViewById(R.id.switch_bluetooth);

		rlDetection = (RelativeLayout) findViewById(R.id.rl_detection);
		switchDetection = (Switch) findViewById(R.id.switch_detection);
		etDetectionSet = (EditText) findViewById(R.id.et_detection);
		tvDetection = (TextView) findViewById(R.id.tv_detection);
		btnDetectionSet = (Button) findViewById(R.id.btn_detection_set);

		rlDeviceName = (RelativeLayout) findViewById(R.id.rl_device_name);
		etDeviceName = (EditText) findViewById(R.id.et_device_name);
		tvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
		btnDeviceChange = (Button) findViewById(R.id.btn_device_change);

		llDevice = (LinearLayout) findViewById(R.id.ll_device);

		btnSearch = (Button) findViewById(R.id.btn_search);

		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
		lvPairedDevice = (ListView) findViewById(R.id.lv_device_paired);
		lvScannedDevice = (ListView) findViewById(R.id.lv_device_scanned);

		blueToothHelper = BlueToothHelper.getInstance();

		pairedDeviceAdapter = new DeviceAdapter(BluetoothActivity.this);
		lvPairedDevice.setAdapter(pairedDeviceAdapter);
		scannedDeviceAdapter = new DeviceAdapter(BluetoothActivity.this);
		lvScannedDevice.setAdapter(scannedDeviceAdapter);
	}

	private void initData()
	{
		if (blueToothHelper.isEnable())
		{
			switchBluetooth.setChecked(true);
			switchDetection.setChecked(true);
			// 开始倒计时
			surplusTime = DEFAULT_SURPLUS_TIME;
			mHandler.removeMessages(WHAT_HANDLER_CODE);
			mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CODE, 500);
		}
		else
		{
			switchBluetooth.setChecked(false);
			switchDetection.setChecked(false);
		}

		tvDeviceInfo.setText(blueToothHelper.getBluetoothAdapter().getName() + " - " + blueToothHelper.getBluetoothAdapter().getAddress());

		llProgress.setVisibility(View.GONE);

		Set<BluetoothDevice> pairedDevice = blueToothHelper.getBluetoothAdapter().getBondedDevices();
		if (null != pairedDevice)
		{
			pairedDeviceAdapter.addAll(blueToothHelper.getBluetoothAdapter().getBondedDevices());
		}
		else
		{
			LogFileUtil.v(TAG, "blueToothHelper bondedDevices is null");
		}
	}

	@Override
	public void scanStart()
	{
		LogFileUtil.v(TAG, "scan start");
		llProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void scanFinish(BluetoothDevice result)
	{
		LogFileUtil.v(TAG, "scan finish result = " + result);
		llProgress.setVisibility(View.GONE);
		if (null != result)
		{
			if (!scannedDeviceAdapter.contains(result) && !pairedDeviceAdapter.contains(result))
			{
				scannedDeviceAdapter.add(result);
			}
		}
	}

	@Override
	public void bonding()
	{
		LogFileUtil.v(TAG, "device bonding");
	}

	@Override
	public void bondFinish(BluetoothDevice device, boolean result)
	{
		LogFileUtil.v(TAG, "device bondFinish result = " + result);
		Set<BluetoothDevice> pairedDevice = blueToothHelper.getBluetoothAdapter().getBondedDevices();
		if (null != pairedDevice)
		{
			pairedDeviceAdapter.addAll(blueToothHelper.getBluetoothAdapter().getBondedDevices());
		}
		else
		{
			LogFileUtil.v(TAG, "blueToothHelper bondedDevices is null");
		}

		// 清理扫描到的设备
		for (BluetoothDevice paired : pairedDevice)
		{
			String name = paired.getName();
			for (int i = 0; i < scannedDeviceAdapter.getCount(); i++)
			{
				if (((BluetoothDevice) scannedDeviceAdapter.getItem(i)).getName().equalsIgnoreCase(name))
				{
					scannedDeviceAdapter.remove(scannedDeviceAdapter.getItem(i));
				}
			}
		}
	}

	/** 列表适配器 */
	private class DeviceAdapter extends CommonListAdapter<BluetoothDevice>
	{
		public DeviceAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_device;
		}

		@Override
		protected void setViewContent(final int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			TextView tvItemDevice = viewHolder.get(R.id.tv_item_device);
			tvItemDevice.setText(sList.get(i).getName());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		LogFileUtil.v(TAG, "requestCode = " + requestCode + ",resultCode = " + resultCode + ",data = " + data);
		if (REQUEST_ENABLE_CODE == requestCode)
		{
			if (RESULT_OK == resultCode)
			{
				rlDetection.setVisibility(View.VISIBLE);
				rlDeviceName.setVisibility(View.VISIBLE);
				llDevice.setVisibility(View.VISIBLE);

				btnSearch.setClickable(true);
				// 开始倒计时
				surplusTime = DEFAULT_SURPLUS_TIME;
				// 移除 之前所有
				mHandler.removeMessages(WHAT_HANDLER_CODE);
				mHandler.sendEmptyMessageDelayed(WHAT_HANDLER_CODE, 500);
			}
			else
			{
				switchBluetooth.setChecked(false);
			}
		}
	}
}
