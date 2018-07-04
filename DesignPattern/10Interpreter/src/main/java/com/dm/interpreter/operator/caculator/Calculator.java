package com.dm.interpreter.operator.caculator;

import com.dm.interpreter.arithmetic.ArithmeticExpression;
import com.dm.interpreter.arithmetic.NumExpression;
import com.dm.interpreter.operator.AdditionExpression;
import com.dm.interpreter.operator.SubtractionExpression;

import java.util.Stack;

public class Calculator {
    // 声明一个Stack栈储存并操作所有相关的解释器
    private Stack<ArithmeticExpression> mExpStack = new Stack<ArithmeticExpression>();

    public Calculator(String expression) {
        // 声明两个ArithmeticExpression类型的临时变量,储存运算符左右两边的数字解释器
        ArithmeticExpression exp1, exp2;

        // 依据空格分割表达式字符串(这里是前提)
        String[] elements = expression.split(" ");

        /**
         * 循环遍历表达式元素数组
         */
        for (int i = 0; i < elements.length; i++) {
            switch (elements[i].charAt(0)) {
                case '+': // 加号
                    // 则将栈中的解释器弹出作为运算符号左边的解释器
                    // Returns the element at the top of the stack and removes it.
                    exp1 = mExpStack.pop();

                    // 同时将运算符号数组下标下一个元素构造成为一个数字解释器
                    exp2 = new NumExpression(Integer.valueOf(elements[++i]));

                    // 通过上面两个数字解释器构造加法运算解释器
                    mExpStack.push(new AdditionExpression(exp1, exp2));
                    break;
                case '-': // 减号
                    exp1 = mExpStack.pop();
                    exp2 = new NumExpression(Integer.valueOf(elements[++i]));

                    // Pushes the specified object onto the top of the stack.
                    mExpStack.push(new SubtractionExpression(exp1, exp2));
                    break;
                default: // 数字
                    /**
                     * 如果不是运算符则为数字
                     * 若是数字,直接构造数字解释器并压入栈
                     */
                    mExpStack.push(new NumExpression(Integer.valueOf(elements[i])));
                    break;
            }
        }
    }

    /**
     * 计算结果
     */
    public int calculate() {
        return mExpStack.pop().interpreter();
    }

}
