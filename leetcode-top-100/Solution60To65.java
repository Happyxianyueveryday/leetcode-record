package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solution60To65 {

    // 322. 零钱兑换
    // 一维动态规划：典型的一维动态规划问题，本题和完全平方数那题的方法类似，要以最少零钱数量兑换出数值amount，只要在以最少零钱数量兑换出数值amount - coins[i]，再加上任意一个零钱coins[i]即可
    // 因此，设dp[i]为兑换出数值i的最少零钱数量，canChange[i]为可以兑换出数值i，则dp[i] = min(dp[i - coins[0]] + 1, dp[i - coins[1]] + 1, ...)，其中要求canChange[i - coins[j]] == true
    public int coinChange(int[] coins, int amount) {
        boolean[] canChange = new boolean[amount + 1];
        int[] dp = new int[amount + 1];
        canChange[0] = true;  // 特别地，为了解决单个零钱组成值的问题，认为0可以由最少0张零钱组成，这种解法为了解决单张零钱组成数值i的情况，需要出现dp[0]
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            double minValue = Double.MAX_VALUE;
            for (int j = 0; j < coins.length; j++) {
                if (i - coins[j] >= 0 && canChange[i - coins[j]]) {
                    minValue = Math.min(minValue, dp[i - coins[j]] + 1);
                }
            }
            if (minValue == Double.MAX_VALUE) {
                canChange[i] = false;
            } else {
                canChange[i] = true;
                dp[i] = (int) minValue;
            }
        }
        return canChange[amount]? dp[amount]: -1;
    }

    // 394. 字符串解码
    // 栈的基本应用：使用栈对字符串进行处理即可，依次遍历输入字符串的每一个字符，遇到普通字母字符，左括号，或者数字都入栈
    // 直到遇到右括号时，出栈直到栈顶为左括号，这时出栈的部分组成字符串即得到需要重复的字符串，然后再出栈直到栈顶为普通字符为止，这时得到重复的次数，将需要重复的字符串重复该次数后，将结果重新放入栈中
    public String decodeString(String s) {
        Stack<String> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ']') {
                builder.delete(0, builder.length());
                while (!"[".equals(stack.peek())) {
                    String str = stack.pop();
                    builder.append(str);
                }
                stack.pop();        // 出栈左括号

                String repeatStr = builder.toString();  // 这里无需反转待重复的字符串，因为最后生成结果时会进行一次反转

                builder.delete(0, builder.length());
                while (!stack.isEmpty() && Character.isDigit(stack.peek().charAt(0))) {
                    builder.append(stack.pop());
                }

                int repeatCount = Integer.parseInt(builder.reverse().toString());

                builder.delete(0, builder.length());
                for (int j = 0; j < repeatCount; j++) {
                    builder.append(repeatStr);
                }

                stack.push(builder.toString());
            } else {
                stack.push(s.substring(i, i + 1));
            }
        }

        builder.delete(0, builder.length());
        while (!stack.isEmpty()) {
            builder.append(stack.pop());
        }

        return builder.reverse().toString();
    }


    // 338. 比特位计数
    // 一维动态规划：将1到n中的每个整数分别除以2取余数，很容易实现O(nlogn)的算法，下面设计一种O(n)的算法
    // 假设dp[i]表示数值i的比特位计数，观察列举1到16的数字以及对应的结果可以发现，假设i=2^n+k，则有dp[i]=dp[k]+1，因此提前制作好小于等于n的所有的2的平方数，然后从大到小依次取余数即可得到k
    public int[] countBits(int n) {
        int[] res = new int[n + 1];
        List<Integer> square = new ArrayList<>();

        int power = 1;
        while (power <= n) {
            square.add(power);
            power *= 2;
        }

        for (int i = 0; i <= n; i++) {
            if (i == 0) {
                res[i] = 0;
            } else if (i == 1) {
                res[i] = 1;
            } else {
                int k = 0;
                for (int j = square.size() - 1; j >= 0; j--) {
                    if (square.get(j) <= i) {
                        k = i % square.get(j);
                        break;
                    }
                }
                res[i] = res[k] + 1;
            }
        }

        return res;
    }

    // 543. 二叉树的直径
    // 递归子问题：本题和二叉树中的最大路径和是使用相同的算法，需要特别注意这里要求的是直径，不是最长路径中含有的节点数，这里需要先做判空左子树和右子树，然后来计算直径
    // 二叉树root的直径可能有三种情况，一是直径只在左子树中出现，二是直径只在右子树中出现，三是直径经过二叉树的根节点，且过左子树和右子树中的至少一个子树
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return 0;
        } else {
            int leftRes = 0, rightRes = 0, bothRes = 0;
            if (root.left != null) {
                leftRes = diameterOfBinaryTree(root.left);
                bothRes = bothRes + 1 + maxDepthOfBinaryTree(root.left);
            }
            if (root.right != null) {
                rightRes = diameterOfBinaryTree(root.right);
                bothRes = bothRes + 1 + maxDepthOfBinaryTree(root.right);
            }

            return Math.max(bothRes, Math.max(leftRes, rightRes));
        }
    }

    // 从二叉树的根节点出发到叶子节点的最长路径的直径
    public int maxDepthOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return 0;
        } else {
            return 1 + Math.max(maxDepthOfBinaryTree(root.left), maxDepthOfBinaryTree(root.right));
        }
    }

    // 538. 把二叉搜索树转换为累加树
    // 二叉搜索树的中序遍历：二叉搜索树中序遍历的重要性质即得到的遍历序列从小到大递增，这里实际上需要求大于每个节点的节点值之和
    // 因此只需要微调中序遍历，先遍历右子节点，再遍历根节点，再遍历左子节点即可，非递归实现是类似的
    public TreeNode convertBST(TreeNode root) {
        if (root == null) {
            return null;
        }

        Stack<TreeNode> stack = new Stack<>();
        int sum = 0;
        TreeNode current = root;

        while (current != null) {
            stack.push(current);
            current = current.right;
        }

        while (!stack.isEmpty()) {
            TreeNode now = stack.pop();

            sum += now.val;
            now.val = sum;

            current = now.left;
            while (current != null) {
                stack.push(current);
                current = current.right;
            }
        }

        return root;
    }

}
