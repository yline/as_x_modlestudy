package com.dm.decorator.decorator;

import com.dm.decorator.component.Person;

/**
 * Person 的装饰, 负责Person的某一方面的 所有
 */
public abstract class PersonCloth extends Person
{
    protected Person mPerson;
    
    public PersonCloth(Person person)
    {
        this.mPerson = person;
    }
    
    @Override
    public void dressed()
    {
        mPerson.dressed();
    }
}
