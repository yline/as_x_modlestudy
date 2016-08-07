package com.boye.http.decode;

import com.boye.http.manager.NetManager;
import com.boye.http.obtain.NetAttachObtain;

public class NetPictureUpload extends NetAttachObtain
{
    
    @Override
    protected void success(String data)
    {
        super.success(data);
        // String str = data;
        NetManager.getInstance().setPictureUploadSuccess();
    }
    
    @Override
    protected void errorNet(String ex)
    {
        super.errorNet(ex);
        NetManager.getInstance().setPictureUploadNetError(ex);
    }
    
    @Override
    protected void errorParams(String ex)
    {
        super.errorParams(ex);
        NetManager.getInstance().setPictureUploadParamsError(ex);
    }
}
