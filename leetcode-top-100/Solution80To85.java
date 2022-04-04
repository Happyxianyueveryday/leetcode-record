package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Solution80To85 {

    // 221. 最大正方形
    // 二维动态规划：假设dp[i][j]表示右下角为(i,j)的最大正方形边长，则有如下的状态转移方程
    // (1) 若matrix[i][j]==0，则这时以(i,j)为右下角无法组成任何正方形，因此dp[i][j]=0
    // (2) 若matrix[i][j]==1，则这时的最大正方形边长是和(i,j)左方，上方和左上方的最大正方形边长中的最小值加1，即dp[i][j]=min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
    // 然后再考虑二维动态规划中的基础情况，即矩阵中的第一行和第一列，对于第一行和第一列的元素而言，显然若matrix[i][j]=1时dp[i][j]=1，否则不存在正方形，dp[i][j]=0
    // 这里简单解释下第二个状态转移方程，当(i,j)为某个正方形的右下角时，其左上以及左上方三个位置也必须是该正方形的右下角，所以以(i,j)为右下角的正方形边长就是这三个位置中的最小值加1
    public int maximalSquare(char[][] matrix) {
        int[][] dp = new int[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {   // 初始化矩阵第一列
            if (matrix[i][0] == '1') {
                dp[i][0] = 1;
            } else {
                dp[i][0] = 0;
            }
        }

        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[0][j] == '1') {
                dp[0][j] = 1;
            } else {
                dp[0][j] = 0;
            }
        }

        for (int i = 1; i < matrix.length; i++) {   // 使用状态转移方程更新
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == '1') {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        int res = 0;
        for (int i = 0; i < matrix.length; i++) {   // 在动态规划矩阵中查找到最大的边长值就可以得到最大面积值
            for (int j = 0; j < matrix[0].length; j++) {
                res = Math.max(res, dp[i][j] * dp[i][j]);
            }
        }

        return res;
    }

    // 581. 最短无序连续子数组
    // 数组排序算法：在O(nlogn)的时间复杂度下很容易找到算法，直接对原数组进行排序后，再顺序遍历一遍，找到第一个位置和最后一个位置对应的元素和排序后的数组不相同的即可。下面给出一种O(n)的解法
    // 从左到右遍历数组，并且同时维护当前元素之前的数组中的最大值，遍历过程中最后一个小于当前最大值的元素，就是无序片段的尾部。同理，从右到左遍历数组，并且同时维护当前元素之后的数组中的最小值，遍历过程中最后一个二大于当前最小值的元素，就是无序片段的开头。
    public int findUnsortedSubarray(int[] nums) {
        int maxValue = Integer.MIN_VALUE;
        int endPos = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < maxValue) {
                endPos = i;
            } else {
                maxValue = Math.max(maxValue, nums[i]);
            }
        }

        int minValue = Integer.MAX_VALUE;
        int startPos = -1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] > minValue) {
                startPos = i;
            } else {
                minValue = Math.min(minValue, nums[i]);
            }
        }

        return startPos != -1 && endPos != -1? endPos - startPos + 1: 0;
    }

    // 42. 接雨水
    // 一维动态规划：本题的关键核心思路是，下标为i的位置能接到雨水的高度，就是该位置左右两侧最大高度中的较小值，减去当前位置的高度，根据这个原理不难实现时间复杂度为O(n^2)的算法，下面提供使用动态规划将时间复杂度降低到O(n)的方法
    // 使用leftDp[i]表示位置i左侧的最大值，rightDp[i]表示位置i右侧的最大值，则有dpLeft[i]=max(height[i-1], dpLeft[i-1])，以及有dpRight[i]=max(height[i+1],dpRight[i+1])，dpLeft从左到右进行更新，dpRight从右到左进行更新
    public int trap(int[] height) {
        int[] dpLeft = new int[height.length];
        for (int i = 0; i < height.length; i++) {
            if (i == 0) {   // 下标0的位置左侧不存在最大值，记作0即可
                dpLeft[i] = 0;
            } else {
                dpLeft[i] = Math.max(dpLeft[i - 1], height[i - 1]);     // 注意下标的左侧i不包括i自身
            }
        }

        int[] dpRight = new int[height.length];
        for (int i = height.length - 1; i >= 0; i--) {
            if (i == height.length - 1) {
                dpRight[i] = 0;
            } else {
                dpRight[i] = Math.max(dpRight[i + 1], height[i + 1]);
            }
        }

        int[] res = new int[height.length];
        for (int i = 0; i < height.length; i++) {
            res[i] = Math.max(Math.min(dpLeft[i], dpRight[i]) - height[i], 0);  // 特别的，位置i的最小接雨水高度为0
        }

        int sum = 0;
        for (int num: res) {
            sum += num;
        }

        return sum;
    }

    // 128. 最长连续序列
    // 哈希表应用：本题很容易得到一个时间复杂度为O(nlogn)的算法，即对元素进行排序，但排序的方法不符合题目O(n)的时间复杂度要求，使用哈希表的算法如下
    // (1) 创建一个哈希表，将数组元素均放入哈希表中，这样查找一个元素是否存在只需要O(1)的时间
    // (2) 遍历给定数组中的每一个元素，对于元素nums[i]，判断nums[i]的前驱值nums[i]-1是否存在于哈希表
    // (3) 若nums[i]-1存在，则这时可以从nums[i]-1开始往后依次枚举来尝试下一个元素是否存在，不必从nums[i]开始，因此直接略过
    // (4) 若nums[i]-1不存在，则从nums[i]开始依次枚举尝试下一个元素是否存在，并记录连续序列的长度
    // (5) 遍历结束后，记录连续序列的最大长度即可
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            hashSet.add(nums[i]);
        }
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (hashSet.contains(nums[i] - 1)) {    // 存在当前元素的前驱元素，则从前驱元素开始枚举即可
                continue;
            } else {    // 不存在当前元素的前驱元素，从当前元素开始枚举
                int length = 1;
                int num = nums[i] + 1;
                while (hashSet.contains(num)) {
                    length += 1;
                    num += 1;
                }
                res = Math.max(res, length);
            }
        }
        return res;
    }

    // 31. 下一个排列
    // 数组交换操作：下一个排列实际上就是把nums数组看作是一个数，需要查找到在这些数组成的全排列中，大于当前元素的最小值
    // 本题的算法是，从数组的末尾进行倒序遍历，对于每一个遍历到的当前元素，再从数组末尾开始倒序查找到第一个小于当前元素的位置，交换该位置和当前元素的位置，然后对该位置之后的数组片段重新进行排序即可
    public void nextPermutation(int[] nums) {
        for (int i = nums.length - 1; i >= 0; i--) {
            for (int j = nums.length - 1; j > i; j--) {
                if (nums[i] < nums[j]) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;

                    Arrays.sort(nums, i + 1, nums.length);
                    return;
                }
            }
        }
        Arrays.sort(nums);
    }

}
