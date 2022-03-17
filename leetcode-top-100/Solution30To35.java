package com.company;

import javafx.util.Pair;

import java.sql.Array;
import java.util.*;

public class Solution30To35 {

    // 96. 不同的二叉搜索树
    // 一维动态规划：假设dp[i]为节点数量为i的二叉搜索树数量，则节点为i的二叉搜索树，左子树只要是节点数量为a的二叉搜索树，右子树为节点数为b的二叉搜索树，只要满足a+b==i-1即可
    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= i - 1; j++) {
                dp[i] += dp[j] * dp[i - 1 - j];
            }
        }
        return dp[n];
    }

    // 98. 验证二叉搜索树
    // 二叉树的中序遍历性质：二叉搜索树的中序遍历为严格的升序，可以直接由该性质转化为非递归进行验证
    // 特别需要注意二叉树中可能出现Integer类型的最小值，因此需要使用到Double类的无穷小概念，这个是非常常用的技巧，需要特别注意
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        double lastVal = Double.NEGATIVE_INFINITY;  // 中序遍历过程中上一个节点值
        Stack<TreeNode> stack = new Stack<>();
        TreeNode now = root;
        while (now != null) {
            stack.add(now);
            now = now.left;
        }
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            if (current.val > lastVal) {
                lastVal = current.val;
            } else {
                return false;
            }
            now = current.right;
            while (now != null) {
                stack.add(now);
                now = now.left;
            }
        }
        return true;
    }

    // 101. 对称二叉树
    // 递归子问题：很典型的递归子问题，单个树root对称的条件是左子树和右子树对称
    // 而两个树root1和root2对称的条件是，root1根节点的值等于root2根节点的值，且root1的左子树和root2的右子树对称，且root1的右子树和root2的左子树对称
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        } else if (root.left == null && root.right == null) {   // 叶子节点情况
            return true;
        } else {
            return isSymmetricForTwoTrees(root.left, root.right);
        }
    }

    public boolean isSymmetricForTwoTrees(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return true;
        } else if (root1 == null || root2 == null) {
            return false;
        } else {
            return root1.val == root2.val && isSymmetricForTwoTrees(root1.left, root2.right) && isSymmetricForTwoTrees(root1.right, root2.left);
        }
    }

    // 101. 对称二叉树
    // 二叉树的层次遍历性质：本题同样可以使用非递归来求解，直接使用二叉树的层次遍历，同时进行两种层次遍历，一种是每层内从左向右，另一种是每层内从右向左，观察两种遍历中每次遍历到的值是否相等即可
    // 需要特别注意因为空节点的存在，必须将空节点一并在层次遍历中遍历，才能保证严格对称性
    public boolean isSymmetric2(TreeNode root) {
        Queue<TreeNode> queue1 = new LinkedList<>();
        Queue<TreeNode> queue2 = new LinkedList<>();
        queue1.add(root);
        queue2.add(root);

        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            TreeNode current1 = queue1.poll();
            TreeNode current2 = queue2.poll();

            if (current1 == null || current2 == null) {
                if (current1 == null && current2 == null) {
                    continue;
                } else {
                    return false;
                }
            } else {
                if (current1.val != current2.val) {
                    return false;
                }
                queue1.add(current1.left);
                queue1.add(current1.right);

                queue2.add(current2.right);
                queue2.add(current2.left);
            }
        }

        return queue1.isEmpty() && queue2.isEmpty();
    }

    // 102. 二叉树的层序遍历
    // 二叉树的层次遍历：基础算法题目，使用队列实现二叉树的层次遍历，需要非常熟悉
    // 特别地，因为这里需要将不同层的节点值分开输出，因此队列中必须跟随记录当前节点的层数
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();

        if (root == null) {
            return res;
        }

        Queue<Pair<TreeNode, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(root, 0));

        while (!queue.isEmpty()) {
            Pair<TreeNode, Integer> entry = queue.poll();
            TreeNode current = entry.getKey();

            if (entry.getValue() >= res.size()) {
                List<Integer> levelRes = new ArrayList<>();
                levelRes.add(current.val);
                res.add(levelRes);
            } else {
                res.get(entry.getValue()).add(current.val);            }

            if (current.left != null) {
                queue.add(new Pair<>(current.left, entry.getValue() + 1));
            }
            if (current.right != null) {
                queue.add(new Pair<>(current.right, entry.getValue() + 1));
            }
        }

        return res;
    }

    // 104. 二叉树的最大深度
    // 递归子问题：很简单的递归子问题，二叉树root的最大深度就是左子树和右子树中的最大深度加1
    // 本题同样可以使用非递归求解，中序遍历或者层次遍历，只需要在遍历中保存各个节点的深度信息，也可以直接进行求解
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return 1;
        } else {
            return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
        }
    }
}
