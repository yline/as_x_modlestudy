package com.dm.interpreter.arithmetic;

public abstract class ArithmeticExpression {
    /**
     * 抽象的解析方法
     * 具体的解析逻辑由具体的子类实现
     *
     * @return 解析得到具体的值
     */
    public abstract int interpreter();
}
