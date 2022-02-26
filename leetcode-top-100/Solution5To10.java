package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution5To10 {

    // 10. 正则表达式匹配
    // 递归子问题：典型的递归子问题，可以将问题转化为子问题，利用递归解决，得到递归方法后也可以转化为动态规划问题来解决
    // 1. 若当前正则表达式p的第二个字符不为'*'，则判断s[0]和p[0]的单个字符是否匹配即可，即isSingleMatch(s[0], p[0])，然后转化为子问题isMatch(s[1:], p[1:])
    // 2. 若当前正则表达式p的第二个字符为p，则分为两种情况：
    //  (1) 若s[0]和p[0]不匹配，即isSingleMatch(s[0], p[0])==false，这时只能舍弃正则表达式中的'*'，不匹配s中的字符，然后转化为子问题isMatch(s, p[2:])
    //  (2) 若s[0]和p[0]匹配，即isSingleMatch(s[0], p[0])==true，这时可以舍弃正则表达式中的'*'不匹配s中的字符，然后转化为子问题isMatch(s, p[2:])，也可以让正则表达式中的'*'匹配s[0]，接着继续匹配，然后转化为子问题isMatch(s[1:], p)
    // 3. 递归的终止条件是正则表达式p为空串，在转化为子问题时，尤其需要注意字符串s为空的情况
    public boolean isMatch(String s, String p) {
        if (p == null || p.equals("")) {
            return s == null || s.equals("");
        } else {
            if (p.length() > 1 && p.charAt(1) == '*') {
                if (s == null || s.equals("")) {
                    return isMatch(s, p.substring(2));     // 特别注意s为空字符串的情况，这时只能舍弃'*'
                } else if (isSingleMatch(s.charAt(0), p.charAt(0))) {
                    return isMatch(s, p.substring(2)) || isMatch(s.substring(1), p);
                } else {
                    return isMatch(s, p.substring(2));
                }
            } else {
                if (s == null || s.equals("")) {
                    return false;   // 特别注意s为空字符串时的情况，这时正则表达式开头没有'*'，必然匹配失败
                } else {
                    return isSingleMatch(s.charAt(0), p.charAt(0)) && isMatch(s.substring(1), p.substring(1));
                }
            }
        }
    }

    // 判断单个字符是否满足匹配
    // 若正则表达式字符为普通字符，则匹配对应相同的字符，若正则表达式字符为'.'，则匹配任意一个字符
    public boolean isSingleMatch(Character s, Character p) {
        return p == '.' || s == p;
    }


    // 11. 盛最多水的容器
    // 典型双指针问题：使用左右两个指针分别代表容器的左右侧，因为水平面取决于左右侧中较小的一个，而双指针移动过程中容器底面积缩小，需要水平面上升
    // 因此每次移动时，只需要移动两个指针中较小的那个到下一个位置，两个相等则移动任一个即可，然后更新计算容器盛水大小，最终得到最大值即可
    public int maxArea(int[] height) {
        int leftpos = 0, rightpos = height.length - 1;
        int res = 0;
        while (leftpos < rightpos) {
            res = Math.max(res, (rightpos - leftpos) * Math.min(height[leftpos], height[rightpos]));
            if (height[leftpos] <= height[rightpos]) {
                leftpos += 1;
            } else {
                rightpos -= 1;
            }
        }
        return res;
    }

    // 15. 三数之和
    // 先取出数组中的一个数nums[i]，然后问题就转化为从数组中选择两个数（注意不能选择刚取出的nums[i]）相加等于-nums[i]，先将数组排序后再使用双指针法即可
    // 特别地，保证结果中不出现重复的办法是，每次得到三个数相加等于0时，将其中最小的和最大的两个组成对扔到哈希表里即可，由这两个数即可确定第三个数
    public List<List<Integer>> threeSum(int[] nums) {
        HashSet<Pair<Integer, Integer>> hashSet = new HashSet<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int begin = 0, end = nums.length - 1;
            while (begin < end) {
                if (begin == i) {
                    begin += 1;
                } else if (end == i) {
                    end -= 1;
                } else if (nums[begin] + nums[end] > -nums[i]) {
                    end -= 1;
                } else if (nums[begin] + nums[end] < -nums[i]) {
                    begin += 1;
                } else {
                    hashSet.add(new Pair<>(
                            Math.min(Math.min(nums[begin], nums[end]), nums[i]),
                            Math.max(Math.max(nums[begin], nums[end]), nums[i])));
                    begin += 1;
                    end -= 1;
                }
            }
        }
        List<List<Integer>> res = new ArrayList<>();
        for (Pair<Integer, Integer> entry: hashSet) {
            List<Integer> lis = new ArrayList<>();
            lis.add(entry.getKey());
            lis.add(entry.getValue());
            lis.add(- entry.getKey() - entry.getValue());
            res.add(lis);
        }
        return res;
    }

    // 17. 电话号码的字母组合
    // 递归子问题：对于长度为n的digits字符串，直接在digits[:n]的生成的结果基础上，以此加上最后一个数字对应的几种字符即可
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits == null || "".equals(digits)) {
            return res;
        } else if (digits.length() == 1) {
            switch (digits.charAt(0)) {
                case '1': {
                    res.add("");
                    break;
                }
                case '2': {
                    res.add("a");
                    res.add("b");
                    res.add("c");
                    break;
                }
                case '3': {
                    res.add("d");
                    res.add("e");
                    res.add("f");
                    break;
                }
                case '4': {
                    res.add("g");
                    res.add("h");
                    res.add("i");
                    break;
                }
                case '5': {
                    res.add("j");
                    res.add("k");
                    res.add("l");
                    break;
                }
                case '6': {
                    res.add("m");
                    res.add("n");
                    res.add("o");
                    break;
                }
                case '7': {
                    res.add("p");
                    res.add("q");
                    res.add("r");
                    res.add("s");
                    break;
                }
                case '8': {
                    res.add("t");
                    res.add("u");
                    res.add("v");
                    break;
                }
                case '9': {
                    res.add("w");
                    res.add("x");
                    res.add("y");
                    res.add("z");
                    break;
                }
            }
            return res;
        } else {
            List<String> lastRes = letterCombinations(digits.substring(0, digits.length() - 1));
            List<String> nowRes = letterCombinations(String.valueOf(digits.charAt(digits.length() - 1)));
            for (String lastStr: lastRes) {
                for (String nowStr: nowRes) {
                    res.add(lastStr + nowStr);
                }
            }
            return res;
        }
    }

    // 19. 删除链表的倒数第 N 个结点
    // 双指针法典型题：使用快慢双指针begin和end，begin指向链表的第一个节点，end指向链表的最后一个节点，同时移动指针begin和end，当end指向链表末尾null时，begin恰好指向倒数第n个节点
    // 特别地，为了快速删除最后指向倒数第n个节点的begin，需要另一个临时指针指向begin的之前的一个节点
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) {
            return null;
        }

        ListNode begin = head, end = head, temp = null;
        for (int i = 0; i < n; i++) {
            end = end.next;
        }

        while (end != null) {
            temp = begin;
            begin = begin.next;
            end = end.next;
        }

        if (temp == null) {
            // 需要删除的结点是第一个节点
            head = begin.next;
        } else {
            temp.next = begin.next;
        }
        return head;
    }
}
