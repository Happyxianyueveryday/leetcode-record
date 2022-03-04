package com.company;

import java.util.*;

public class Solution15To20 {

    // 33. 搜索旋转排序数组
    // 二分查找算法：本题相当经典，是二分查找算法的灵活的运用，旋转后的数组实际上可以看作数组a和数组b的连接组成的新数组，其中数组a，b是递增数组，且a的元素大于b的元素，因此不论二分的中点在数组a还是数组b中，每次总有一侧的数组是递增数组，可以直接二分查找
    // 初始化双指针begin指向数组下标0，end指向最后一个数组元素，进行下列步骤：
    // 1. 计算mid = (begin + end) / 2
    // 2. 综合考虑target, nums[mid], nums[begin], nums[end]之间的关系，考虑的方式是先确定nums[mid]在数组a还是在数组b中，然后考虑target在nums[mid]的左侧还是右侧
    //    (1) nums[begin] <= nums[mid]，则nums[mid]在数组a中，nums[mid]左侧的数为递增序列，若target满足nums[begin] <= target <= nums[end]，直接在nums[mid]左侧进行二分查找，否则递归在nums[mid]右侧进行上述步骤
    //    (2) nums[begin] > nums[mid]，这时说明mid指向的位置在数组b，nums[mid]右侧的数为递增序列，若target满足nums[mid] <= target <= nums[end]，直接在nums[mid]右侧进行二分查找，否则递归在nums[mid]左侧进行上述步骤
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int begin = 0, end = nums.length - 1;
        while (begin <= end) {
            int mid = (begin + end) / 2;
            if (nums[begin] <= nums[mid]) {   // nums[mid]在数组a中
                if (target >= nums[begin] && target <= nums[mid]) {
                    int targetIndex = Arrays.binarySearch(nums, begin, mid + 1, target);    // 注意Arrays.binarySearch查找不到时返回插入点而不是固定-1
                    return targetIndex >= 0? targetIndex: -1;
                } else {
                    begin = mid + 1;
                }
            } else {    // nums[mid]在数组b中
                if (target >= nums[mid] && target <= nums[end]) {
                    int targetIndex = Arrays.binarySearch(nums, mid, end + 1, target);
                    return targetIndex >= 0? targetIndex: -1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    // 34. 在排序数组中查找元素的第一个和最后一个位置
    // 二分查找算法：数据结构的经典问题，二分查找可以有两种写法来返回出现的第一个或者最后一个元素的位置
    // 两种写法到底是查找到第一个还是最后一个，记不清楚某个小细节的话，直接假设一个数组[a, a, a]，然后要查找值为a的第一个位置和最后一个位置，模拟执行路径一遍即可
    public int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[] {-1, -1};
        } else {
            return new int[] {binarySearchFirst(nums, target), binarySearchLast(nums, target)};
        }
    }

    // 二分查找数组中的第一个指定元素的位置
    // 核心要点是：1. 初始指针指向首尾元素的下标。2. 中点计算公式mid = (begin + end) / 2。3. 等于的情况需要合并到小于，即分小于等于和大于两种情况缩小查找区间。
    public int binarySearchFirst(int[] nums, int target) {
        int begin = 0, end = nums.length - 1;
        while (begin < end) {
            int mid = (begin + end) / 2;
            if (target <= nums[mid]) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }
        return nums[begin] == target? begin: -1;
    }

    // 二分查找数组中的最后一个指定元素的位置
    // 核心要点是：1. 初始指针指向首尾元素的下标。2. 中点计算公式mid = (begin + end) / 2 + 1。3. 等于的情况需要合并到大于，即分小于和大于等于两种情况缩小查找区间。
    public int binarySearchLast(int[] nums, int target) {
        int begin = 0, end = nums.length - 1;
        while (begin < end) {
            int mid = (begin + end) / 2 + 1;
            if (target < nums[mid]) {
                end = mid - 1;
            } else {
                begin = mid;
            }
        }
        return nums[begin] == target? begin: -1;
    }

    // 39. 组合总和
    // 递归子问题：本题的用例规模不算大，因此可以将问题转化为递归子问题来求解
    // 具体的算法也很简单，遍历数组中的每一个元素candidates[i]，有两种可能性：
    // 1. 数组中单个值组成目标值，即candidates[i] == target，这时直接作为一个解放入结果
    // 2. 数组中多个值相加组成目标值，这时递归求解target - candidates[i]的所有解，再向这些解中加入candidates[i]即可
    // 对于求解中存在的重复解，排序后使用哈希表去重即可
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        if (target <= 0) {
            return new ArrayList<>();
        } else {
            HashSet<List<Integer>> res = new HashSet<>();
            for (int i = 0; i < candidates.length; i++) {
                // 单个值构成解
                if (candidates[i] == target) {
                    ArrayList<Integer> singleSum = new ArrayList<>();
                    singleSum.add(candidates[i]);
                    res.add(singleSum);
                }
                // 多个值构成解
                List<List<Integer>> subSum = combinationSum(candidates, target - candidates[i]);
                for (List<Integer> sum: subSum) {
                    sum.add(candidates[i]);
                    sum.sort(Comparator.comparingInt(o -> o));
                }
                res.addAll(subSum);
            }
            return new ArrayList<>(res);
        }
    }

    // 46. 全排列
    // 递归子问题：全排列的数量级相当大，因此题目给的用例规模都不算大，考虑直接使用递归子问题来求解
    // 本题的算法也很简单，遍历整个数组nums，使用递归来求解数组去除nums[i]得到的新数组的全排列，然后在每个结果后加上nums[i]即可
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) {
            return res;
        } else if (nums.length == 1) {
            ArrayList<Integer> subRes = new ArrayList<>();
            subRes.add(nums[0]);
            res.add(subRes);
        } else {
            for (int i = 0; i < nums.length; i++) {
                List<List<Integer>> subRes = permute(deleteNum(nums, i));
                for (List<Integer> elements: subRes) {
                    elements.add(nums[i]);
                }
                res.addAll(subRes);
            }
        }
        return res;
    }

    public int[] deleteNum(int[] nums, int targetIndex) {
        int[] res = new int[nums.length - 1];
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i != targetIndex) {
                res[index] = nums[i];
                index += 1;
            }
        }
        return res;
    }


    // 48. 旋转图像
    // 数组原地变换：原地变化的核心是按照一定的规律配对元素，配对的元素交换值，才能做到不丢失矩阵的信息，其中最常见的就是对角线交换
    // 本题的算法也是利用对角线交换，首先以对角线为轴，将位置(i, j)和位置(j, i)，交换后再将每行的元素逆置即可
    public void rotate(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i > j) {    // 只交换对角线一侧和另外一侧即可
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = temp;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 - j] = temp;
            }
        }
    }
}
