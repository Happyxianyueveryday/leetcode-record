package com.company;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Solution75To80 {

    // 647. 回文子串
    // 二维动态规划：和最长回文子串类似的题目，使用二维动态规划进行求解
    // 假设dp[i][j]为从下标i到下标j的子串是否为回文子串，则有dp[i][j]=dp[i+1][j-1]&&(dp[i]==dp[j])，以i为行j为列，也就是当前点取决于左下角的点，特别的，初始条件为当i>=j时，均为回文子串
    public int countSubstrings(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }

        boolean[][] dp = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {  // 矩阵对角线及以下均为回文字符串
            for (int j = 0; j < s.length(); j++) {
                if (i >= j) {
                    dp[i][j] = true;
                }
            }
        }

        for (int i = s.length(); i >= 0; i--) {  // 因为矩阵对角线及以下均已初始化，因此更新方向必须为从下到上，每行内从左到右，更新方向画个图即可确定
            for (int j = 0; j < s.length(); j++) {
                if (i < j) {
                    dp[i][j] = dp[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
                }
            }
        }

        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                if (i <= j) {
                    res += dp[i][j]? 1: 0;
                }
            }
        }

        return res;
    }

    // 437. 路径总和 III
    // 递归子问题：因为本题涉及的路径不包括横跨两个子树的，因此问题简单很多
    // 二叉树root中等于targetSum的路径总和，等于左子树中等于targetSum的路径总和，左子树中从根节点出发等于targetSum-root.val的路径总和，右子树中等于targetSum的路径总和，右子树中从根节点出发等于targetSum-root.val的路径总和，以及只选取根节点自身，上述所有数值的总和
    public int pathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val == targetSum? 1: 0;
        } else {
            return (root.val == targetSum? 1: 0)
                    + pathSum(root.left, targetSum)
                    + rootPathSum(root.left, targetSum - root.val)
                    + pathSum(root.right, targetSum)
                    + rootPathSum(root.right, targetSum - root.val);
        }
    }

    public int rootPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val == targetSum? 1: 0;
        } else {
            return (root.val == targetSum? 1: 0)
                    + rootPathSum(root.left, targetSum - root.val)
                    + rootPathSum(root.right, targetSum - root.val);
        }
    }

    // 287. 寻找重复数
    // 链表判断环问题：本题中将数组nums视作一个跳跃链表，即每个元素nums[i]的下一个元素为nums[nums[i]]，则因为数组中存在重复的数值，这样构成的链表必定存在环，问题转换为求解链表中环的入口
    // 同样使用快慢双指针法来求解链表环的入口，首先将快慢指针均置于链表首节点，快指针一次走两步，慢指针一次走一步，直到快慢指针相等退出循环，这时将另一个新指针置放到首节点，同时一次走一步移动慢指针和新指针，直到两个指针相等，这时指向的位置就是环的入口节点
    public int findDuplicate(int[] nums) {
        int fastPtr = 0, slowPtr = 0;
        while (true) {
            fastPtr = nums[nums[fastPtr]];
            slowPtr = nums[slowPtr];

            if (fastPtr == slowPtr) {
                break;
            }
        }
        int resPtr = 0;
        while (resPtr != slowPtr) {
            resPtr = nums[resPtr];
            slowPtr = nums[slowPtr];
        }
        return resPtr;
    }

    // 236. 二叉树的最近公共祖先
    // 递归子问题：非常经典的二叉树问题，递归子问题解法不难想出，需要非常熟悉
    // 给定的两个节点p和q可能存在四种情况，一是p和q均在左子树中，二是p和q均在右子树中，三是p和q中一个在左子树，一个在右子树中，四是p或者q的一个就是根节点，剩下一个在左子树或者右子树中
    // 因此递归子问题的算法可以设计为，对于根节点root和节点p，节点q，若根节点等于p或者等于q，则最近公共祖先必然是根节点
    // 否则，分别在左子树和右子树中查找p和q的最近共公共祖先，若左子树和右子树的查找结果都非空，则p和q分别在左子树和右子树中，此时根节点root为最近公共祖先
    // 若左子树和右子树的查找结果只有一个非空，说明两个节点都在这个非空的结果对应的子树中，这时返回左子树和右子树中非空的那个查找结果就行
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        } else if (root == p || root == q) {
            return root;
        } else {
            TreeNode leftRes = lowestCommonAncestor(root.left, p, q);
            TreeNode rightRes = lowestCommonAncestor(root.right, p, q);

            if (leftRes != null && rightRes != null) {
                return root;
            } else if (leftRes != null) {
                return leftRes;
            } else {
                return rightRes;
            }
        }
    }


    // 208. 实现 Trie (前缀树)
    // 多叉树的实现：前缀树实际上是一个多叉树，树的根节点为空，根节点的拥有一个哈希表，键为英语单词字母，值为对应的子节点
    // 特别的，需要对单词的结尾插入特殊的字符来表示，比如"app"和"apple"两个均插入到树中，第三个字符p对应的子节点需要插入一个"#"来特殊区分"app"这个单词
    class Trie {

        class TreeNode {
            Character val;
            HashMap<Character, TreeNode> nodes; // 键为下一个字母，值为该字母对应的节点

            public TreeNode() {
                val = null;
                nodes = new HashMap<>();
            }

            public TreeNode(Character word) {
                val = word;
                nodes = new HashMap<>();
            }
        }

        private TreeNode root;

        public Trie() {
            root = new TreeNode();
        }

        public void insert(String word) {
            TreeNode current = root;
            for (int i = 0; i < word.length(); i++) {
                TreeNode temp = current.nodes.get(word.charAt(i));
                if (temp == null) {
                    temp = new TreeNode(word.charAt(i));
                    current.nodes.put(word.charAt(i), temp);
                }
                current = temp;
            }
            current.nodes.put('#', new TreeNode('#'));      // 特殊字符来表示单词的结尾
        }

        public boolean search(String word) {
            TreeNode current = root;
            for (int i = 0; i < word.length(); i++) {
                TreeNode temp = current.nodes.get(word.charAt(i));
                if (temp == null) {
                    return false;
                } else {
                    current = temp;
                }
            }
            return current.nodes.containsKey('#');     // 单词搜索时，最后一个字母搜索到节点必须包含单词结尾的标记
        }

        public boolean startsWith(String prefix) {
            TreeNode current = root;
            for (int i = 0; i < prefix.length(); i++) {
                TreeNode temp = current.nodes.get(prefix.charAt(i));
                if (temp == null) {
                    return false;
                } else {
                    current = temp;
                }
            }
            return true;    // 前缀搜索时，最后一个字母的位置可以是叶子节点也可以是非叶子节点
        }
    }


}
