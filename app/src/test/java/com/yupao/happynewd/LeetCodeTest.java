package com.yupao.happynewd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeetCodeTest {

    /**
     * 求除自身以外数组的乘积：不能用除法,在O(n)的时间复杂度内完成
     * eg:[1,2,3] 输出: [6,3,2]
     * 解题思路：不用除法
     * 1. 除一个元素以外的乘积是：它左侧的元素乘积和*右边的元素乘积和: 1: 1 * (2*3); 2: 1 * 3; 3 = (1*2)*1
     * 2. 分别用left数组，right数组表示元素左侧，右侧的乘积和。
     * 3. 则结果数组 answer[i] = left[i] * right[i]
     * 4. 为了节约空间 answer，left, right使用一个数组，通过两次循环来更新元素的值。
     *
     * @param nums [1,2,3,4,5]
     * @return [120, 60, 40, 30, 24]
     */
    public static int[] produceSlef(int[] nums) {
        int length = nums.length;
        //
        int[] answer = new int[length];
        answer[0] = 1;
        //answer = left: [1,0,0,0,0]
        for (int i = 1; i < length; i++) {
            // i的左侧乘积和：i前一个的乘积和 * i自身，因为是从1开始即：nums[i - 1] * answer[i - 1]
            answer[i] = nums[i - 1] * answer[i - 1];
        }
        //第一轮循环计算出元素左侧的乘积和answer = left: [1,1,2,6,24]
        System.out.println("answer: ");
        System.out.println(Arrays.toString(answer));
        // 用一个int来暂存元素的右侧乘积和：
        int r = 1;
        //并且从右往左计算：[1,2,3,4,5],5的右侧乘积和 = 1，4的右侧乘积和 = 5*1，3的右侧乘积和 = 4*5*1...
        for (int i = length - 1; i >= 0; i--) {
            answer[i] = answer[i] * r;
            System.out.println(Arrays.toString(answer));
            // 与左侧乘积和一样的计算方式i前一个的乘积和 * i自身：r = r * nums[i]
            r *= nums[i];
            System.out.println(r);
        }
        return answer;
    }

}
