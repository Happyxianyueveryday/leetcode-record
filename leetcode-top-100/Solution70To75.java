package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution70To75 {

    // 494. 目标和
    // 二维动态规划：数组子序列和问题
    // 假设给定数组中的总和为sum，前缀为加号的和为sum1，前缀为减号的和为sum2，则根据题目条件有sum1-sum2=target，又因为sum1+sum2=sum，两式相加得sum1=(target+sum)/2
    // 因此问题转化为典型的数组子序列和问题，即在数组中寻找若干个数，这些数的和刚好等于(target+sum)/2，求解满足该条件的解个数，子序列和问题是很常见的问题，必须熟悉算法，使用二维动态规划求解
    // 设dp[i][j]为在下标小于i的子数组片段中，即前i个数组成的数组中，和为j的子序列个数，则有状态转移方程如下：
    // (1) 若nums[i-1]>j，dp[i][j]=dp[i-1][j]，也即当nums[j]单个数字就大于目标和时，只能不选择nums[i-1]，这时子数组nums[:i]子序列和的个数就是等于子数组nums[:i-1]的子序列和个数
    // (2) 若nums[i-1]<=j，dp[i][j]=dp[i-1][j]+dp[i-1][j-nums[i-1]]，也即当nums[i]单个数字小于等于目标和时，有两种选择，一是选中nums[i-1]，然后在之前的子数组s[:i-1]中凑出j-nums[i]，二是不选中nums[i-1]，然后在之前的子数组s[:i-1]中直接凑出值j
    // 为了正确的使用状态转移方程进行迭代，这里需要将dp[i][j]定义为前i个数字中子序列和为j，而不是下标i之前的数字
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        if ((target + sum) % 2 != 0) {  // 不能被2整除则必定不存在要求解
            return 0;
        }

        int tar = (target + sum) / 2;   // 问题转化为在数组nums中查找子序列和等于tar的子序列个数

        if (tar < 0) {
            return 0;
        }

        int[][] dp = new int[nums.length + 1][tar + 1];     // 初始化dp数组基础情况

        dp[0][0] = 1;
        for (int j = 1; j <= tar; j++) {     // 初始化i == 0的基础情况
            dp[0][j] = 0;
        }

        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= tar; j++) {
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[nums.length][tar];
    }

    // 494. 目标和
    // 二叉树的广度优先搜索：在本题的场景中最终结果由若干个重复步骤组成，每一步都有两个固定选择，这种场景很适合化为二叉树处理，本题中选择即即相加或者相减，所有可能性自然构成二叉树
    // 特别地，不需要实际构造出二叉树，只是使用二叉树的广度优先搜索思想即可
    public int findTargetSumWays2(int[] nums, int target) {
        int res = 0;
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();   // 键为表达式计算到该深度的值，值为当前深度

        queue.add(new Pair<>(0, -1));   // 注意第一个数也可以加或者减，因此深度需要从-1开始

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> current = queue.poll();

            int curVal = current.getKey();
            int curDep = current.getValue();

            if (curDep == nums.length - 1 && curVal == target) {     // 若当前节点是叶子节点且值和目标值相等，则为一个解
                res += 1;
            }

            if (curDep < nums.length - 1) {   // 左子节点表示加上下一层深度的值，右子节点表示减去下一层深度的值
                queue.add(new Pair<>(curVal + nums[curDep + 1], curDep + 1));
                queue.add(new Pair<>(curVal - nums[curDep + 1], curDep + 1));
            }
        }

        return res;
    }

    // 560. 和为 K 的子数组
    // 遍历解：子数组是由开始下标i和结束下标j来确定的，同时遍历过程中，子数组nums[i:j+1]的和可以在子数组nums[i:j]的和上加上nums[j+1]的得到，因此时间复杂度控制在O(n^2)
    public int subarraySum(int[] nums, int k) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum == k) {
                    res += 1;
                }
            }
        }
        return res;
    }

    // 448. 找到所有数组中消失的数字
    // 哈希表应用：使用一个哈希表来记录数组中出现过的数字，然后依次检测1到n中的数字是否在哈希表中即可
    public List<Integer> findDisappearedNumbers(int[] nums) {
        HashSet<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            hashSet.add(nums[i]);
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= nums.length; i++) {
            if (!hashSet.contains(i)) {
                res.add(i);
            }
        }
        return res;
    }

    // 448. 找到所有数组中消失的数字
    // 题目也要求尝试一下仅用O(1)空间复杂度的解法，这里也提供一种对应的解法
    // 利用nums数组自身的空间，遍历一遍nums数组，对于每个元素nums[i]，通过取绝对值将abs(nums[i])-1对应下标的位置置为相反数，一轮结束后，数组中仍然是正数的元素的下标再加1就是消失的数字
    public List<Integer> findDisappearedNumbers2(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            nums[Math.abs(nums[i]) - 1] = -Math.abs(nums[Math.abs(nums[i]) - 1]);
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    // 739. 每日温度
    // 跳跃表：本题实际上就是求解一个跳跃表，假设这个跳跃表为dp，则dp[i]表示dp
    // 根据该性质可以倒序处理数组得到跳跃表，对于dp[i]，设j=i+1，则有：
    // (1) 若temperatures[j] > temperatures[i]，则temperatures[i]的下一个较大元素就是下标j的元素，这时dp[i]=j-i
    // (2) 若temperatures[j] <= temperatures[i]，这时去查找下一个大于temperatures[j]的元素
    //     根据跳跃表dp的性质，若dp[j]==0，则不存在大于temperatures[j]的元素，因此dp[i]=0，若dp[i]!=0，则该元素就在下标j+dp[j]的位置
    public int[] dailyTemperatures(int[] temperatures) {
        int[] dp = new int[temperatures.length];

        for (int i = temperatures.length - 1; i >= 0; i--) {
            int j = i + 1;
            while (j < temperatures.length) {
                if (temperatures[j] > temperatures[i]) {
                    dp[i] = j - i;
                    break;
                } else if (dp[j] == 0) {
                    dp[i] = 0;
                    break;
                } else {
                    j = j + dp[j];
                }
            }
        }

        return dp;
    }


    // 416. 分割等和子集
    // 二维动态规划：求解数组子序列和等于给定值的问题，跟上面的第一题目标和是类似的
    // 目标是求解一个数组子序列，子序列和等于数组nums的元素和的一半
    // 假设dp[i][j]为前i个数字组成的子数组中和为j的子数组数量，则有：
    // (1) 若当前数值比目标和j小或者等于，即nums[i-1]<=j，则dp[i][j]=dp[i-1][j]+dp[i-1][j-nums[i-1]]
    // (2) 若当前数值比目标和j大，nums[i-1]>j，则dp[i][j]=dp[i-1][j]
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) {
            return false;
        }

        int target = sum / 2;
        int[][] dp = new int[nums.length + 1][target + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= target; j++) {
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[nums.length][target] != 0;
    }


}
