package com.company;

import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution35To40 {

    // 105. 从前序与中序遍历序列构造二叉树
    // 递归子问题：首先取出前序遍历数组的首元素preorder[0]，这就是构造的二叉树的根节点的值
    // 然后在中序遍历数组中查找到元素preorder[0]的位置下标为index，则根节点的左子树的前序遍历为preorder去除首元素，中序遍历为inorder[:index]，右子树为前序遍历为preorder构造完左子树再去掉首元素，中序遍历为inorder[index:]
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return innerBuildTree(preorder, inorder, new AtomicInteger(0), preorder.length, 0, inorder.length);
    }

    // 注意preBeginIndex的传递方式，必须通过递归以引用形式传递，这样当先构造完左子树时，右子树的前序遍历序列的起始下标才是正确的
    // 需要使用引用传递int类型时，最好用的类是AtomicInteger
    public TreeNode innerBuildTree(int[] preorder, int[] inorder, AtomicInteger preBeginIndex, int preEndIndex, int inBeginIndex, int inEndIndex) {
        if (preBeginIndex.get() >= preEndIndex || inBeginIndex >= inEndIndex) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preBeginIndex.get()]);
        int targetIndex = -1;   // 注意在中序遍历数组的有效范围内搜索
        for (int i = inBeginIndex; i < inEndIndex; i++) {
            if (inorder[i] == preorder[preBeginIndex.get()]) {
                targetIndex = i;
            }
        }
        preBeginIndex.set(preBeginIndex.get() + 1);
        root.left = innerBuildTree(preorder, inorder, preBeginIndex, preEndIndex, inBeginIndex, targetIndex);
        root.right = innerBuildTree(preorder, inorder, preBeginIndex, preEndIndex, targetIndex + 1, inEndIndex);

        return root;
    }

    // 114. 二叉树展开为链表
    // 二叉树的前序遍历：O(1)空间复杂度的前序遍历需要使用到morris遍历，可以做下了解，这里还是给出使用栈的遍历，遍历中不断将上一次遍历的节点和当前出栈的节点链接即可
    // 二叉树的前序遍历算法也很简单，使用栈，先将根节点放入栈中，循环出栈栈顶元素直到栈为空，输出栈顶元素，然后先将栈顶元素的右子节点入栈，再将栈顶元素的左子节点入栈
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.add(root);

        TreeNode lastNode = null;

        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            if (lastNode != null) {
                lastNode.left = null;
                lastNode.right = current;
            }
            lastNode = current;

            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }
    }

    // 121. 买卖股票的最佳时机
    // 比较基础的题目，只需要一个变量记录在遍历到第i天，在第i天之前的股票价值最小值即可，本题很常见需要注意下
    public int maxProfit(int[] prices) {
        int res = 0;
        int minPrice = prices[0];   // 在当天之前的股票价值最小值
        for (int i = 1; i < prices.length; i++) {
            res = Math.max(res, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        return res;
    }

    // 124. 二叉树中的最大路径和
    // 递归子问题：二叉树中的最大路径和对应的路径分为三种情况，一是只在左子树中，二是只在右子树中，三是通过根节点同时在左子树和右子树中
    // 因此，二叉树root中的最大路径和，是左子树root.left的最大路径和，右子树root.left的最大路径和，左子树最大根路径和加上右子树最大根路径和根节点，三者中的最大值
    public int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val;
        } else {
            // 特别注意第三种情况，横跨左右子树和根节点的情况，若左子树或者右子树的最大根路径和小于0，可以选择不连接
            int case1 = root.left != null? maxPathSum(root.left): Integer.MIN_VALUE;
            int case2 = root.right != null? maxPathSum(root.right): Integer.MIN_VALUE;
            int case3 = root.val + Math.max(0, maxRootSum(root.left)) + Math.max(0, maxRootSum(root.right));
            return Math.max(case1,
                    Math.max(case2, case3));
        }
    }

    // 树的最大根路径和：二叉树root的最大根路径和和定义为起点为根节点的最大路径
    public int maxRootSum(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val;
        } else {
            return root.val + Math.max(0, Math.max(maxRootSum(root.left), maxRootSum(root.right)));
        }
    }

    // 136. 只出现一次的数字
    // 位运算的性质：很经典的题目，因为两个相同的数字做位异或运算的结果是0，而0和任何一个数字做位异或运算的结果都是该数字本身，因此遍历所有数字做位异或运算即得需要的结果
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }
}
