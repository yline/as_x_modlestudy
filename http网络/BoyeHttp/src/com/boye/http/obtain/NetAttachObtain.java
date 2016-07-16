package com.boye.http.obtain;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import com.boye.http.constants.NetConstant;
import com.boye.http.delete.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * 无token,无固定格式,数据量较大
 */
public class NetAttachObtain
{
    /** 用户ID */
    private static final String UID = "uid";
    
    /** 类型(avatar=头像,gallery=相册,other=其它,ID=身份证) */
    private static final String TYPE = "type";
    
    /** FILE 对象 */
    private static final String IMAGE = "image";
    
    public void getData(NetAttachObtainParams attachParam)
    {
        RequestParams params = new RequestParams(NetConstant.ATTACH_HTTP);
        params.addBodyParameter(UID, attachParam.getUid());
        params.addBodyParameter(TYPE, attachParam.getType());
        params.addBodyParameter(IMAGE, attachParam.getFile());
        
        x.http().post(params, new Callback.CommonCallback<String>()
        {
            
            @Override
            public void onCancelled(CancelledException isOnCallback)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                errorNet(ex + "");
            }
            
            @Override
            public void onFinished()
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onSuccess(String result)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String data = jsonObject.getString("data");
                    if (0 == code)
                    {
                        success(data);
                    }
                    else
                    {
                        errorParams(data);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    errorNet(e + "");
                }
            }
        });
    }
    
    /**
     * 再次解析
     * @param data	已初步解析的data信息
     */
    protected void success(String data)
    {
        LogFileUtil.v(MainApplication.TAG, "网络返回数据： " + data);
    }
    
    /**
     * 网络错误+Json格式错误(网络错误导致的)
     * @param ex	错误提示
     */
    protected void errorNet(String ex)
    {
        LogFileUtil.v(MainApplication.TAG, "网络错误： " + ex);
    }
    
    /**
     * 请求参数错误
     * @param ex	错误提示
     */
    protected void errorParams(String ex)
    {
        LogFileUtil.v(MainApplication.TAG, "请求参数错误： " + ex);
    }
}
