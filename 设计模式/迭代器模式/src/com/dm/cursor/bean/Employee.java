package com.dm.cursor.bean;

public class Employee
{
    private String name;
    
    private int age;
    
    private String sex;
    
    private String position;
    
    public Employee(String name, int age, String sex, String position)
    {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.position = position;
    }
    
    @Override
    public String toString()
    {
        return "Employee [name=" + name + ", age=" + age + ", sex=" + sex + ", position=" + position + "]";
    }
}
