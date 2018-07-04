package com.dm.interpreter.operator;

import com.dm.interpreter.arithmetic.ArithmeticExpression;

public abstract class OperatorExpression extends ArithmeticExpression {
    protected ArithmeticExpression exp1, exp2;

    public OperatorExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
}
