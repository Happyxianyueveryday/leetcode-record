package com.company;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solution45To50 {

    // 155. 最小栈
    // 辅助栈：在最小栈中维护两个栈即可，一个栈用于记录正常的栈顺序，另外一个栈按顺序记录另一个栈中的每个元素入栈时对应的最小值即可
    // 本题目是辅助栈方法中比较常见的题型，需要特别注意
    class MinStack {

        private Stack<Integer> stack;
        private Stack<Integer> minStack;

        public MinStack() {
            stack = new Stack<>();
            minStack = new Stack<>();
        }

        public void push(int val) {
            int minValue = !minStack.isEmpty()? minStack.peek(): Integer.MAX_VALUE;  // 当前元素入栈前栈中的最小值
            stack.push(val);
            minStack.push(Math.min(minValue, val));     // 当前元素入栈后栈中的最小值
        }

        public void pop() {
            stack.pop();
            minStack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // 198. 打家劫舍
    // 一维动态规划：最大子串和/最小子数组和问题的衍生问题，只是加上了最大和对应的子串/子数组中元素不能相邻的条件
    // 设dp[i]为最大的偷窃下标为i时的最大偷窃值，则有动态规划转移方程：dp[i] = max(dp[i-2], ..., dp[0]) + nums[i]
    public int rob(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int max = 0;
            for (int j = 2; j <= i; j++) {
                max = Math.max(max, i - j >= 0? dp[i - j]: 0);
            }
            dp[i] = max + nums[i];
        }
        int res = 0;
        for (int element: dp) {
            res = Math.max(res, element);
        }
        return res;
    }

    // 206. 反转链表
    // 链表双指针法：很基础的链表题型，需要非常熟悉，最方便的方法是使用双指针，直接调整两两相邻节点之间的指针方向的方法来反转
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode now = head, prev = null;
        while (now != null) {
            // 反转两两节点之间的指针方向
            ListNode next = now.next;
            now.next = prev;
            // 移动指针到下一组节点
            prev = now;
            now = next;
        }
        head = prev;        // 当now指向空节点时，prev就是反转后的链表首节点
        return head;
    }


    // 200. 岛屿数量
    // 深度优先搜索：本题实际上既可以用图的深度优先搜索，也可以用广度优先搜索，推荐优先使用深度优先搜索，广度优先搜索的题目可以参见Solution25To30.java中的单词搜索问题
    // 深度优先搜索的非递归实现通常使用栈，和二叉树类似，本题的算法只要从每个没有访问过的陆地开始对附近的陆地进行深度优先搜索，搜索到的陆地进行标记，最后搜索的次数就是岛屿的数量
    public int numIslands(char[][] grid) {
        boolean[][] isVisit = new boolean[grid.length][grid[0].length];     // 已访问节点标记
        int res = 0;
        Stack<Pair<Integer, Integer>> stack = new Stack<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1' && !isVisit[i][j]) {
                    stack.push(new Pair<>(i, j));
                    res += 1;

                    while (!stack.isEmpty()) {
                        Pair<Integer, Integer> current = stack.pop();
                        int x = current.getKey();
                        int y = current.getValue();

                        isVisit[x][y] = true;

                        if (x + 1 < grid.length && !isVisit[x + 1][y] && grid[x + 1][y] == '1') {
                            stack.add(new Pair<>(x + 1, y));
                        }
                        if (x - 1 >= 0 && !isVisit[x - 1][y] && grid[x - 1][y] == '1') {
                            stack.add(new Pair<>(x - 1, y));
                        }
                        if (y + 1 < grid[0].length && !isVisit[x][y + 1] && grid[x][y + 1] == '1') {
                            stack.add(new Pair<>(x, y + 1));
                        }
                        if (y - 1 >= 0 && !isVisit[x][y - 1] && grid[x][y - 1] == '1') {
                            stack.add(new Pair<>(x, y - 1));
                        }
                    }
                }
            }
        }
        return res;
    }

    // 169. 多数元素
    // 哈希表计数：哈希表计数或者排序都可以求解
    public int majorityElement(int[] nums) {
        HashMap<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            count.put(nums[i], count.getOrDefault(nums[i], 0) + 1);
        }
        int res = 0;
        for (int key: count.keySet()) {
            if (count.get(key) > Math.floor(nums.length / 2.0)) {
                res = key;
                break;
            }
        }
        return res;
    }

}
