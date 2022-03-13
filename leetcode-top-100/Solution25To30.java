package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution25To30 {

    // 70. 爬楼梯
    // 一维动态规划：很基础的动态规划问题，第i个台阶可以由第i-1个台阶跨一步到达，或者由第i-2个台阶跨两步到达
    public int climbStairs(int n) {
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                dp[i] = 1;
            } else if (i == 1) {
                dp[i] = 2;
            } else {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
        }
        return dp[n - 1];
    }

    // 75. 颜色分类
    // 基本排序算法：题目的输出使用任意排序均可解决问题，这里简单使用一个冒泡排序
    // 冒泡排序属于数据结构的基本算法：依次通过交换数组元素的值计算下标为i=n-1, n-2, ..., 1的位置的排序后的值，对于每一轮计算中，对于下标小于i的元素相邻进行比较，若下标小的元素值大于下标大的则交换值，两层循环结束后即完成排序
    public void sortColors(int[] nums) {
        for (int i = nums.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

    // 75. 颜色分类
    // 双指针算法：因为使用排序解决问题最少的时间复杂度需要O(nlogn)，这里也提供一种双指针算法
    // 因为实际上只有0，1，2三种颜色，因此第一次遍历使用双指针只需要不断将0通过值交换放到数组最前面，第二次遍历再使用双指针将2通过值交换放到数组最后面即可，
    public void sortColors2(int[] nums) {
        int begin = 0;
        int i = nums.length - 1;
        while (i >= begin) {
            if (nums[i] == 0) {
                int temp = nums[i];
                nums[i] = nums[begin];
                nums[begin] = temp;
                begin += 1;
            } else {
                i -= 1;
            }
        }   // 结束循环时begin指向顺序遍历数组中第一个不是0的位置
        int end = nums.length - 1;
        i = begin;
        while(i <= end) {
            if (nums[i] == 2) {
                int temp = nums[i];
                nums[i] = nums[end];
                nums[end] = temp;
                end -= 1;
            } else {
                i += 1;
            }
        }
    }


    // 78. 子集
    // 递归子问题：很经典的递归子问题，考虑到题目给定的样例规模不算大，可以直接使用递归来求解
    // 递归算法也很简单，以[1,2,3]为例，其所有子集就是依次去掉一个元素即[2,3]和[1,3]以及[1,2]的子集，加上自身也即全集[1,2,3]组成
    public List<List<Integer>> subsets(int[] nums) {
        if (nums.length == 1) {
            List<List<Integer>> res = new ArrayList<>();
            res.add(new ArrayList<>());
            res.add(Arrays.asList(nums[0]));
            return res;
        } else {
            HashSet<List<Integer>> res = new HashSet<>();
            for (int i = 0; i < nums.length; i++) {
                int[] subNums = new int[nums.length - 1];
                int begin = 0;
                for (int j = 0; j < nums.length; j++) {
                    if (j != i) {
                        subNums[begin] = nums[j];
                        begin += 1;
                    }
                }
                List<List<Integer>> subRes = subsets(subNums);
                res.addAll(subRes);
            }
            List<Integer> allRes = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                allRes.add(nums[i]);
            }
            res.add(allRes);
            return new ArrayList<>(res);
        }
    }

    // 94. 二叉树的中序遍历
    // 二叉树遍历：二叉树经典遍历算法，使用栈进行非递归的中序遍历
    // 使用栈进行非递归的中序遍历算法如下：
    // 1. 创建栈，从根节点开始一路向左遍历，将所有左子节点放入栈中，直到当前节点的左子节点为空
    // 2. 出栈栈顶节点并输出该节点的值，并且从栈顶节点的右子节点开始一路向左遍历，将所有左子节点放入栈中，直到当前节点的左子节点为空
    // 3. 重复上述步骤直到栈为空，中序遍历完成
    public List<Integer> inorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode now = root;
        while (now != null) {
            stack.push(now);
            now = now.left;
        }
        List<Integer> res = new ArrayList<>();
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            res.add(current.val);
            now = current.right;
            while (now != null) {
                stack.push(now);
                now = now.left;
            }
        }
        return res;
    }

    // 79. 单词搜索
    // 广度优先搜索：从单词的第一个字符作为在图中的起点，分别展开广度优先搜索即可解决问题
    // 需要特别注意广度优先搜索必须要使用标志位避免路径重复走的问题，需要一个相同大小的boolean数组来记录当前走过的路径，注意二维数组使用clone得到的是浅拷贝而不是深拷贝
    public boolean exist(char[][] board, String word) {
        Queue<Pair<Pair<Integer, Integer>, Pair<String, boolean[][]>>> queue = new LinkedList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    boolean[][] map = new boolean[board.length][board[0].length];
                    map[i][j] = true;
                    queue.add(new Pair<>(new Pair<>(i, j), new Pair<>(String.valueOf(board[i][j]), map)));
                    while (!queue.isEmpty()) {
                        Pair<Pair<Integer, Integer>, Pair<String, boolean[][]>> current = queue.poll();
                        int x = current.getKey().getKey();
                        int y = current.getKey().getValue();
                        String str = current.getValue().getKey();
                        boolean[][] curMap = current.getValue().getValue();

                        if (word.equals(str)) {
                            return true;
                        }

                        if (!word.startsWith(str)) {    // 前缀不匹配即可直接剪枝
                            continue;
                        }

                        if (x + 1 < board.length && !curMap[x + 1][y]) {
                            boolean[][] newMap = deepCopyMatrix(curMap);
                            newMap[x + 1][y] = true;
                            queue.offer(new Pair<>(new Pair<>(x + 1, y), new Pair<>(str + board[x + 1][y], newMap)));
                        }
                        if (x - 1 >= 0 && !curMap[x - 1][y]) {
                            boolean[][] newMap = deepCopyMatrix(curMap);
                            newMap[x - 1][y] = true;
                            queue.offer(new Pair<>(new Pair<>(x - 1, y), new Pair<>(str + board[x - 1][y], newMap)));
                        }
                        if (y + 1 < board[0].length && !curMap[x][y + 1]) {
                            boolean[][] newMap = deepCopyMatrix(curMap);
                            newMap[x][y + 1] = true;
                            queue.offer(new Pair<>(new Pair<>(x, y + 1), new Pair<>(str + board[x][y + 1], newMap)));
                        }
                        if (y - 1 >= 0 && !curMap[x][y - 1]) {
                            boolean[][] newMap = deepCopyMatrix(curMap);
                            newMap[x][y - 1] = true;
                            queue.offer(new Pair<>(new Pair<>(x, y - 1), new Pair<>(str + board[x][y - 1], newMap)));
                        }
                    }
                }
            }
        }
        return false;
    }

    // 二维数组深拷贝
    private boolean[][] deepCopyMatrix(boolean[][] matrix) {
        boolean[][] copyRes = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, copyRes[i], 0, matrix[0].length);
        }
        return copyRes;
    }
}
