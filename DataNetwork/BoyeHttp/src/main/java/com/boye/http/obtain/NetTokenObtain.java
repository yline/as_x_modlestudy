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
 * 获取token,无公共参数,有固定格式,数据量比较小,token
 */
public class NetTokenObtain
{
    // json解析
    private static final String JSON_CODE = "code";
    
    private static final String JSON_DATA = "data";
    
    // 授权类型(固定为client_credentials)
    private static final String GRANT_TYPE_KEY = "grant_type";
    
    private static final String GRANT_TYPE_VALUE = "client_credentials";
    
    // 客户端ID
    private static final String CLIENT_ID_KEY = "client_id";
    
    private static final String CLIENT_ID_VALUE = "by565fa4facdb191";
    
    // 客户端密钥
    private static final String CLIENT_SECRET_KEY = "client_secret";
    
    private static final String CLIENT_SECRET_VALUE = "b6b27d3182d589b92424cac0f2876fcd";
    
    public void getToken()
    {
        RequestParams params = new RequestParams(NetConstant.TOKEN_HTTP);
        params.addBodyParameter(GRANT_TYPE_KEY, GRANT_TYPE_VALUE);
        params.addBodyParameter(CLIENT_ID_KEY, CLIENT_ID_VALUE);
        params.addBodyParameter(CLIENT_SECRET_KEY, CLIENT_SECRET_VALUE);
        
        x.http().post(params, new Callback.CommonCallback<String>()
        {
            
            @Override
            public void onCancelled(CancelledException cex)
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
                // 示例
                // {"code":0,"data":{"access_token":"dc7a9ae8f6c784e2365e8c5d6bbcc580498bc8f0","expires_in":3600,"token_type":"Bearer","scope":"base"}}
                // Gson解析中不能"半解析"
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt(JSON_CODE);
                    String data = jsonObject.getString(JSON_DATA);
                    if (0 != code)
                    {
                        errorNet("获取token出错");
                    }
                    else
                    {
                        success(data);
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
