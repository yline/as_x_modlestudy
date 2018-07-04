package com.dm.interpreter.operator;

import com.dm.interpreter.arithmetic.ArithmeticExpression;

public class AdditionExpression extends OperatorExpression {

    public AdditionExpression(ArithmeticExpression exp1, ArithmeticExpression exp2) {
        super(exp1, exp2);
    }

    @Override
    public int interpreter() {
        return exp1.interpreter() + exp2.interpreter();
    }

}
