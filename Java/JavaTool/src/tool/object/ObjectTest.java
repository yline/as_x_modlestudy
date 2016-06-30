package tool.object;

/**
 * 任何class文件,没有继承父类,则默认继承Object
 */
public class ObjectTest
{
    public static void main(String[] args)
    {
        // Cloneable
        User user = new User("yline");
        
        try
        {
            User user2 = user.clone();
            // 判断 false
            System.out.println(user == user2);
            
            // 判断 true
            System.out.println(user.name == user2.name);
            // 判断 true
            System.out.println(user.name.equals(user2.name));
            // 判断 true
            System.out.println(user.address == user2.address);
            // 判断 true
            System.out.println(user.address.equals(user2.address));
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        
        // 工具类
        String str = null;
        // result   0
        System.out.println(java.util.Objects.hashCode(str));
        // result  null
        System.out.println(java.util.Objects.toString(str));
        // 只有这个不能为null  result null
        System.out.println(java.util.Objects.requireNonNull(str + ""));
    }
    
    // Address
    static class Address
    {
        String detail;
        
        public Address(String detail)
        {
            this.detail = detail;
        }
    }
    
    /**
     * 实现 Cloneable 接口
     * 浅克隆
     * 它只是简单的复制,如果复制的对象是引用,则复制的结果也只是引用
     */
    static class User implements Cloneable
    {
        String name;
        
        Address address;
        
        public User(String name)
        {
            this.name = name;
            this.address = new Address("test");
        }
        
        @Override
        protected User clone()
            throws CloneNotSupportedException
        {
            return (User)super.clone();
        }
    }
    
}
