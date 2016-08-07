package com.boye.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.boye.http.decode.NetPictureUpload;
import com.boye.http.decode.NetToken;
import com.boye.http.decode.NetUserLogin;
import com.boye.http.manager.NetManager;
import com.boye.http.obtain.NetAttachObtainParams;

/**
 * 后台接口操作,一切按照接口来
 */
public class NetBaseOperate
{
    
    /**
     * 上传图片
     * @param uid	uid用户编号
     * @param type	上传用途
     * @param file	文件路径
     * @param callback
     */
    protected void attachUpload(String uid, NetAttachObtainParams.UPLOAD_PICTURE_TYPE type, File file,
        NetManager.OnPictureUploadCallback callback)
    {
        NetAttachObtainParams params = new NetAttachObtainParams();
        
        params.setUid(uid);
        params.setType(type);
        params.setFile(file);
        
        attachUpload(params, callback);
    }
    
    /**
     * 图片上传
     * @param attachParam
     * @param callback
     */
    protected void attachUpload(NetAttachObtainParams attachParam, NetManager.OnPictureUploadCallback callback)
    {
        NetPictureUpload picture = new NetPictureUpload();
        picture.getData(attachParam);
        
        NetManager.getInstance().setOnPictureUploadCallback(callback);
    }
    
    /**
     * 获取token
     * @param callback
     */
    protected void token(NetManager.OnTokenCallback callback)
    {
        NetToken token = new NetToken();
        token.getToken();
        
        NetManager.getInstance().setOnTokenCallback(callback);
    }
    
    /**
     * 用户登录
     * @param username	用户名(手机号)
     * @param password	密码
     * @param callback
     */
    protected void userLogin(String username, String password, NetManager.OnUserLoginCallback callback)
    {
        Map<String, String> map = new HashMap<String, String>();
        
        map.put(NetUserLogin.USERNAME, username);
        map.put(NetUserLogin.PASSWORD, password);
        
        userLogin(map, callback);
    }
    
    /**
     * 用户登录
     */
    protected void userLogin(Map<?, ?> map, NetManager.OnUserLoginCallback callback)
    {
        NetUserLogin userLogin = new NetUserLogin();
        userLogin.getData(map, NetUserLogin.TYPE_VALUE, NetUserLogin.API_VER_VALUE);
        
        NetManager.getInstance().setOnUserLoginCallback(callback);
    }
}
