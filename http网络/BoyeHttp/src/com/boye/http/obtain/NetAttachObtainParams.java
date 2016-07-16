package com.boye.http.obtain;

import java.io.File;

/**
 * 无token,无固定格式,数据量较大,这个用于确定格式; 这一个可以使用于很多很多,例如子bean
 */
public class NetAttachObtainParams
{
    private String uid;
    
    private UPLOAD_PICTURE_TYPE type;
    
    private File file;
    
    public String getUid()
    {
        return uid;
    }
    
    public void setUid(String uid)
    {
        this.uid = uid;
    }
    
    public String getType()
    {
        return type.getValue();
    }
    
    public void setType(UPLOAD_PICTURE_TYPE type)
    {
        this.type = type;
    }
    
    public File getFile()
    {
        return file;
    }
    
    public void setFile(File file)
    {
        this.file = file;
    }
    
    public enum UPLOAD_PICTURE_TYPE
    {
        /** 头像 */
        AVATAR("avatar"),
        /** 相册 */
        GALLERY("gallery"),
        /** 其它 */
        OTHER("other"),
        /** 身份证 */
        ID("ID");
        
        private final String value;
        
        /** 外界获取数值 */
        public String getValue()
        {
            return value;
        }
        
        UPLOAD_PICTURE_TYPE(String value)
        {
            this.value = value;
        }
    }
}
