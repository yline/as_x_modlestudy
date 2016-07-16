package com.boye.http;

import java.io.File;

import com.boye.http.manager.NetManager;
import com.boye.http.obtain.NetAttachObtainParams;

/**
 * 界面接口操作,一切按照界面来
 */
public class NetOperate extends NetBaseOperate
{
    
    /** 获取token */
    public void getToken(NetManager.OnTokenCallback callback)
    {
        token(callback);
    }
    
    /**
     * 用户登录
     * @param username	用户名(手机号)
     * @param password	密码
     * @param callback
     */
    public void login(String username, String password, NetManager.OnUserLoginCallback callback)
    {
        userLogin(username, password, callback);
    }
    
    /**
     * 上传图片
     * @param uid	uid用户编号
     * @param type	上传用途
     * @param file	文件路径
     * @param callback
     */
    public void uploadPicture(String uid, NetAttachObtainParams.UPLOAD_PICTURE_TYPE type, File file,
        NetManager.OnPictureUploadCallback callback)
    {
        attachUpload(uid, type, file, callback);
    }
}
