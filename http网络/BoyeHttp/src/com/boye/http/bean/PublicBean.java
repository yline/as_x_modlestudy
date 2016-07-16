package com.boye.http.bean;

public class PublicBean
{
    /** 返回错误时,会有此参数 */
    private int code;
    
    /** 客户端id */
    private String client_id;
    
    /** 加密过后的数据字符串 */
    private String data;
    
    /** 请求的id，用来标识请求(会原样返回) */
    private String notify_id;
    
    /** 请求时间 时间戳 */
    private String time;
    
    /** 请求的接口类型，一个字符串 */
    private String type;
    
    /** 目前固定为md5 */
    private String alg;
    
    /** 签名，客服端对传输数据的签名结果 */
    private String sign;
    
    public int getCode()
    {
        return code;
    }
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    public String getClient_id()
    {
        return client_id;
    }
    
    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    }
    
    public String getData()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    public String getNotify_id()
    {
        return notify_id;
    }
    
    public void setNotify_id(String notify_id)
    {
        this.notify_id = notify_id;
    }
    
    public String getTime()
    {
        return time;
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getAlg()
    {
        return alg;
    }
    
    public void setAlg(String alg)
    {
        this.alg = alg;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }
}
