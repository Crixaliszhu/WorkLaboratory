package com.yupao.happynewd;

import com.yupao.happynewd.node.ListNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class LeetCodeUtils {

    public int[] produceSlef(int[] nums) {
        int length = nums.length;
        int[] answer = new int[length];
        answer[0] = 1;
        for (int i = 1; i < length; i++) {
            answer[i] = nums[i - 1] * answer[i - 1];
        }
        System.out.println("andwer: ");
        System.out.println(Arrays.toString(answer));
        int r = 1;
        for (int i = length - 1; i >= 0; i--) {
            answer[i] = answer[i] * r;
            System.out.println("andwer[i]: ");
            System.out.println(answer[i]);
            r *= nums[i];
        }

        return answer;
    }

    static public void peopleTest() {
        People petter1 = new Men("petter");
        People p1 = petter1.getPeople();
        System.out.println("name1 = ");
        System.out.println(p1.pName);

        People petter2 = new People("Bob");
        People p2 = petter2.getPeople();
        System.out.println("name2 = ");
        System.out.println(p2.pName);

        Men petter3 = new Men("3");
        People p3 = petter3.getPeople();
        System.out.println("name3 = ");
        System.out.println(p3.pName);

        Men petter4 = new Men("4");
        Men p4 = petter4.getPeople();
        System.out.println("name4 = ");
        System.out.println(p4.pName);
    }

    static public void setTest() {
        People p1 = new People("p1");
//        p1.setAnim(p1);
//
        Men m1 = new Men("m2");
//        m1.setAnim(p1);

        People p2 = new Men("p2");
        p2.setAnim(m1);

        People p3 = new Men("p3");
//        p3.setAnim(p1);
    }

    static public int getMatchIndex(String a, String b) {
        int index = a.indexOf(b);
        return index;
    }

    /**
     * KMP算法计算next数组
     *
     * @param son abababca
     */
    static public int[] getNextArray(String father, String son) {
        int n = father.length(), m = son.length();
        // next数组
        int[] next = new int[m];
        System.out.println("----------------");
        System.out.println(Arrays.toString(next));
        // 双指针一前一后 a b a b a b c a
        for (int i = 1, j = 0; i < m; i++) {
            // i = 6, next[6] = 4, j = 4 son[6] = c, son[4] = a
            while (j > 0 && son.charAt(i) != son.charAt(j)) {
                // j = next[3] = 2
                // i = 6, next[6] = 4, j = 2 son[6] = c, son[2] = a
                // j = next[1] = 0
                j = next[j - 1];
            }
            //son[6] = c, son[0] = a
            if (son.charAt(i) == son.charAt(j)) {
                j++;
            }
            // next[6] = 0
            next[i] = j;
        }
        return next;
    }

    /**
     * 给定一个整数数组(该数组是非递减排序的)，和目标值target。从数组中找出两个数相加之和等于 target的两数。
     * 要求返回两数的index, 且 1 <= index1 <= index2。
     * 额外已知条件：你可以假设答案唯一，并且不可以重复使用相同的元素。
     *
     * @return
     */
    static public int[] getTargetSumTwoIndex(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            for (int j = n - 1; j >= i; j--) {
                if (nums[j] + nums[i] == target) {
                    return new int[]{i + 1, j + 1};
                }
            }
        }

        return new int[2];
    }

    /**
     * 找出最大容水两
     *
     * @param height
     * @return
     */
    static public int findMostWater(int[] height) {
        int n = height.length;
        int left = 0, right = n - 1, total = 0;
        while (left < right) {
            int y = Math.min(height[left], height[right]);
            System.out.println("y = ");
            System.out.println(y);
            int x = right - left;
            System.out.println("x = ");
            System.out.println(x);
            if (x * y > total) {
                total = x * y;
            }
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
            System.out.println("-----------------");
        }
        return total;
    }

    /**
     * 给你两个字符串：ransomNote 和 magazine ，判断 ransomNote 能不能由 magazine 里面的字符构成。
     * <p>
     * 如果可以，返回 true ；否则返回 false 。
     * <p>
     * magazine 中的每个字符只能在 ransomNote 中【使用一次】。
     * 输入：ransomNote = "aa", magazine = "aab"
     * 输出：true
     * <p>
     * 输入：ransomNote = "aa", magazine = "ab"
     * 输出：false
     * <p>
     * 输入：ransomNote = "aab", magazine = "baa"
     * 输出：false
     *
     * @return
     */
    static public boolean canBuild(String a, String b) {
        // a的char,b都包含
        if (a.length() > b.length()) return false;
        for (int i = 0; i < a.length(); i++) {
            char ch = a.charAt(i);
            int index = b.indexOf(ch);
            if (index < 0) {
                return false;
            } else {
                b = b.replaceFirst(String.valueOf(ch), "");
            }
        }
        return true;
    }

    /**
     * 寻找三数之合为0的三元组: {-1,0,1,2,-1,-4}
     *
     * @return
     */
    static public List<List<Integer>> getThreeSumIsZero(int[] numbers) {
        List<List<Integer>> list = new ArrayList<>();
        int length = numbers.length;
        if (length < 3) {
            return list;
        }
        int a = Integer.MAX_VALUE;
        // 先排序
        Arrays.sort(numbers);
        System.out.println(Arrays.toString(numbers));
        // 再双指针查找：{-4,-1,-1,0,1,2}
        for (int i = 0; i < length; i++) {
            if (i + 2 < length && (numbers[i] + numbers[i + 1] + numbers[i + 2]) > 0) {
                // 最小的组合都大于0,那必然无解
                break;
            }
            if ((numbers[i] + numbers[length - 2] + numbers[length - 2]) < 0) {
                // 对于本次循环i,最大的组合都小于0,那必然无解
                continue;
            }
            // 去重
            if (i > 0 && numbers[i] == numbers[i - 1]) {
                continue;
            }
            int next = -numbers[i];
//            int k = length - 1;
//            for (int j = i + 1; j < length; j++) {
//                if (j > i + 1 && numbers[j] == numbers[k - 1]) {
//                    continue;
//                }
//                while (j < k && numbers[j] + numbers[k] > next) {
//                    --k;
//                }
//                if (j == k) {
//                    break;
//                }
//                if (numbers[j] + numbers[k] == next) {
//                    List<Integer> sums = new ArrayList<>();
//                    sums.add(numbers[i]);
//                    sums.add(numbers[j]);
//                    sums.add(numbers[k]);
//                    list.add(sums);
//                }
//            }

            int j = i + 1, k = length - 1;
            while (j < k) {
                int sum = numbers[j] + numbers[k];
                if (sum == next) {
                    List<Integer> sums = new ArrayList<>();
                    sums.add(numbers[i]);
                    sums.add(numbers[j]);
                    sums.add(numbers[k]);
                    list.add(sums);
                    j++;
                    k--;
                } else if (sum > next) {
                    while (j < k && numbers[k] == numbers[k + 1]) {
                        --k;
                    }
                    --k;
                } else {
                    while (j < k && numbers[j] == numbers[j - 1]) {
                        ++j;
                    }
                    ++j;
                }
            }

        }
        return list;
    }

    /**
     * i5 1 2 6 9 3 7 10 8j
     * key = nums[i]; 5
     * if(i < j && nums[j] > key) j--;
     * i5 1 2 6 9 3j 7 10 8
     * if(i < j){
     * nums[i] = nums[j]; 这是赋值不是交换位置
     * i3 1 2 6 9 3j 7 10 8
     * i++;
     * 3 i1 2 6 9 3j 7 10 8
     * }
     * <p>
     * if(i < j && nums[i] < key) i++;
     * 3 1 2 i6 9 3j 7 10 8
     * <p>
     * if( i < j){
     * nums[j] = nums[i];
     * 3 1 2 i6 9 6j 7 10 8;
     * j--;
     * 3 1 2 i6 9j 6 7 10 8;
     * }
     * nums[i] = key;
     * 3 1 2 i5 9j 6 7 10 8;
     * quickSort(nums,start, i-1);
     * 3 1 2
     * quickSort(nums,i+1, end);
     * 6 9 7 10 8;
     *
     * @param nums
     * @param start
     * @param end
     * @return
     */
    static public int[] quickSort(int[] nums, int start, int end) {
        int key = nums[start];
        int i = start, j = end;
        while (i < j) {
            // 对右指针：找比key小的数，找到就停下来
            while (i < j && nums[j] > key) {
                j--;
            }
            // 把小于key的数，赋值给i
            if (i < j) {
                nums[i] = nums[j];
                i++;
            }
            // 对左指针：找比key大的数
            while (i < j && nums[i] < key) {
                i++;
            }
            // 把大于key的数,赋值j
            if (i < j) {
                nums[j] = nums[i];
                j--;
            }
            nums[i] = key;
            // 此时已经将nums分割成，key左边的数据比key小，key右边的数比key大。需要对左右的字数组进行快排。
            quickSort(nums, start, i - 1);
            quickSort(nums, i + 1, end);
        }
        return nums;
    }

    /**
     * 给定一个含有 n 个正整数的数组和一个正整数 target 。
     * <p>
     * 找出该数组中满足其总和大于等于 target 的长度最小的 【连续】子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
     * eg：[2,3,1,2,4,3]， 7： [4,3] 输出：2
     *
     * @param nums
     * @param target
     * @return
     */
    static public int getCloseTarget(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) return 0;
        // 1. 暴力解法
