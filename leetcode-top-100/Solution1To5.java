import java.util.HashMap;
import java.util.HashSet;

class Solution1To5 {
    // 1. 两数之和
    // 哈希表缓存：很基础的问题，以数组元素的值作为键，下标作为值建立映射表即可实现O(n)
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> cache = new HashMap<>();
        for (int i = 0; i<nums.length; i++) {
            if (cache.containsKey(target - nums[i])) {
                return new int[] {i, cache.get(target - nums[i])};
            }
            cache.put(nums[i], i);
        }
        return new int[] {-1, -1};
    }

    // 2. 两数相加
    // 链表数据结构：典型链表操作题，考验对链表操作的熟悉程度
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res = new ListNode();
        ListNode pos1 = l1, pos2 = l2, respos = res;
        int add = 0;  // 低位到高位的进位值
        // 核心条件是两个位数加完，加上进位不等于0
        while (pos1 != null || pos2 != null || add != 0) {
            int val1 = pos1 != null? pos1.val: 0;
            int val2 = pos2 != null? pos2.val: 0;

            respos.next = new ListNode((val1 + val2 + add) % 10);
            add = (val1 + val2 + add) / 10;  // 先计算当前位，再更新进位

            pos1 = pos1 != null? pos1.next: null;
            pos2 = pos2 != null? pos2.next: null;
            respos = respos.next;
        }
        return res.next;
    }

    // 3. 无重复字符的最长子串
    // 滑动窗口问题：典型的滑动窗口问题，设窗口起点为a时，得到的无重复的最长子串终点为b，即(a,b)为以a为起点的无重复的最长子串，则窗口起点为a+1时，已知(a+1, b)为不重复字串，从b+1往后再试探即可
    // 具体每个窗口里验证无重复性质，则使用哈希表解决
    public int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        int res = 0;
        int end = 0;      // 上一次的滑动窗口终点
        for (int start = 0; start < s.length(); start++) {
            int i;
            for (i = end; i < s.length(); i++) {
                if (set.contains(s.charAt(i))) {
                    break;
                }
                set.add(s.charAt(i));
            }
            res = Math.max(res, set.size());
            end = i;  // 记录窗口终点

            // 下次移动窗口起点时，重复利用上次的set
            set.remove(s.charAt(start));
        }
        return res;
    }

    // 4. 寻找两个正序数组的中位数
    // 归并排序问题：经典归并排序思想的应用，使用归并即可合并两个有序数组为一个数组
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int [] nums = new int[nums1.length + nums2.length];
        int pos = 0, pos1 = 0, pos2 = 0;

        while (pos1 < nums1.length && pos2 < nums2.length) {
            if (nums1[pos1] < nums2[pos2]) {
                nums[pos] = nums1[pos1];
                pos1 += 1;
            } else {
                nums[pos] = nums2[pos2];
                pos2 += 1;
            }
            pos += 1;
        }

        while (pos1 < nums1.length) {
            nums[pos] = nums1[pos1];
            pos1 += 1;
            pos += 1;
        }

        while (pos2 < nums2.length) {
            nums[pos] = nums2[pos2];
            pos2 += 1;
            pos += 1;
        }

        return nums.length % 2 == 1? nums[nums.length / 2]: (nums[nums.length/ 2 - 1] + nums[nums.length / 2]) / 2.0;
    }

    // 5. 最长回文子串
    // 解法1：当作滑动窗口问题，滑动窗口问题，假设窗口起点为a时，满足回文性质的子串窗口终点为b，即(a,b)为起点为a的最长回文子串，则起点为a+1时，(a+1, b-1)必然是回文子串，从b-1开始向后尝试即可
    // 至于回文性质的验证，从中心进行对比即可，本题和最长不重复子串是一套解法
    public String longestPalindrome(String s) {
        if (s.length() == 1) {
            return s;
        }
        // 非空字符串的最长回文子串最小长度为1
        String res = String.valueOf(s.charAt(0));
        int maxlen = 1;
        int lastend = 0;
        for (int start = 0; start < s.length(); start++) {
            for (int end = lastend; end < s.length(); end++) {
                if (start <= end) {
                    String substr = s.substring(start, end + 1);
                    if (isVaildSubSeq(substr)) {
                        if (end - start + 1 > maxlen) {
                            maxlen = end - start + 1;
                            res = substr;
                            lastend = end;
                        }
                    }
                }
            }
        }
        return res;
    }

    // 解法2：使用动态规划方法解，解法1滑动窗口方法并没有完整应用回文子串的性质，只是动态规划转移表达式的一部分
    // 利用回文串的性质，任何一个回文串都是由现有的回文串，通过左右两侧加入一个相同字符生成的，最基础的简单回文串，是单个字符。
    // 为了解决偶数个字符的回文串的问题，不妨把空串也视作回文串，因此奇数个字符的回文串均是由单个字符的字符串，通过左右两侧加入相同字符，重复一定次数完成；偶数个字符的回文串则是由空串，通过左右两侧加入相同字符，重复一定次数完成。
    // 假设起点索引a，终点索引值b的回文串记作(a,b)，则(a, b)，是由回文串(a+1, b-1)，在左右两侧加入一个相同字符生成。(a,b)为回文串记作s(a,b)=true，则有：
    // s(a, b) = true, 如果a>=b (a==b为单个字符的字符串，a>b为空串)
    // s(a, b) = (s[a] == s[b]) && s(a+1, b-1)
    // 以横轴为起点值索引，纵轴为终点值索引，形成正方形矩阵，根据上述性质1，二维矩阵对角线以及对角线以上元素值均为true。接下来根据性质2确定迭代顺序，因为s(a,b)可由s(a+1, b-1)确定，即一个点由其右上角的点决定，且只需要考虑a<=b，即对角线以下的下半区，因此从上到下按行更新，每个行内从左到右按列更新即可。
    public String longestPalindrome2(String s) {
        if (s.length() == 1) {
            return s;
        }
        // mat[i][j]表示子串(i, j)是否为回文
        boolean[][] mat = new boolean[s.length()][s.length()];
        // 初始化边界值，单字符的字符串，以及空串(j<i)都是回文串
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                if (j <= i) {
                    mat[i][j] = true;
                }
            }
        }
        // 迭代计算矩阵，矩阵对角线以下横向一排排从左到右计算
        for (int j = 0; j < s.length(); j++) {
            for (int i = 0; i < j; i++) {
                mat[i][j] = mat[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
            }
        }

        String res = String.valueOf(s.charAt(0));
        int maxLength = 1;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                if (j >= i && mat[i][j] && j - i + 1 > maxLength) {
                    maxLength = j - i + 1;
                    res = s.substring(i, j + 1);
                }
            }
        }
        return res;
    }

    // 判断单个字符串是否为回文子串
    private boolean isVaildSubSeq(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() == 1) {
            return true;
        }
        int pos1 = 0, pos2 = s.length() - 1;
        while (pos1 <= pos2) {
            if (s.charAt(pos1) != s.charAt(pos2)) {
                return false;
            } else {
                pos1 += 1;
                pos2 -= 1;
            }
        }
        return true;
    }
}