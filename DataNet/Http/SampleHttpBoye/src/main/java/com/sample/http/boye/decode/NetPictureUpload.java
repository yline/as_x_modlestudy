package com.sample.http.boye.decode;


import com.sample.http.boye.manager.NetManager;
import com.sample.http.boye.obtain.NetAttachObtain;

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
