package com.jdk8.spliterator;

/**
 * @author fangxilin
 * @Description NumCounter
 * @date
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)
 */
public class NumCounter {
    private int num;
    private int sum;
    // 是否是完整连续的数字（下个字符非数字的情况下为true）
    private boolean isWholeNum;

    public NumCounter(int num, int sum, boolean isWholeNum) {
        this.num = num;
        this.sum = sum;
        this.isWholeNum = isWholeNum;
    }

    public NumCounter accumulate(Character c) {
        System.out.println(Thread.currentThread().getName());
        NumCounter numCounter = null;
        if (Character.isDigit(c)) {
            if (isWholeNum) {
                numCounter = new NumCounter(Integer.parseInt(c.toString()), sum, false);
            } else {
                StringBuilder sb = new StringBuilder().append(num).append(c); //拼接数字
                numCounter = new NumCounter(Integer.parseInt(sb.toString()), sum, false);
            }
        } else {
            numCounter = new NumCounter(0, sum + num, true); //已经是个完整连续的数字时（下个字符非数字的情况），重置当前num为0，并计算sum
        }
        System.out.println(numCounter + ", c=" + c);
        return numCounter;
    }

    public NumCounter combine(NumCounter numCounter) {
        NumCounter numCounter1 = new NumCounter(0, this.getSum() + numCounter.getSum(), numCounter.isWholeNum);
        System.out.println("多线程并行执行后结果：" + numCounter1);
        return numCounter1;
    }

    public int getSum() {
        return sum + num;
    }


    @Override
    public String toString() {
        return "NumCounter{" +
                "num=" + num +
                ", sum=" + sum +
                ", isWholeNum=" + isWholeNum +
                '}';
    }
}

