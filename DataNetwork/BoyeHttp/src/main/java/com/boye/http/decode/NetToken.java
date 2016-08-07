package com.boye.http.decode;

import com.boye.http.bean.TokenBean;
import com.boye.http.delete.MainApplication;
import com.boye.http.manager.NetManager;
import com.boye.http.obtain.NetTokenObtain;
import com.google.gson.Gson;
import com.yline.log.LogFileUtil;

public class NetToken extends NetTokenObtain
{
    
    @Override
    protected void success(String data)
    {
        Gson gson = new Gson();
        TokenBean tokenBean = gson.fromJson(data, TokenBean.class);
        LogFileUtil.v(MainApplication.TAG, "access_token = " + tokenBean.getAccess_token());
        // 处理token,可以缓存,也可以传出去
        NetManager.getInstance().setTokenSuccess(tokenBean);
    }
    
    @Override
    protected void errorNet(String ex)
    {
        super.errorNet(ex);
        
        NetManager.getInstance().setTokenNetError(ex + "");
    }
    
    @Override
    protected void errorParams(String ex)
    {
        super.errorParams(ex);
        
    }
    
}
