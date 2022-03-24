package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution55To60 {

    // 279. 完全平方数
    // 多叉树的广度优先搜索：一个任务需要用多个相同的步骤完成，且每一步都可以有多个选择的情况，优先考虑多叉树结构，来表示每一步可能的多个选择，然后利用多叉树的各种算法来求解，本题的规模中需要配合进行剪枝
    // 对于给定的值n，将其作为多叉树的根节点，每次都可以从小于n的完全平方数中取出一个值，以n减去这个完全平方数的值作为根节点的子节点，到值为0时作为叶子节点，以此类推可以得到一个多叉树，特别地，题目只要求最少的组成树，即这个树的根节点到叶子节点的最小深度，可以在不实际构造这个树的基础上，直接通过广度优先搜索完成
    public int numSquares(int n) {
        ArrayList<Integer> square = new ArrayList<>();  // 构造所有小于等于n的完全平方数
        int i = 1;
        while (i * i <= n) {
            square.add(i * i);
            i += 1;
        }

        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();   // 队列中的元素，节点值作为key，当前节点深度作为值
        queue.add(new Pair<>(n, 0));

        int res = Integer.MAX_VALUE;    // 叶子节点到根节点的最小深度值
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> current = queue.poll();
            int val = current.getKey();
            int depth = current.getValue();

            if (val == 0) {     // 当前节点值为0即为叶子节点
                res = Math.min(res, depth);
            } else if (depth >= res) {  // 当前节点深度已经大于最小深度值，可以直接剪枝
                continue;
            } else {
                for (i = square.size() - 1; i >= 0; i--) {  // 先从大的完全平方数开始减，方便进行剪枝
                    if (square.get(i) <= val) {
                        queue.add(new Pair<>(val - square.get(i), depth + 1));
                    }
                }
            }
        }
        return res;
    }

    // 279. 完全平方数
    // 一维动态规划：用dp[i]表示数字i需要的最小完全平方数的加数的数量，则可以得到动态规划转移方程dp[i] = min(dp[i - 1], dp[i - 4],..., dp[i - k*k]) + 1
    // 本题也是一维动态规划的典型题目，一维动态规划的时间复杂度远小于上面的广度优先搜索
    public int numSquares2(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;
        for (int i = 1; i < n + 1; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                min = Math.min(min, dp[i - j * j]);
            }
            dp[i] = min + 1;
        }
        return dp[n];
    }

    // 283. 移动零
    // 数组移动操作：很基础的题目，维护一个指针ptr初始化指向数组末尾，然后遇到0则先将0出现的位置与指针ptr之间的数字均向前挪一个位置，最后将指针ptr向前挪一个位置，循环进行上述操作即可
    public void moveZeroes(int[] nums) {
        int ptr = nums.length - 1;
        int i = 0;
        while (i <= ptr) {
            if (nums[i] == 0) {     // 注意移动后原来位置nums[i]的值变为移动前nums[i+1]的值，因此不能将i加1
                for (int j = i; j < ptr; j++) {
                    nums[j] = nums[j + 1];
                }
                nums[ptr] = 0;
                ptr -= 1;
            } else {
                i += 1;
            }
        }
    }

    // 297. 二叉树的序列化与反序列化
    // 二叉树的层次遍历：需要使用遍历来确定一个二叉树时，推荐使用二叉树的层次遍历，只是层次遍历中需要输出所有的空结点
    // 从二叉树的序列化结果重新构造二叉树时，也是使用队列，首先构造二叉树的根节点并放入队列，然后从队列中出队队首元素作为当前节点，从序列化结果中取出下两个节点的输出值，分别构造当前节点的左子节点和右子节点，如果左子节点不为空将左子节点入队，如果右子节点不为空将右子节点入队，循环上述操作直到队列为空为止
    public class Codec {

        public static final String TREENODE_NULL_VALUE = "null";

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            List<Integer> res = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                TreeNode current = queue.poll();

                if (current == null) {
                    res.add(null);
                } else {
                    res.add(current.val);
                    queue.add(current.left);
                    queue.add(current.right);
                }
            }

            return res.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data == null) {
                return null;
            }

            String[] split = data.substring(1, data.length() - 1)
                    .replaceAll(" ", "")
                    .split(",");

            if (split.length == 0 || TREENODE_NULL_VALUE.equals(split[0])) {
                return null;
            }

            Queue<TreeNode> queue = new LinkedList<>();

            TreeNode root = new TreeNode(Integer.parseInt(split[0]));
            int index = 1;
            queue.add(root);

            while (!queue.isEmpty()) {
                TreeNode current = queue.poll();

                current.left = !TREENODE_NULL_VALUE.equals(split[index])?
                        new TreeNode(Integer.parseInt(split[index])): null;
                if (current.left != null) {
                    queue.add(current.left);
                }
                index += 1;

                current.right = !TREENODE_NULL_VALUE.equals(split[index])?
                        new TreeNode(Integer.parseInt(split[index])): null;
                if (current.right != null) {
                    queue.add(current.right);
                }
                index += 1;
            }

            return root;
        }
    }

    // 142. 环形链表 II
    // 链表双指针法：非常基础和常见的链表基础题，同样使用和之前判断链表是否存在环的算法
    // (1) 创建快慢双指针都指向首节点
    // (2) 然后同时移动指针直到快慢指针再次相等，快指针一次两步，慢指针一次一步，如果再次相等时快指针不为空则存在环
    // (3) 这时再创建一个新指针指向首节点，同时移动快指针和这个新指针，一次均走一步，移动到快指针和新指针相等，这时的值就是链表入口节点
    public ListNode detectCycle(ListNode head) {
        ListNode fastPtr = head, slowPtr = head;

        while (true) {
            fastPtr = fastPtr != null && fastPtr.next != null? fastPtr.next.next: null;
            slowPtr = slowPtr != null? slowPtr.next: null;

            if (fastPtr == slowPtr) {
                break;
            }
        }

        if (fastPtr != null) {      // 链表中存在环
            ListNode res = head;
            while (fastPtr != res) {
                fastPtr = fastPtr.next;
                res = res.next;
            }
            return fastPtr;
        } else {        // 链表中不存在环
            return null;
        }
    }

    // 617. 合并二叉树
    // 递归子问题：对于给定的二叉树root1和root2，若root1和root2均为空，返回空，若root1和root2中存在非空，则构造新节点，节点值为root1和root2的值之和，然后递归地由root1.left和root2.left构造新节点的左子树，由root2.right和root2.right构造新节点的右子树
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return null;
        } else {
            int value1 = root1 != null? root1.val: 0;
            int value2 = root2 != null? root2.val: 0;
            TreeNode res = new TreeNode(value1 + value2);
            res.left = mergeTrees(root1 != null? root1.left: null, root2 != null? root2.left: null);
            res.right = mergeTrees(root1 != null? root1.right: null, root2 != null? root2.right: null);
            return res;
        }
    }

}