//        int ans = Integer.MAX_VALUE;
//        for (int i = 0; i < n; i++) {
//            int sum = 0;
//            for (int j = i; j < n; j++) {
//                sum += nums[j];
//                if (sum >= target) {
//                    ans = Math.min(ans, j - i + 1);
//                    break;
//                }
//            }
//        }
//        return ans == Integer.MAX_VALUE ? 0 : ans;

        // 2. 滑动窗口解法，题目中"【连续】子数组"可以发现可以使用滑动窗口解法
        /**
         * 两个指针start, end, [start,end]内的元素之和即满足 >= target的条件，答案 = end - start + 1
         * 1. end不断后移，找到 >= target的位置， 此时更新 ans
         * 2. 一旦更新ans后，就可以移动后移start指针，并继续重复步骤1
         */
        int start = 0, end = 0;
        int sum = 0;
        int ans = Integer.MAX_VALUE;
        while (end < n) {
            sum += nums[end];
            while (sum >= target) {
                ans = Math.min(ans, end - start + 1);
                sum -= nums[start];
                start++;
            }
            end++;
        }

        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    /**
     * 获取最长无重复字符子串的长度：
     * eg: abcabcdd, 最长无重复字符子串为abc，长度 = 3， return 3；
     *
     * @return
     */
    static public int getMaxLengthSon(String s) {
        int n = s.length();
        if (n <= 1) return n;
        // hashset存入字符，判断不重复的方式, 以遍历字符串s的i为左指针，右指针从每个i开始往后移动；
//        Set<Character> noRepeat = new HashSet<>();
//        // 右指针从-1开始，则每次 right+1可以覆盖 0~n-1
//        int right = -1, ans = 0;
//        for (int left = 0; left < n; left++) {
//            if (left != 0) {
//                noRepeat.remove(s.charAt(left - 1));
//            }
//            // 从左指针开始遍历，找出最大无重复子串
//            while (right + 1 < n && !noRepeat.contains(s.charAt(right + 1))) {
//                noRepeat.add(s.charAt(right + 1));
//                right++;
//            }
//            ans = Math.max(ans, right - left + 1);
//        }
//        return ans;
        // hashMap存字符索引的方式判断是否重复,以遍历s的i为右指针，重复时，从hashMap取出重复元素的位置为左指针。
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0, left = 0;
        // abcabcbb
        for (int right = 0; right < n; right++) {
            char ch = s.charAt(right);
            //a0 b1 c2-a; a0 b1 c2 a3-b;....;a0 b1 c2 a3 b4 c5 b6;
            if (map.containsKey(ch)) {
                left = Math.max(map.get(ch) + 1, left);
                // left = Math.max(1,0) = 1;left = Math.max(2,1) = 2
            }
            max = Math.max(max, right - left + 1);
            // max = 3;max = 3;
            map.put(ch, right);// put会覆盖重复元素，所以每次从map里取出的左指针的值都是最新或者说最后加入到map里的那个
            //a0 b1 c2 a3 b4
        }
        return max;
    }

    /**
     * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
     * <p>
     * 输入: s = "cbaebabacd", p = "abc"
     * 输出: [0,6]
     * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
     * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
     *
     * @return
     */
    static public List<Integer> getXorString(String s, String p) {
        int n = s.length();
        int m = p.length();
        if (n < m) return new ArrayList<>();
        List<Integer> list = new ArrayList<>();

//        // 滑动窗口-原始解法：分别记录字符的出现次数，遍历s字串，向右滑动，比较与p等长的子串是否和p拥有相同字符出现次数。
//        // pCount只用来比较
//        int[] pCount = new int[26];
//        int[] sCount = new int[26];
//        for (int i = 0; i < m; i++) {
//            sCount[s.charAt(i) - 'a']++;
//            pCount[p.charAt(i) - 'a']++;
//        }
//        if (Arrays.equals(sCount, pCount)) {
//            list.add(0);
//        }
//        for (int i = 0; i < n - m; i++) {
//            // 窗口向右移动
//            // i从0开始自增，这里相当于去掉左侧第一个元素的出现次数
//            sCount[s.charAt(i) - 'a']--;
//            // 在右侧加入一个元素的出现次数
//            sCount[s.charAt(i + m) - 'a']++;
//            if (Arrays.equals(sCount, pCount)) {
//                list.add(i + 1);
//            }
//        }
        // 滑动窗口-优化解法：用一个count结合滑动窗口，表示窗口内元素出现的次数。在s里出现次数＋1，在p里出现-1
        // 用differ表示 滑动窗口内不同元素的个数(不等于0)。
        int[] count = new int[26];
        // baa aa
        for (int i = 0; i < m; i++) {
            count[s.charAt(i) - 'a']++;
            count[p.charAt(i) - 'a']--;
        }
        int differ = 0;
        for (int i = 0; i < 26; i++) {
            if (count[i] != 0) {
                differ++;
            }
        }
        // i =0时，首次匹配是否时异位词。
        if (differ == 0) {
            list.add(0);
        }
        for (int i = 0; i < n - m; i++) {
            // 窗口向右滑动左侧处理： 去掉左侧第一个元素的次数，去掉的方式就是：对s[i]位置上的次数-1(因为在s里出现次数＋1)；
            int charCount = count[s.charAt(i) - 'a'];
            if (charCount == 1) {// 如果这个元素出现次数是1，表示它在p中没有出现，是一个不同的数，去掉他则不同数会少一个，differ-1；
                --differ;
            } else if (charCount == 0) {//反之则diff+1;
                ++differ;
            }
            --count[charCount];
            // 窗口向右滑动右侧处理： 在右侧加上窗口长度后一位的元素的次数，方式就是：对s[i+m]位置上的次数+1(因为在s里出现次数＋1)；
            int rightCount = s.charAt(i + m) - 'a';
            if (count[rightCount] == -1) {//如果这个元素出现此时是-1，则表示它在p里出现了1次，加上它则成为相同数，所以 diff-1;
                --differ;
            } else if (count[rightCount] == 0) {//反之则diff+1;
                ++differ;
            }
            ++count[rightCount];
            if (differ == 0) {
                list.add(i + 1);
            }
        }

        return list;
    }

    /**
     * 同构字符串：
     * 给定两个字符串 s 和 t ，判断它们是否是同构的。
     * <p>
     * 如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
     * <p>
     * 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，字符可以映射到自己本身。
     * <p>
     * 输入：s = "egg", t = "add"
     * 输出：true
     * <p>
     * 输入：s = "foo", t = "bar"
     * 输出：false
     * <p>
     * 输入：s = "badc", t = "abab"
     * 输出：false
     *
     * @return
     */
    static public boolean sameConstructor(String s, String t) {
        if (s.length() != t.length()) return false;
        Map<Character, String> map = new HashMap<>();
        Map<Character, String> mapT = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i);
            char b = t.charAt(i);
            if ((mapT.containsKey(b) && !Objects.equals(mapT.get(b), String.valueOf(a))) || (map.containsKey(a) && !Objects.equals(map.get(a), String.valueOf(b)))) {
                return false;
            }
            map.put(a, String.valueOf(b));
            mapT.put(b, String.valueOf(a));
        }
        return true;
    }

    static public boolean sameConstructorString(String p, String s) {
        List<String> list = Arrays.asList(s.split(" "));
        System.out.println("lsit = ");
        System.out.println(list);

        return true;
    }

    static public boolean patternFun(String pattern, String str) {
        Map<String, Character> str2ch = new HashMap<String, Character>();
        Map<Character, String> ch2str = new HashMap<Character, String>();
        int m = str.length();
        int i = 0;
        for (int p = 0; p < pattern.length(); ++p) {
            if (i >= m) {
                return false;
            }
            int j = i;
            while (j < m && str.charAt(j) != ' ') {
                j++;
            }
            char ch = pattern.charAt(p);
            String tmp = str.substring(i, j);
            if (str2ch.containsKey(tmp) && str2ch.get(tmp) != ch) {
                return false;
            }
            if (ch2str.containsKey(ch) && !tmp.equals(ch2str.get(ch))) {
                return false;
            }
            str2ch.put(tmp, ch);
            ch2str.put(ch, tmp);
            i = j + 1;
        }
        return i >= m;
//        List<String> list = Arrays.asList(str.split(" "));
//        int n = pattern.length();
//        if (n != list.size()) return false;
//        for (int i = 0; i < n; i++) {
//            char ch = pattern.charAt(i);
//            String s = list.get(i);
//            if (ch2str.containsKey(ch) && !ch2str.get(ch).equals(s)) {
//                return false;
//            }
//            if (str2ch.containsKey(s) && str2ch.get(s) != ch) {
//                return false;
//            }
//            ch2str.put(ch, s);
//            str2ch.put(s, ch);
//        }
//        return true;
    }

    /**
     * 是否时异味词：
     * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
     * <p>
     * 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
     * <p>
     * 输入: s = "anagram", t = "nagaram"
     * 输出: true
     * <p>
     * 输入: s = "rat", t = "car"
     * 输出: false
     *
     * @param s
     * @param t
     * @return
     */
    static public boolean isAnagram(String s, String t) {
        int n = s.length();
        if (n != t.length()) return false;
        char[] charsA = s.toCharArray();
        char[] charsB = t.toCharArray();
        Arrays.sort(charsA);
        Arrays.sort(charsB);
        return Arrays.equals(charsA, charsB);
    }


    /**
     * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
     * <p>
     * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
     * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
     * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
     *
     * @return
     */
    public static List<List<String>> getXorStringList(String[] arr) {
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            char[] chars = arr[i].toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            List<String> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(arr[i]);
            map.put(key, list);
        }
        return new ArrayList<List<String>>(map.values());
    }

    public static int[] getSumFromArray(int[] arr, int target) {
        if (arr.length < 2) return new int[]{};
        int[] ans = new int[]{};
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int a = arr[i];
            if (map.containsKey(target - a)) {
                return new int[]{i, map.get(target - a)};
            }
            map.put(a, i);
        }

        return ans;
    }

    /**
     * 获取快乐数
     *
     * @param n
     * @return
     */
    public static boolean getHappyNum(int n) {
        Set<Integer> map = new HashSet<>();
        while (n != 1 && !map.contains(n)) {
            map.add(n);
            n = getNext(n);
        }
        return n == 1;
    }

    private static int getNext(int n) {
        int total = 0;
        while (n > 0) {
            int d = n % 10;
            n = n / 10;
            total += d * d;
        }
        return total;
    }

    /**
     * 给你一个整数数组 nums 和一个整数 k ，判断数组中是否存在两个 不同的索引 i 和 j ，满足 nums[i] == nums[j] 且 abs(i - j) <= k 。如果存在，返回 true ；否则，返回 false 。
     * 输入：nums = [1,2,3,1], k = 3
     * 输出：true
     * <p>
     * 输入：nums = [1,2,3,1,2,3], k = 2
     * 输出：false
     *
     * @param arr
     * @return
     */
    public static boolean hasSameNumber(int[] arr, int k) {
        int n = arr.length;
//        if (n < 2) return false;
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < n; i++) {
//            if (map.containsKey(arr[i]) && Math.abs(map.get(arr[i]) - i) <= k) {
//                return true;
//            }
//            map.put(arr[i], i);
//        }

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (i > k) {
                set.remove(arr[i - k - 1]);
            }
            if (!set.add(arr[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 找出给定无序整数数组中，连续的元素最大长度
     * 输入：【4，100，300，2，301，1，3】 输出：4
     * 组内连续出现的数有：300，301； 1，2，3，4；最大的长度为 4
     *
     * @param nums
     * @return
     */
    public static int getLongestConsecutive(int[] nums) {
        int n = nums.length;
        int maxLength = 0;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) { // 复杂度：O(n)
            set.add(num);
        }
        for (int num : nums) { // 复杂度：O(n)
            // 找到没有比它跟小的数的那个元素，它就是一组连续数的开头，从它开始遍历
            if (!set.contains(num - 1)) {
                int curLength = 0;
                int curNum = num;
                // 从开头数 开始往后查找连续数
                while (set.contains(curNum + 1)) {
                    curLength++;
                    curNum++;
                }
                maxLength = Math.max(curLength, maxLength);
            }
        }

        return maxLength;
    }

    /**
     * 给定一个  无重复元素 的 有序 整数数组 nums 。
     * <p>
     * 返回 恰好覆盖数组中所有数字 的 最小有序 区间范围列表 。也就是说，nums 的每个元素都恰好被某个区间范围所覆盖，并且不存在属于某个范围但不属于 nums 的数字 x 。
     * <p>
     * 输入：nums = [0,1,2,4,5,7]
     * 输出：["0->2","4->5","7"]
     * 解释：区间范围是：
     * [0,2] --> "0->2"
     * [4,5] --> "4->5"
     * [7,7] --> "7"
     *
     * @param nums
     * @return
     */
    public static List<String> getBetweenString(int[] nums) {
        List<String> list = new ArrayList<>();
        int n = nums.length;
        int i = 0;
        while (i < n) {
            int low = i;
            i++;
            if (i < n && nums[i] == nums[i - 1] + 1) {
                i++;
            }
            int high = i - 1;
            StringBuilder sb = new StringBuilder(Integer.toString(nums[low]));
            if (low < high) {
                sb.append("->");
                sb.append(nums[high]);
            }
            list.add(sb.toString());
        }
        return list;
    }

    /**
     * 给定一个二位整形数组： {[1,4],[2,6],[,7,9]}
     * 合并区间：保证两个区间不存在交集
     * 输入：{[1,4],[2,6],[,7,9]}
     * 输出：{[1,6],[,7,9]}
     *
     * @param intervals
     * @return
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        int n = intervals.length;
        if (n < 2) return intervals;
        Arrays.sort(intervals, new Comparator<int[]>() {

            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];
            if (i == 0 || list.get(list.size() - 1)[1] < start) {
                list.add(new int[]{start, end});
            } else {
                list.get(list.size() - 1)[1] = Math.max(list.get(list.size() - 1)[1], end);
            }
        }
        return list.toArray(new int[][]{});
    }


    /**
     * 给定一个二位整形数组（子区间已不存在交集）： {[1,3],[4,8],[9,12]}
     * 将一个新区间插入：二位数组，保证两个区间不存在交集(如有必要可合并区间)
     *
     * @param intervals {[1,3],[4,8],[9,12]}
     * @param s         [2,5]
     * @return
     */
    public static int[][] mergeIntervals(int[][] intervals, int[] s) {
        //方法一：模拟
        // 如果 intervals 中区间[l1,r1]不如s[l2,r2]重合则必然满足：该区间要么在s的左侧 r1 < l2，要么在s的右侧 r1 > l2
        // 我们遍历数组intervals 如果不重合加入到新数组，如果重合则合并，取并集min(l1,l2), max(r1,r2)。
        // 当遇到(li, ri)满足 li > r2时说明以后的遍历不会重合了，将并集加入新数组。
        int left = s[0];
        int right = s[1];
        boolean isAdded = false;
        List<int[]> list = new ArrayList<>();
        for (int[] ints : intervals) {
            if (ints[0] > right) {
                // ints在s的右侧-判断该不该加入 s
                if (!isAdded) {
                    list.add(new int[]{left, right});
                    isAdded = true;
                }
                list.add(ints);
            } else if (ints[1] < left) {
                // ints在s的左侧-直接加入
                list.add(ints);
            } else {
                // 有交集-更新s的边界
                left = Math.min(left, ints[0]);
                right = Math.max(right, ints[1]);
            }
        }
        // 如果s是最大的那个区间，那么上面循环里的加入s的操作不会走，在最后补一下加入s的操作
        if (!isAdded) {
            list.add(new int[]{left, right});
        }

        return list.toArray(new int[][]{});
    }

    /**
     * 判断环形链表
     *
     * @param head
     * @return
     */
    public static boolean checkCycle(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            if (!set.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /**
     * 快慢指针 判断环形链表
     *
     * @return
     */
    public static boolean checkCycleTwo(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null) {
            if (slow == fast) {
                return true;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }

    /**
     * 给你两个链表，链表中存储的数字倒叙组成一个整数（[2->3->4]： 432）,除了数0: [0],其他整数不会以0开头。
     * 将相加的和页以倒叙链表存储
     * [2,3,4] + [1,2,3] = [3,5,7]
     *
     * @param one
     * @param two
     * @return
     */
    public static ListNode addTwoNodes(ListNode one, ListNode two) {
        ListNode head = null;
        ListNode temp = null;
        int over = 0;
        while (one != null || two != null) {

            int a = one != null ? one.val : 0;
            int b = two != null ? two.val : 0;

            int sum = a + b + over;
            if (head == null) {
                head = temp = new ListNode(sum % 10);
            } else {
                temp.next = new ListNode(sum % 10);
                temp = temp.next;
            }
            over = sum / 10;
            if (one != null) {
                one = one.next;
            }
            if (two != null) {
                two = two.next;
            }
        }
        if (over > 0) {
            temp.next = new ListNode(over);
        }

        return head;
    }

    /**
     * 合并两个升序链表，合并后的链表满足页升序排列。
     *
     * @return
     */
    public static ListNode mergeLink(ListNode node1, ListNode node2) {
        ListNode head = new ListNode(-1);
        ListNode pre = head;
        // 循环条件：任意链表没有到达尾部
        while (node1 != null && node2 != null) {
            if (node1.val <= node2.val) {
                pre.next = node1;
                node1 = node1.next;
            } else {
                pre.next = node2;
                node2 = node2.next;
            }
            pre = pre.next;
        }
        // 因其中一个链表先遍历都尾部时，另一个未到达结尾的链表剩余元素必然都是比较大的元素，拼接到pre后面即可。
        pre.next = node1 == null ? node2 : node1;
        return head.next;
    }

    /**
     * 反转链表的 [left,right]区间内的元素
     *
     * @param head
     * @param left
     * @param right
     * @return
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {

        return head;
    }

    /**
     * 删除排序链表种的重复元素，只留下没有重复的元素。
     * 输入：[1->2->2->3->3->4]
     * 输出： 1->4
     *
     * @param node
     * @return
     */
    public static ListNode getNoRepeatNodes(ListNode node) {
        // 存每个节点出现的次数，如果大于1则标识是重复节点
//        Map<Integer, Integer> map = new HashMap<>();
//        ListNode headPre = new ListNode(-1);
//        headPre.next = node;
//        // 先计算重复次数
//        ListNode cur = headPre.next;
//        while (cur != null) {
//            if (map.containsKey(cur.val)) {
//                map.put(cur.val, map.get(cur.val) + 1);
//            } else {
//                map.put(cur.val, 1);
//            }
//            cur = cur.next;
//        }
//        //开始检测
//        ListNode temp = headPre.next;
//        ListNode pre = headPre;
//        while (temp != null) {
//            if (map.get(temp.val) > 1) {
//                // 重复元素
//                pre.next = temp.next;
//            } else {
//                pre = temp;
//            }
//            temp = temp.next;
//        }
        // 充分利用已排序这一特点，那么重复元素是相邻的
        ListNode headPre = new ListNode(-1);
        ListNode cur = headPre;
        // -1 -> 2 -> 1A -> 1B -> 1C -> 3
        // 从哑节点开始遍历，所以 判断cur.next, cur.next.next不为空是没问题的，避免了异常情况，并且不会漏节点。
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                //2 -> 1A -> 1B -> 1C -> 3
                // 记录这个重复节点的数据为x,删除后续等于x的节点
                int x = cur.val;
                //cur = 2, cur.next = 1A, cur.next.next = 1B, 目标是删除 1A,1B,1C三个节点
                while (cur.next != null && cur.next.val == x) {
                    //cur.next = 1B, cur.next.next = 1C - 满足 = x
                    //cur.next = 1C, cur.next.next = 3 - 还是满足 = x
                    //cur.next = 3, cur.next.next = null 不满足 = x 退出内循环。cur.next.next = null 退出外循环
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }

        return headPre.next;
    }

    /**
     * 计算二叉树的最大深度
     * 1. 树的最大深度等于 Math.max(左子树的最大深度,右子树的最大深度) + 1
     *
     * @param node
     * @return
     */
    public static int getTreeLength(ListNode node) {
        if (node == null) {
            return 0;
        }

        int left = getTreeLength(node.left);
        int right = getTreeLength(node.right);

        return Math.max(left, right) + 1;
    }

    /**
     * 翻转二叉树：左子树交换到右子树的位置上
     */
    public static ListNode reverseTre(ListNode node) {
        if (node == null) {
            return null;
        }
        // 先翻转左右子树
        ListNode left = reverseTre(node.left);
        // 子树已翻转，则只需要交换左右子树位置
        node.left = reverseTre(node.right);
        node.right = left;

        return node;
    }

    /**
     * 检查二叉树是否是轴对称树
     * 与判断二叉树是否相等(左子树=左子树)类似：左子树=右子树
     *
     * @param root
     * @return
     */
    public static boolean isSymmetricallyTree(ListNode root) {
        if (root == null) return true;
        if(root.left == null && root.right == null){
            return true;
        }
        if(root.left == null || root.right == null) return false;
        if (root.left.val != root.right.val) return false;

        return isSymmetricallyTree(root.left) && isSymmetricallyTree(root.right);

//        return check(root, root);
    }

    /**
     * 检查是否相等
     *
     * @param p
     * @param q
     * @return
     */
    private static boolean check(ListNode p, ListNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        // 不同是:左子树与右子树相比
        return check(p.left, q.right) && check(p.right, q.left);
    }

}
