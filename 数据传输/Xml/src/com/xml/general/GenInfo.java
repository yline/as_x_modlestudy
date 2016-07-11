package com.xml.general;

public class GenInfo
{
    private long data;
    
    private int type;
    
    private String body;
    
    private String address;
    
    private long id;
    
    /**
     * @param data
     * @param type
     * @param body
     * @param address
     * @param id
     */
    public GenInfo(long data, int type, String body, String address, long id)
    {
        super();
        this.data = data;
        this.type = type;
        this.body = body;
        this.address = address;
        this.id = id;
    }
    
    public long getData()
    {
        return data;
    }
    
    public void setData(long data)
    {
        this.data = data;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public String getBody()
    {
        return body;
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "GenInfo [data=" + data + ", type=" + type + ", body=" + body + ", address=" + address + ", id=" + id
            + "]";
    }
    
}
