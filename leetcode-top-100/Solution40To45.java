package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Solution40To45 {

    // 139. 单词拆分
    // 一维动态规划：设dp[i]表示下标从0到i-1的子串是否能够被字典组成，则有如下的更新规则
    // 如果dp[i-k]=true，且s[i-k:i]在字典中存在对应相等的单词，则有dp[i]=true
    // 上述动态规划可能显得不是很清楚，这里举个例子来说明
    // 假设s="leetcode"，wordDict=["leet","code"]
    // 则因为下标0到3的子串"leet“可以被字典组成，因此dp[4]=true，下面求解dp[8]时，因为dp[4]=true，且s[4:8]="code"存在于字典中，因此dp[8]=true
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;   // 空字符串认为是存在的
        for (int i = 1; i < s.length() + 1; i++) {
            for (int k = 0; k <= i; k++) {
                if (dp[i - k] && wordDict.contains(s.substring(i - k, i))) {
                    dp[i] = true;
                }
            }
        }
        return dp[s.length()];
    }

    // 141. 环形链表
    // 链表双指针法：设定快慢指针都指向链表首节点，快指针每次移动两步，慢指针每次移动一步。直到移动到快慢指针第二次相等为止，如果快慢指针的值都为空则没有环，否则链表中存在环
    // 特别地，因为开始时快慢指针都指向链表首节点，因此需要使用一个变量来区分第一次快慢指针相等和第二次快慢指针相等
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode fastPtr = head, slowPtr = head;
        boolean isLoop = false;
        while (fastPtr != slowPtr || !isLoop) {
            fastPtr = fastPtr != null && fastPtr.next != null && fastPtr.next.next != null? fastPtr.next.next: null;
            slowPtr = slowPtr != null? slowPtr.next: null;
            isLoop = true;
        }
        return fastPtr != null;
    }

    // 148. 排序链表
    // 归并排序算法：链表的数据结构排序比较适合使用归并排序来实现
    // 归并排序的递归思路和实现需要非常熟悉，特别地，计算链表中间节点时需要计算到中间节点的前置节点，方便将数组分为前后两个半段
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {    // 递归终止条件
            return head;
        }

        ListNode mid = middleNode(head);
        ListNode last = mid.next;       // 链表后半部分
        mid.next = null;                // 中断链表前半和后半部分，重要的小步骤

        // 递归排序步骤
        head = sortList(head);  // 排序链表前半部分
        last = sortList(last);  // 排序链表后半部分

        // 归并流程步骤
        ListNode res = new ListNode();
        ListNode resPos = res, pos1 = head, pos2 = last;

        while (pos1 != null && pos2 != null) {
            if (pos1.val <= pos2.val) {
                resPos.next = pos1;
                pos1 = pos1.next;
            } else {
                resPos.next = pos2;
                pos2 = pos2.next;
            }
            resPos = resPos.next;
        }
        while (pos1 != null) {
            resPos.next = pos1;
            pos1 = pos1.next;
            resPos = resPos.next;
        }
        while (pos2 != null) {
            resPos.next = pos2;
            pos2 = pos2.next;
            resPos = resPos.next;
        }
        return res.next;
    }

    // 查找链表的中间节点
    // 链表双指针法：使用快慢双指针指向头部，快指针一次走两步，慢指针一次走一步，快指针指向链表最后一个节点时，慢指针指向中间节点
    // 特别地，为了归并排序时能够分割链表为前半和后半两段，因此需要返回中间节点的前置节点
    public ListNode middleNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode fastPtr = head, slowPtr = head, res = null;
        while (fastPtr != null && fastPtr.next != null) {
            res = slowPtr;
            fastPtr = fastPtr.next.next != null ? fastPtr.next.next: null;
            slowPtr = slowPtr.next;
        }
        return res;
    }

    // 152. 乘积最大子数组
    // 一维动态规划：满足某种条件的子串通常都可以使用类似的一维动态规划求解，动态规划数组中，设dp[i]为以下标i结尾的满足最大条件的子串即可
    // 只是本题涉及到乘法符号问题，最大值可以由之前的正数最大值乘以正数得到，也可以由之前的负数最小值乘以负数得到，所以需要维护两个动态规划数组
    // 因此假设dp[i]为以下标i结尾的乘积最大子串，dpm[i]为以下标i结尾的乘积最小子串
    // 根据上述假设，则有dp[i]=max(nums[i], dp[i-1]*nums[i], dpm[i-1]*nums[i])，dpm[i] = min(nums[i], dp[i-1]*nums[i], dpm[i-1]*nums[i])
    // 展开一维动态规划即可，最后遍历整个dp数组。其中的最大值就是需要的乘积最大子串
    public int maxProduct(int[] nums) {
        int[] dp = new int[nums.length];
        int[] dpm = new int[nums.length];
        dp[0] = nums[0];
        dpm[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], Math.max(dp[i - 1] * nums[i], dpm[i - 1] * nums[i]));
            dpm[i] = Math.min(nums[i], Math.min(dp[i - 1] * nums[i], dpm[i - 1] * nums[i]));
        }
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < dp.length; i++) {
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    // 160. 相交链表
    // 链表双指针法：比较经典的题目，设指针pos1指向headA，指针pos2指向headB，同时移动两个指针，pos1的下一个节点为空时移动到headB，pos2的下一个节点为空时移动到headA,循环移动直到两个指针相等即可
    // 特别地，为了处理没有相交地情况，需要维护一个交换次数计数，交换大于两次时也退出循环，退出循环时pos1==pos2则存在交点，否则不存在
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pos1 = headA, pos2 = headB;
        int swapCount = 0;     // 双指针是否都已经到达过一次空节点
        while (pos1 != pos2 && swapCount <= 2) {
            if (pos1.next != null) {
                pos1 = pos1.next;
            } else {
                pos1 = headB;
                swapCount += 1;
            }

            if (pos2.next != null) {
                pos2 = pos2.next;
            } else {
                pos2 = headA;
                swapCount += 1;
            }
        }
        return pos1 == pos2? pos1: null;
    }

}
