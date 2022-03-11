package com.company;

import javafx.util.Pair;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Solution20To25 {

    // 53. 最大子数组和
    // 一维动态规划：本题和之前的最长有效子括号使用的方法是一致的，均是以dp[i]作为以i为结尾的最大子数组的和，然后展开一维动态规划
    // 一维动态规划算法也很简单，对于dp[i]，如果以i-1为结尾的最大子数组的和大于等于0，那么以i为结尾的最大子数组就是以i-1为结尾的最大子数组加上位置i的数字nums[i]，否则以i为结尾的最大子数组就是nums[i]单个数字组成的数组
    public int maxSubArray(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (dp[i - 1] >= 0) {
                dp[i] = dp[i - 1] + nums[i];
            } else {
                dp[i] = nums[i];
            }
        }
        int res = Integer.MIN_VALUE;
        for (int i : dp) {
            res = Math.max(res, i);
        }
        return res;
    }

    // 55. 跳跃游戏
    // 一维动态规划：本题仍然是标准的一维动态规划，以dp[i]表示下标为i的位置是否可以到达
    // 算法可以简单表述为：对于位置dp[i]，只需要遍历小于i的所有下标j，如果dp[j]==true，即位置j可达，且nums[j] >= j - i，即位置j的最大可用步数大于i和j之间的距离，则位置i可达
    public boolean canJump(int[] nums) {
        boolean[] dp = new boolean[nums.length];
        dp[0] = true;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (dp[j] && nums[j] >= i - j) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[nums.length - 1];
    }

    // 56. 合并区间
    // 典型双指针法：需要用双指针法解决任务，这时必须保证双指针指向的区间是按从左到右有序的，因此首先需要对区间的左端点进行排序
    // 本题的算法如下：
    // 1. 首先按照区间左端从小到大对区间进行排序，这样保证从前往后遍历时，区间左端是不断增大的
    // 2. 创建双指针begin指向第一个区间左端，end指向第一个区间右端
    // 3. 遍历所有区间，
    //    (1) 若当前区间[a,b]的左端a小于等于end，又因为经过排序当前区间的左端a必然大于或等于begin，则[a,b]和[begin,end]有交集，这时更新[begin,end]=[begin, max(end,b)]
    //    (2) 若当前区间[a,b]的左端a大于end，则区间[a,b]和区间[begin,end]无交集，这时直接将[begin,end]放入结果中，然后更新[begin,end]=[a,b]
    public int[][] merge(int[][] intervals) {
        List<Pair<Integer, Integer>> pairList = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            pairList.add(new Pair<>(intervals[i][0], intervals[i][1]));
        }
        pairList.sort(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        List<Pair<Integer, Integer>> res = new ArrayList<>();
        int begin = pairList.get(0).getKey(), end = pairList.get(0).getValue();
        for (Pair<Integer, Integer> entry: pairList) {
            if (entry.getKey() <= end) {    // 新区间和当前区间有重叠，取两个区间中的较大区间
                end = Math.max(end, entry.getValue());
            } else {    // 新区间和当前区间无重叠，将当前区间放入结果，然后更新当前区间
                res.add(new Pair<>(begin, end));
                begin = entry.getKey();
                end = entry.getValue();
            }
        }
        res.add(new Pair<>(begin, end));

        int[][] finalRes = new int[res.size()][2];
        for (int i = 0; i < res.size(); i++) {
            finalRes[i][0] = res.get(i).getKey();
            finalRes[i][1] = res.get(i).getValue();
        }
        return finalRes;
    }

    // 62. 不同路径
    // 二维动态规划：很简单的二维动态规划问题，dp[i][j]作为第i行第j列位置的路径总数，因为只能向右或者向下移动，因此dp[i][j]=dp[i-1][j]+dp[i][j-1]
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    dp[0][0] = 1;
                } else {
                    int top = (i - 1 >= 0)? dp[i - 1][j]: 0;
                    int left = (j - 1 >= 0)? dp[i][j - 1]: 0;
                    dp[i][j] = top + left;
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    // 64. 最小路径和
    // 二维动态规划：和上题的方式类似，公式为dp[i][j]=min(dp[i-1][j], dp[i][j-1]) + grid[i][j]，特别的，需要小心当(i-1)或者(j-1)超出边界时，需要使用INT_MAX代替
    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int[][] dp = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 && j == 0) {
                    dp[i][j] = grid[0][0];
                } else {
                    int top = (i - 1 >= 0)? dp[i - 1][j]: Integer.MAX_VALUE;
                    int left = (j - 1 >= 0)? dp[i][j - 1]: Integer.MAX_VALUE;
                    dp[i][j] = Math.min(top, left) + grid[i][j];
                }
            }
        }
        return dp[grid.length - 1][grid[0].length - 1];
    }

}
