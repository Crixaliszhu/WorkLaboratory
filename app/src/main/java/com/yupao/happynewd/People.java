package com.yupao.happynewd;

import static java.lang.Math.max;

public class People {
    public String pName;

    public People(String name) {
        pName = name;
    }

    public People getPeople() {
        return new People("people");
    }

    public void setAnim(People p1) {
        System.out.println("people set");
    }

    /**
     * 给你两个二进制字符串a和b,以二进制字符串的形式返回它们的和。
     * 示例1:
     * 输入:a="11",b="1"
     * 输出:"100"
     * 示例2:
     * 输入:a="1010",b = "1011"
     * 输出:"10101"
     * 提示:
     * 1. 1<= a.length,b.length <= 104
     * 2. a和b仅由字符'0'或'1'组成
     * 3. 字符串如果不是"0",就不含前导零
     *
     * @param a
     * @param b
     * @return
     */
    public String addBinary(String a, String b) {
        StringBuilder str = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;
        while (i >= 0 || j >= 0 || carry == 1) {
//            int ai = (i >= 0) ? a.charAt(i--) - '0' : 0;
//            int bj = (j >= 0) ? b.charAt(j--) - '0' : 0;
//            int sum = ai + bj + carry;
//
//            str.append(sum % 2);
//            carry = sum / 2;

            // 1001  11011
            // 更快解法: 1. 减少了一个ai, aj, sum三个变量
            if (i >= 0) {
                carry += a.charAt(i--) - '0';
            }
            if (j >= 0) {
                carry += b.charAt(j--) - '0';
            }
            str.append(carry % 2);
            carry /= 2;
        }

        return str.reverse().toString();
    }

    /**
     * 1001  11011
     *
     * @param a
     * @param b
     * @return
     */
    public String addBinaryMonitor(String a, String b) {
        StringBuilder str = new StringBuilder();

        int carry = 0;
        int len = Math.max(a.length(), b.length());
        for (int i = 0; i < len; i++) {
            int ai = (i < a.length()) ? a.charAt(a.length() - i - 1) - '0' : 0;
            int bj = (i < b.length()) ? b.charAt(b.length() - i - 1) - '0' : 0;
            int sum = ai + bj + carry;

            str.append(sum % 2);
            carry = sum / 2;
        }
        if (carry > 0) {
            str.append(carry);
        }
        return str.reverse().toString();
    }
}
