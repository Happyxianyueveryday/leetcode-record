package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution65To70 {

    // 300. 最长递增子序列
    // 一维动态规划：设dp[i]表示以下标i的数值nums[i]结尾的最长递增子序列的长度
    // 这个设法在之前的最长递增子串，最大和子串，最大乘积子串中都是一样的，需要特别注意这种设法
    // 这里的动态规划算法是，对于给定的i和每一个k满足k<i，如果nums[i]大于nums[k]，则可以将nums[i]加入到dp[k]对应的最长递增子序列的后面，dp[i] = dp[k] + 1
    // 除了上述情况，如果找不到满足这样条件的k，则nums[i]单独作为一个只有一个元素的最长递增子序列
    // 动态规划状态转移方程可以简单表示为：dp[i] = max(dp[k] + 1, 1)，其中k满足k<i且nums[i]>nums[k]
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];    // dp[i]表示以下标i结尾的最长递增子序列长度
        dp[0] = 1;

        for (int i = 1; i < nums.length; i++) {
            int maxLength = 1;  // 最小的情况就是nums[i]单独作为一个最长递增子序列
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    maxLength = Math.max(maxLength, dp[j] + 1);
                }
            }
            dp[i] = maxLength;
        }

        int res = 1;
        for (int i = 0; i < nums.length; i++) {
            res = Math.max(res, dp[i]);
        }

        return res;
    }

    // 347. 前 K 个高频元素
    // 优先队列的应用：前K个最大元素，前K个满足某一种条件的元素，这类问题比较多的都是优先队列/堆来求解
    // 本题中先遍历一遍数组，使用哈希表统计各个数字的出现次数，然后由哈希表按照出现次数的大小构造优先队列，即可进行求解，这里的实现直接使用java内部的工具包，关于堆算法的实现可以参考数据结构部分的练习
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {     // 记录各个数值的出现次数
            hashMap.put(nums[i], hashMap.getOrDefault(nums[i], 0) + 1);
        }

        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });    // 以各个数值的出现次数来构造优先队列
        for (Integer key: hashMap.keySet()) {
            queue.offer(new Pair<>(key, hashMap.get(key)));
        }

        int[] res = new int[k];
        for (int i = 0; i < k; i++) {       // 从优先队列出堆前k个元素
            res[i] = queue.poll().getKey();
        }

        return res;
    }

    // 337. 打家劫舍 III
    // 递归子问题：本题一开始做很容易觉得先层次遍历求树每层的和，然后将问题转化为一维数组的打家劫舍进行一维动态规划求解，但其实从用例可以看到不行
    // 对于输入的树root，可以选择偷窃根节点root，然后在子树root.left.left，root.left.right，root.right.left，root.right.right四个子树中窃取到最大值
    // 或者不偷窃根节点root，直接在子树root.left和子树root.right中窃取到最大值
    // 上述算法直接转化为代码会超时，因为重复计算了很多子树的最大偷窃值，这里推荐使用哈希表，以子树根节点作为键，最大偷窃值作为值，将递归过程子树的计算结果放入哈希表，避免重复计算
    // 至于非递归方法的树形dp这里暂时不使用
    private HashMap<TreeNode, Integer> hashMap = new HashMap<>();
    public int rob(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val;
        } else {
            if (hashMap.containsKey(root)) {
                return hashMap.get(root);
            }
            int leftTreeRob = 0, rightTreeRob = 0;
            int leftLeftTreeRob = 0, leftRightTreeRob = 0;
            int rightLeftTreeRob = 0, rightRightTreeRob = 0;
            if (root.left != null) {
                leftTreeRob = rob(root.left);
                hashMap.put(root.left, leftTreeRob);
            }
            if (root.right != null) {
                rightTreeRob = rob(root.right);
                hashMap.put(root.right, rightTreeRob);
            }
            if (root.left != null && root.left.left != null) {
                leftLeftTreeRob = rob(root.left.left);
                hashMap.put(root.left.left, leftLeftTreeRob);
            }
            if (root.left != null && root.left.right != null) {
                leftRightTreeRob = rob(root.left.right);
                hashMap.put(root.left.right, leftRightTreeRob);
            }
            if (root.right != null && root.right.left != null) {
                rightLeftTreeRob = rob(root.right.left);
                hashMap.put(root.right.left, rightLeftTreeRob);
            }
            if (root.right != null && root.right.right != null) {
                rightRightTreeRob = rob(root.right.right);
                hashMap.put(root.right.right, rightRightTreeRob);
            }
            return Math.max(root.val + leftLeftTreeRob + leftRightTreeRob + rightLeftTreeRob + rightRightTreeRob,
                    leftTreeRob + rightTreeRob);
        }
    }

    // 461. 汉明距离
    // 二进制转化：比较基础的题目，将两数直接位异或操作，然后统计结果中二进制表示1的数量即可
    public int hammingDistance(int x, int y) {
        int xor = x ^ y;
        int res = 0;
        while (xor > 0) {
            res += xor % 2;
            xor /= 2;
        }
        return res;
    }

    // 399. 除法求值
    // 有向带权图的深度优先搜索: 本题的核心点在于将问题抽象表达为数据结构，对于每个输入的除法表达式a/b=k，可以在图中建立从a到b的有向边，权重为k表示a=b*k，同时反向建立从b到a的有向边，权重为1/k表示b=a*1/k
    // 然后假设问题是求解x/y=z，则相当于求解x=y*z中的z，最优方法是求解最短路径，由于本题的数据量不大，也可以直接从图中的x点出发进行深度优先搜索，如果能到达点y，这时将路径中所有有向边的权重相乘即得到需要的最终结果z
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        HashMap<Pair<String, String>, Pair<Double, Double>> map = new HashMap<>();    // 构造有向带权重图，键为有向边，值为分数表示的有向边权重
        for (int i = 0; i < equations.size(); i++) {    // 假设a/b=k，则放入a到b的权重为k/1，b到a的权重为1/k，使用分数的原因是避免除法带来的精度损失
            map.put(new Pair<>(equations.get(i).get(0), equations.get(i).get(1)),
                    new Pair<>(values[i], 1.0));
            map.put(new Pair<>(equations.get(i).get(1), equations.get(i).get(0)),
                    new Pair<>(1.0, values[i]));
        }

        Stack<Pair<String, Double>> stack = new Stack<>();    // 图的非递归深度优先搜索，栈中为当前节点位置和从起点到该点的比值
        HashSet<String> isVisited = new HashSet<>();          // 已经访问过的节点集合
        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {

            isVisited.clear();

            String start = queries.get(i).get(0);       // 起点
            String end = queries.get(i).get(1);         // 终点

            double result = -1f;

            for (Pair<String, String> path: map.keySet()) {
                if (path.getKey().equals(start)) {
                    stack.push(new Pair<>(start, 1.0));     // 这里题目要求a/a的情况，假设a不在图中，则返回-1而不是1
                }
            }

            while (!stack.isEmpty()) {
                Pair<String, Double> current = stack.pop();

                if (current.getKey().equals(end)) {     // 当前节点就是终点，则搜索结束
                    result = current.getValue();
                }

                isVisited.add(current.getKey());        // 当前节点设置为已经访问
                double curVal = current.getValue();

                for (Pair<String, String> path: map.keySet()) {
                    if (current.getKey().equals(path.getKey()) && !isVisited.contains(path.getValue())) {
                        Pair<Double, Double> params = map.get(path);    // 路径权重值
                        stack.push(new Pair<>(path.getValue(), curVal * params.getKey() / params.getValue()));
                    }
                }
            }

            res[i] = result;
        }

        return res;
    }


}
