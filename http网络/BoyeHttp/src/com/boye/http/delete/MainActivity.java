package com.boye.http.delete;

import java.io.File;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import com.boye.http.NetOperate;
import com.boye.http.R;
import com.boye.http.bean.TokenBean;
import com.boye.http.bean.UserLoginBean;
import com.boye.http.manager.NetManager.OnPictureUploadCallback;
import com.boye.http.manager.NetManager.OnTokenCallback;
import com.boye.http.manager.NetManager.OnUserLoginCallback;
import com.boye.http.obtain.NetAttachObtainParams;

import android.os.Bundle;
import android.view.View;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseIocFragmentActivity
{
    private NetOperate operate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        operate = new NetOperate();
    }
    
    @Event(R.id.token)
    private void token(View v)
    {
        operate.getToken(new OnTokenCallback()
        {
            
            @Override
            public void onNetError(String ex)
            {
                
            }
            
            @Override
            public void onSuccess(TokenBean bean)
            {
                
            }
        });
    }
    
    @Event(R.id.login)
    private void login(View v)
    {
        final String username = "15958148487";
        final String password = "123456";
        
        operate.login(username, password, new OnUserLoginCallback()
        {
            
            @Override
            public void onParamsError(String ex)
            {
                // TODO Auto-generated method stub
                LogUtil.i(ex);
            }
            
            @Override
            public void onNetError(String ex)
            {
                // TODO Auto-generated method stub
                LogUtil.i(ex);
            }
            
            @Override
            public void onSuccess(UserLoginBean bean)
            {
                String content = "成功";
                content += "bean.getUid = " + bean.getUid() + ", ";
                content += "bean.getMobile = " + bean.getMobile() + ", ";
                LogUtil.i(content + "\n");
                
            }
        });
    }
    
    @Event(R.id.pictureUpload)
    private void uploadPicture(View v)
    {
        // 选择图片要做
        final String uid = "36";
        final NetAttachObtainParams.UPLOAD_PICTURE_TYPE type = NetAttachObtainParams.UPLOAD_PICTURE_TYPE.GALLERY;
        final String path = "/storage/emulated/0/DCIM/Camera/IMG20150120" + "165822.jpg";
        final File file = new File(path);
        if (file.exists())
        {
            operate.uploadPicture(uid, type, file, new OnPictureUploadCallback()
            {
                
                @Override
                public void onParamsError(String ex)
                {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public void onNetError(String ex)
                {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public void onSuccess()
                {
                    // TODO Auto-generated method stub
                    
                }
            });
        }
        else
        {
            LogUtil.i("文件不存在");
        }
    }
    
}
