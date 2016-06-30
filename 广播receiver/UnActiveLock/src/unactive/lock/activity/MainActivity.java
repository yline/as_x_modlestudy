package unactive.lock.activity;

import com.unactive.lock.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import unactive.lock.receiver.LockReceiver;

/**
 * 利用DeviceAdminReceiver广播,实现防卸载
 * @author YLine
 *
 * 2016年6月30日 下午10:46:22
 */
public class MainActivity extends BaseActivity
{
    private DevicePolicyManager mDevicePolicyManager;
    
    private ComponentName mComponentName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initData();
        
        // 激活
        findViewById(R.id.btn_active).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                boolean isActive = mDevicePolicyManager.isAdminActive(mComponentName);
                LogFileUtil.v(MainApplication.TAG, "btn_active onClicked, isActive = " + isActive);
                
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                MainActivity.this.startActivity(intent);
            }
        });
        
        // 取消激活
        findViewById(R.id.btn_unactive).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "btn_unactive onClicked");
                mDevicePolicyManager.removeActiveAdmin(mComponentName);
                LogFileUtil.v(MainApplication.TAG,
                    "removeActiveAdmin isActive = " + mDevicePolicyManager.isAdminActive(mComponentName));
            }
        });
    }
    
    private void initData()
    {
        mDevicePolicyManager =
            (DevicePolicyManager)MainApplication.getApplication().getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(MainApplication.getApplication(), LockReceiver.class);
    }
}
