package com.company;

import java.util.*;

public class Solution10To15 {

    // 20. 有效的括号
    // 栈的基本应用：计科大一基础题
    // 从左到右遍历字符串，遇到左括号入栈，遇到右括号出栈栈顶元素比较是否和当前右括号匹配，若栈此时为空或者栈顶元素不匹配，则括号无效，遍历结束栈为空则为有效的括号，否则为无效的括号
    public boolean isValid(String s) {
        if (s == null) {
            return true;
        }

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                stack.push(s.charAt(i));
            } else if (s.charAt(i) == ')') {
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (s.charAt(i) == ']') {
                if (!stack.isEmpty() && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (s.charAt(i) == '}') {
                if (!stack.isEmpty() && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    // 21. 合并两个有序链表
    // 归并排序算法：基础题，归并排序算法中的归并部分
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode pos1 = list1, pos2 = list2;
        ListNode res = new ListNode();
        ListNode resPos = res;

        while (pos1 != null && pos2 != null) {
            if (pos1.val <= pos2.val) {
                resPos.next = pos1;
                pos1 = pos1.next;
            } else {
                resPos.next = pos2;
                pos2 = pos2.next;
            }
            resPos = resPos.next;
        }
        while (pos1 != null) {
            resPos.next = pos1;
            pos1 = pos1.next;
            resPos = resPos.next;
        }
        while (pos2 != null) {
            resPos.next = pos2;
            pos2 = pos2.next;
            resPos = resPos.next;
        }

        return res.next;
    }

    // 22. 括号生成
    // 递归子问题：括号对数为n的所有可能的括号可以由括号对数小于n的括号通过一定的操作生成，有如下两种方式：
    // 1. 由括号对数为a和括号对数为b的所有可能的括号左右拼接而来，其中a+b==n，例如括号对数为5的所有可能的括号，可以拆成对数为1的括号+对数为4的括号，对数为2的括号加上对数为3的括号，左右拼接而成
    // 2. 括号对数为n-1的所有可能的括号，在左右分别加左括号和右括号复合而来
    // 相对的，根据上述的递归子问题性质，很容易将这个递归问题转化为动态规划，注意使用哈希表来排除重复生成的括号即可
    public List<String> generateParenthesis(int n) {
        HashMap<Integer, HashSet<String>> hashMap = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            HashSet<String> hashSet= new HashSet<>();
            if (i == 1) {
                hashSet.add("()");
            } else {
                // 左右拼接的情况
                for (int j = 1; j <= i / 2; j++) {
                    HashSet<String> firstSet = hashMap.get(j);
                    HashSet<String> secondSet = hashMap.get(i - j);
                    for (String firstStr: firstSet) {
                        for (String secondStr: secondSet) {
                            hashSet.add(firstStr + secondStr);
                            hashSet.add(secondStr + firstStr);
                        }
                    }
                }
                // 由n-1对的括号外侧分别加括号叠加而成
                HashSet<String> lastSet = hashMap.get(i - 1);
                for (String lastStr: lastSet) {
                    hashSet.add("(" + lastStr + ")");
                }
            }
            hashMap.put(i, hashSet);
        }
        return new ArrayList<>(hashMap.get(n));
    }

    // 23. 合并K个升序链表
    // 多路归并排序：数据结构的基本内容，整体算法和二路归并排序类似，只是每次需要筛选出多路中首个元素最小的那个，因此需要使用到堆，也即优先队列
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;
            }
        });
        ListNode res = new ListNode();
        ListNode resPos = res;

        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                priorityQueue.offer(lists[i]);  // 优先队列禁止插入空值
            }
        }

        while (!priorityQueue.isEmpty()) {
            ListNode top = priorityQueue.peek();
            resPos.next = new ListNode(top.val);
            resPos = resPos.next;
            top = top.next;
            priorityQueue.poll();
            if (top != null) {
                priorityQueue.offer(top);
            }
        }
        return res.next;
    }

    // 32. 最长有效括号
    // 二维动态规划：和上面的有效括号有一定联系，参考有效括号递归子问题的生成方法，二维动态规划会超时，无法ac该题，只作为参考
    // 特别注意的是，动态规划表达式要推的结果一定是mat[i][j]，要写的表达式左侧必须是mat[i][j]，不能是i+1，j+1，才能保证确定迭代方向能正确
    // 假设mat[i][j]表示从下标i到下标j的子串是否为有效括号，i代表矩阵行，j代表矩阵列，则有:
    // 1. 若mat[i][j]中(j-i+1)为奇数，则必然不是有效括号，因为有效括号字符串必然是偶数
    // 2. 若mat[i+1][j-1] == true，且s[i]为左括号，s[j]为右括号，则mat[i][j] == true，对应在现有的括号外加一层的括号组成新的有效括号的情况
    // 3. 若mat[i][k] == true，且mat[k+1][j] == true，其中i<=k<=j，则mat[i][j] == true，对应由两个括号组成新的有效括号的情况
    // 若(j-i+k)为偶数，则mat[i][j]由情况2和情况3成或的关系决定m
    // 下面确定迭代方向，还是按照标准流程来，看mat[i][j]由哪个方向上的元素决定，动态规划数组的规模为边长为s.length的正方形，已知：
    // 1. j<i时，mat[i][j] == true，j == i时，mat[i][j] == false，也即矩阵对角线以及对角线以下的值均确定
    // 对于情况2，mat[i][j]由左下方的mat[i-1][j+1]决定
    // 对于情况3，mat[i][j]由正左方的mat[i][k]和正下方的mat[k+1][j]决定，因为i<=k<=j
    // 因此迭代方向必须是从下到上遍历每行，在每行内从左到右遍历
    public int longestValidParentheses(String s) {
        boolean[][] mat = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j <= i; j++) {
                mat[i][j] = (j < i);
            }
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            for (int j = i + 1; j < s.length(); j++) {
                if ((j - i + 1) % 2 == 1) {
                    mat[i][j] = false;
                } else if (s.charAt(i) == ')' || s.charAt(j) == '(') {
                    mat[i][j] = false;
                } else {
                    if (i + 1 < s.length()
                            && mat[i + 1][j - 1] && s.charAt(i) == '(' && s.charAt(j) == ')') {
                        mat[i][j] = true;  // mat[i][j]由右上方的mat[i-1][j+1]决定
                    } else {
                        mat[i][j] = false; // mat[i][j]由正左方的mat[i][k]和正下方的mat[k+1][j]决定
                        for (int k = i; k < j; k++) {
                            mat[i][j] = mat[i][j] || (mat[i][k] && mat[k + 1][j]);
                        }
                    }
                }
            }
        }
        int maxLength = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                if (mat[i][j]) {
                    maxLength = Math.max(maxLength, j - i + 1);
                }
            }
        }
        return maxLength;
    }

    // 32. 最长有效括号
    // 一维动态规划：上述的二维动态规划实际提交会超时，本题可以将问题进一步简化为一维动态规划
    // 假设dp[i]表示以下标i结尾的最长有效括号的长度，主要考虑子串的最后两个字符，考虑如何递推得到dp[i]
    // 1. 假设s[i] == ')'且s[i-1] == '('，即s[:i]的形式为"......()"，
    //    这时因为dp[i-2]对应的最长子串是以i-2下标为结尾的最长有效括号，因此有dp[i] = dp[i-2] + 2
    // 2. 假设s[i] == ')'且s[i-1] == ')'，即s[:i]的形式为"......))"，
    //    这时因为dp[i-1]对应的最长子串是以i-1下标为结尾的最长有效括号，因此只要该最长子串的前一个位置即s[i - 1 - dp[i-1]] == '('和s[i]匹配，则可以由dp[i-1]子串的前一个位置和dp[i-1]对应子串拼接，则dp[i] =  dp[i-1] + dp[i - 2 - dp[i-1]]
    // 3. 其他情况必然不存在以下标i结尾的任何有效括号，因此dp[i] = 0
    public int longestValidParentheses2(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }
        int[] dp = new int[s.length()];
        dp[0] = 0;   // 第一个字符结尾的最长有效括号的长度必然为0
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == ')' && s.charAt(i - 1) == '(') { // 形如"......()"的情况
                dp[i] = (i - 2 >= 0? dp[i - 2]: 0) + 2;
            } else if (s.charAt(i) == ')' && s.charAt(i) == ')') {  // 形如"......))"的情况
                dp[i] = (i - 1 - dp[i - 1] >= 0 && s.charAt(i - 1 - dp[i - 1]) == '(')?
                        dp[i - 1] + ((i - 2 - dp[i - 1] >= 0)? dp[i - 2 - dp[i - 1]]: 0) + 2: 0;
            } else {
                dp[i] = 0;
            }
        }
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            res = Math.max(res, dp[i]);
        }
        return res;
    }
}
