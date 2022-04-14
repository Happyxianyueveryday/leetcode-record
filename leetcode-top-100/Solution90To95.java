package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution90To95 {
    // 14. 最长公共前缀
    // 字符串操作：算法比较基础的题目，设定一个指针指向当前对比的下标，对每个单词在该下标的字符进行匹配即可，匹配失败即可结束循环，得到需要的最长公共前缀
    public String longestCommonPrefix(String[] strs) {
        StringBuilder res = new StringBuilder();

        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < strs.length; i++) {
            minLength = Math.min(minLength, strs[i].length());
        }

        for (int i = 0; i < minLength; i++) {
            char character = strs[0].charAt(i);
            boolean flag = true;
            for (int j = 1; j < strs.length; j++) {
                if (character != strs[j].charAt(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                res.append(character);
            } else {
                break;
            }
        }

        return res.toString();
    }

    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    // 138. 复制带随机指针的链表
    // 链表基本操作：本题属于非常基础的题目，需要非常熟悉
    // 本题的算法是，首先遍历一遍链表，在原链表的基础上复制节点，复制的节点放在原节点之后，这样下标为奇数位置的节点是复制节点
    // 然后再遍历一遍链表复制random指针，对于奇数下标的复制节点，该节点的random指针的值是前驱节点的random指向节点的下一个节点，
    // 最后再遍历一遍链表，分离奇数下标节点和偶数下标节点各为一个新链表，奇数下标节点即为复制得到的链表
    // 本题的算法非常常用，需要非常熟悉
    public Node copyRandomList(Node head) {
        if (head == null) {
            return head;
        }

        Node current = head;
        while (current != null) {   // 以1->2->3为例，复制完得到1->1->2->2->3->3
            Node copyNode = new Node(current.val);
            copyNode.next = current.next;
            current.next = copyNode;

            current = copyNode.next;
        }

        current = head;
        Node prev = null;
        int index = 0;
        while (current != null) {   // 新拷贝出来的节点current，random指针的值等于current的前驱节点prev的random指针指向的节点的下一个节点，也即prev.random.next
            if (index % 2 == 1) {
                current.random = prev.random != null? prev.random.next: null;
            }
            prev = current;
            current = current.next;
            index += 1;
        }

        Node res = null;
        current = head;
        index = 0;
        while (current != null) {   // 分离奇数和偶数节点，将每一个节点指向下一个节点的下一个节点即可，注意下标为1的节点是复制出来的新链表首节点
            if (index == 1) {
                res = current;
            }
            Node temp = current.next;
            if (current.next != null) {
                current.next = current.next.next;
            }
            current = temp;
            index += 1;
        }

        return res;
    }

    // 232. 用栈实现队列
    // 栈的基本应用：使用双栈来实现队列，双栈分别为栈1和栈2
    // 对于入栈操作，直接加入栈1即可，对于出栈操作，首先判断栈2是否为空，栈2非空则直接出栈栈2栈顶元素作为结果，栈2为空则出栈栈1中的所有元素并且依次加入栈2，最后出栈2的栈顶元素结果
    class MyQueue {

        Stack<Integer> stack1;
        Stack<Integer> stack2;

        public MyQueue() {
            stack1 = new Stack<>();
            stack2 = new Stack<>();
        }

        public void push(int x) {
            stack1.push(x);
        }

        public int pop() {
            if (!stack2.isEmpty()) {
                return stack2.pop();
            } else {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
                return stack2.pop();
            }
        }

        public int peek() {
            if (!stack2.isEmpty()) {
                return stack2.peek();
            } else {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
                return stack2.peek();
            }
        }

        public boolean empty() {
            return stack1.isEmpty() && stack2.isEmpty();
        }
    }

    // 239. 滑动窗口最大值
    // 滑动窗口算法：使用优先队列来处理滑动窗口内的元素，时间复杂度在O(nlogn)，维护两个指针begin和end来表示滑动窗口的开始和结束，则初始化begin=0，end=k，将begin到end之间（不包括end）的元素放入优先队列中
    // 然后循环进行以下操作直到end达到最后一个元素时停止，首先移除begin下标的元素，然后将begin增加1，然后将end位置下标的元素加入优先队列中，再将end增加1，这时窗口移动完成，然后获取优先队列中的最大元素并且加加入结果中
    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] res = new int[nums.length - k + 1];
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        int begin = 0, end = k;
        for (int i = begin; i < end; i++) {
            queue.offer(nums[i]);
        }
        res[0] = queue.peek();

        int index = 1;
        while (end < nums.length) {     // 为了不遗漏下标为nums.length-1的元素，必须先移动滑动窗口再获取最大值
            queue.remove(nums[begin]);
            begin += 1;

            queue.offer(nums[end]);
            end += 1;

            int maxValue = queue.peek();
            res[index] = maxValue;
            index += 1;
        }

        return res;
    }

    // 84. 柱状图中最大的矩形
    // 单调栈：首先先给出计算最大矩形的关键思路，我们考虑从数组中的每一个高度值出发，只要在左侧和分别找出第一个小于当前高度的位置下标left和right，即可得到该高度值能达到的最大矩形面积，因此对于数组中的每一个高度，需要求解到左侧和右侧第一个小于当前高度值的元素位置
    // 然后再给出单调栈的定义，要构造一个从栈底到栈顶单调递增的栈，只需要入栈时出栈直到栈顶元素小于当前元素即可，本题中因为要得到每个高度位置左侧和右侧第一个小于当前高度值的位置，因此需要额外记录每个元素值的下标，然后使用单调栈从左到右，从右到左分别进行处理，每次入栈时出栈直到栈顶元素小于当前高度值时，记录下栈顶元素的下标即可，特别的，如果此时栈为空，则记录位置-1
    public int largestRectangleArea(int[] heights) {
        Stack<Pair<Integer, Integer>> stack = new Stack<>();    // 单调栈，其中的元素键为高度值，值为该高度的下标

        int[] left = new int[heights.length];   // 每个高度值左侧第一个小于当前高度值的位置下标
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && stack.peek().getKey() >= heights[i]) {
                stack.pop();
            }
            left[i] = !stack.isEmpty()? stack.peek().getValue(): -1;
            stack.push(new Pair<>(heights[i], i));
        }

        int[] right = new int[heights.length];
        stack.clear();
        for (int i = heights.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && stack.peek().getKey() >= heights[i]) {
                stack.pop();
            }
            right[i] = !stack.isEmpty()? stack.peek().getValue(): -1;
            stack.push(new Pair<>(heights[i], i));
        }

        int res = Integer.MIN_VALUE;
        for (int i = 0; i < heights.length; i++) {
            int leftIndex = left[i] != -1? left[i] + 1: 0;      // 左侧没有小于当前高度的值，则矩形可以到达左侧到第一个元素
            int rightIndex = right[i] != -1? right[i] - 1: heights.length - 1;  // 右侧没有小于当前高度的值，则矩形可以到达右侧的最后一个元素
            res = Math.max(res, (rightIndex - leftIndex + 1) * heights[i]);
        }

        return res;
    }
    
    
}
