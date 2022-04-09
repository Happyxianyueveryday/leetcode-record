package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Solution85To90 {

    // 72. 编辑距离
    // 二维动态规划：假设dp[i][j]为word1的前i个字符和word2的前j个字符之间的编辑距离，这时对于dp[i][j]，考虑左，上，以及左上的相邻单元的值
    // (1) dp[i-1][j]表示word的前i-1个字符和word2的前j个字符的编辑距离，则在此基础上只需要在word2后加上一个和word1第j个字符相等的字符，即可得到dp[i][j]，因此dp[i][j]=dp[i-1][j]+1
    // (2) dp[i][j-1]表示word的前i个字符和word2的前j-1个字符的编辑距离，则在此基础上只需要在word1后加上一个和word2第j个字符相等的字符，即可得到dp[i][j]，因此dp[i][j]=dp[i][j-1]+1
    // (3) dp[i-1][j-1]表示word的前i-1个字符和word2的前j-1个字符的编辑距离，则在此基础上只需要判断word1的第i个字符和word2的第j个字符是否相等即可，若相等，则dp[i][j]=dp[i-1][j-1]，若时不相等，则dp[i][j]=dp[i-1][j-1]+1
    // 本题要求的是最小编辑距离，因此dp[i]就等于上述值中的最小值，本题的关键点在于如何定义动态规划数组，以及判断清楚dp[i][j]和左侧，上侧和左上侧结果之间的状态转移关系
    public int minDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {     // word2为空字符串时，到word1的编辑距离等于word1的长度
            dp[i][0] = i;
        }

        for (int j = 0; j <= word2.length(); j++) {     // word1为空字符串时，到word2的编辑距离等于word2的长度
            dp[0][j] = j;
        }

        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                int temp = Integer.MAX_VALUE;
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {   // dp[i][j]对应的是word1的前i个字符和word2的前j个字符，下标分别为i-1和j-1
                    temp = dp[i - 1][j - 1];
                } else {
                    temp = dp[i - 1][j - 1] + 1;
                }
                dp[i][j] = Math.min(temp, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
            }
        }

        return dp[word1.length()][word2.length()];
    }

    // 76. 最小覆盖子串
    // 滑动窗口算法：创建begin和end分别指向子串的开头和结尾，指针之间的部分即为滑动的窗口，并且使用一个哈希表来记录窗口内出现的字母以及次数，end<s.length时循环进行以下步骤
    // 先将end当前的位置对应字符加入窗口；再判断窗口的子串是否覆盖了目标字符串target，若覆盖了目标字符串，将begin向前移动以缩小窗口，直到缩小到不再覆盖目标字符串时停止，途中记录最小覆盖长度以及对应的字符串，最后再将end位置向前移动，继续扩大窗口
    public String minWindow(String s, String t) {
        HashMap<Character, Integer> target = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            target.put(t.charAt(i), target.getOrDefault(t.charAt(i), 0) + 1);
        }

        String res = "";
        int minLen = Integer.MAX_VALUE;

        HashMap<Character, Integer> hashMap = new HashMap<>();
        int begin = 0, end = 0;
        while (end < s.length()) {
            hashMap.put(s.charAt(end), hashMap.getOrDefault(s.charAt(end), 0) + 1);

            if (isCovered(hashMap, target)) {   // 将end向前移动扩大窗口后，需要进行判断，若当前窗口内子串已经覆盖目标字符串，将begin向前移动以缩小窗口
                while (isCovered(hashMap, target)) {
                    if (end - begin + 1 <= minLen) {
                        minLen = end - begin + 1;
                        res = s.substring(begin, end + 1);
                    }

                    hashMap.put(s.charAt(begin), hashMap.getOrDefault(s.charAt(begin), 1) - 1);
                    begin += 1;
                }
            }

            end += 1;
        }

        return res;
    }

    public boolean isCovered(HashMap<Character, Integer> curMap,
                             HashMap<Character, Integer> target) {
        boolean isCovered = true;
        for (Character c: target.keySet()) {    // 判断当前窗口内子串是否覆盖目标字符串
            if (curMap.getOrDefault(c, 0) < target.get(c)) {
                isCovered = false;
                break;
            }
        }
        return isCovered;
    }

    // 621. 任务调度器
    // 模拟调度算法：本题的核心思路是，每次挑选下一个要执行的任务时，都优先选择在当前时刻不冲突的任务中，剩余数量最多的任务
    // 在实现上可以使用一个哈希表记录每个任务对应的数量，以及任务对应的冷却时间，在需要进行选择一个任务时，首先遍历哈希表，遍历的同时将哈希表中任务的冷却时间减少1，找到当前冷却时间为0的，次数最少的任务，选择该任务，选择该任务后，再重置该任务的冷却时间为n
    public int leastInterval(char[] tasks, int n) {
        HashMap<Character, Pair<Integer, Integer>> hashMap = new HashMap<>();
        for (int i = 0; i < tasks.length; i++) {
            hashMap.put(tasks[i], new Pair<>(hashMap.containsKey(tasks[i])? hashMap.get(tasks[i]).getKey() + 1: 1,
                    0));
        }

        int res = 0;
        while (!hashMap.isEmpty()) {
            int maxCount = Integer.MIN_VALUE;
            Character maxValue = null;
            for (Character key: hashMap.keySet()) {
                if (hashMap.get(key).getValue() == 0 && hashMap.get(key).getKey() >= maxCount) {
                    maxValue = key;
                    maxCount = hashMap.get(key).getKey();
                }
                hashMap.put(key, new Pair<>(hashMap.get(key).getKey(),
                        Math.max(0, hashMap.get(key).getValue() - 1)));     // 遍历的同时将各个任务的冷却时间减少1，注意冷却时间最小值为0
            }
            if (maxValue != null) {     // 如果存在冷却时间为0且次数出现最多的任务。则选择该任务，重置该任务的冷却时间为n且将该任务的数量减少1
                if (maxCount == 1) {
                    hashMap.remove(maxValue);
                } else {
                    hashMap.put(maxValue, new Pair<>(hashMap.get(maxValue).getKey() - 1, n));
                }
            }
            res += 1;   // 不存在满足条件的任务则只能闲置一个时间片段
        }

        return res;
    }

    // 438. 找到字符串中所有字母异位词
    // 滑动窗口算法：字符串中的子串可以由起点和终点两个指针来进行确定，假设(i,j)表示起点为下标i，终点为下标j的子串
    // 因为字母异位词的长度必定和原来的单词保持一致，因此对于给定的i，只需要判断(i,i+p.length-1)一个子串是否为字母异位词即可
    // 而在i移动到下一个位置后，原来的哈希表也可以复用，只需要将下标i+p.length-1的字符加入到哈希表即可判断，因此算法设计如下：
    // (1) 当i==0时，将子串的元素(i,i+p.length-1)加入到哈希表中
    // (2) 当i>0时，只需要将下标i+p.length-1处的字符加入到哈希表中
    // (3) 判断当前哈希表是否和目标字符串的哈希表相同
    // (4) 将i处的元素从哈希表中的计数减去1，计数到0时则删除
    // (5) 将i自增1
    // 循环结束后即完成所有字母异位词的判断，需要注意特别i+p.length-1不能超过s自身长度的下标限制
    public List<Integer> findAnagrams(String s, String p) {
        HashMap<Character, Integer> targetSet = new HashMap<>();
        for (int i = 0; i < p.length(); i++) {
            targetSet.put(p.charAt(i), targetSet.getOrDefault(p.charAt(i), 0) + 1);
        }

        List<Integer> res = new ArrayList<>();
        HashMap<Character, Integer> curSet = new HashMap<>();

        for (int i = 0; i + p.length() - 1 < s.length(); i++) {  // 对于给定的i，只需要判断(i, i+p.length-1)的区间对应字符是否为字母异位词即可
            int index = i + p.length() - 1;
            if (i == 0) {   // i==0时，需要将(0, p.length)依次放入哈希表
                for (int j = 0; j <= index; j++) {    // 注意不要越界
                    curSet.put(s.charAt(j), curSet.getOrDefault(s.charAt(j), 0) + 1);
                }
            } else {    // 之后的循环中，只需要将i+p.length-1放入哈希表即可
                curSet.put(s.charAt(index), curSet.getOrDefault(s.charAt(index), 0) + 1);
            }

            if (targetSet.equals(curSet)) {
                res.add(i);
            }

            if (curSet.get(s.charAt(i)) == 1) { // i移动前从哈希表中减少计数，减少到0则进行删除
                curSet.remove(s.charAt(i));
            } else {
                curSet.put(s.charAt(i), curSet.get(s.charAt(i)) - 1);
            }
        }

        return res;
    }

    // 146. LRU 缓存
    // LRU缓存的实现：非常基础和重点的问题，LRU缓存的规则是，当要加入一个元素时，如果缓存容量不够，则将最近最少读取和写入的元素从缓存中删除
    // LRU缓存通常使用哈希表配合双向链表实现，实现上建议单独抽出一个将节点移动到链表最前面的方法，各个操作的处理如下所示：
    // (1) 读取：通过哈希表直接返回该key对应的链表节点，不存在该key则返回-1，存在则返回对应节点值，然后将该链表节点移动到链表最前面
    // (2) 写入：先判断该key是否存在于链表中，存在则直接进行修改，修改完毕再将该链表节点移动到链表最前面作为首节点，若不存在就插入到链表的最前面，如果链表节点数大于最大缓存数，则删除链表最后一个节点
    // (3) 将链表节点移动到链表最前面：移动的过程边缘情况很多，建议分为当前节点为首节点，当前节点为尾节点，当前节点为中间节点，三种情况分别进行处理，并且单独抽出该方法，因为上述的读取写入两种操作均需要使用该方法
    class LRUCache {

        // 双向链表节点
        class ListNode {
            ListNode prev;
            ListNode next;
            int key;
            int val;

            public ListNode() {
                this.prev = null;
                this.next = null;
                this.key = 0;
                this.val = 0;
            }

            public ListNode(int key, int val) {
                this.prev = null;
                this.next = null;
                this.key = key;
                this.val = val;
            }
        }

        private int capacity = 0;
        private ListNode head = null;
        private ListNode tail = null;
        private final HashMap<Integer, ListNode> hashMap;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.hashMap = new HashMap<>();
        }

        public int get(int key) {
            ListNode current = hashMap.get(key);
            if (current == null) {
                return -1;
            } else {
                moveNodeToFirstPos(current);
                return current.val;
            }
        }

        public void put(int key, int value) {
            if (hashMap.containsKey(key)) {
                hashMap.get(key).val = value;
                moveNodeToFirstPos(hashMap.get(key));
            } else {
                ListNode newNode = new ListNode(key, value);   // 创建新节点加入链表头部
                hashMap.put(key, newNode);
                if (head == null) {
                    head = newNode;
                    tail = head;
                } else {
                    newNode.next = head;
                    newNode.prev = null;
                    head.prev = newNode;
                    head = newNode;
                }

                if (hashMap.size() > capacity) {   // 加入新节点后如果缓存内节点超过容量，删除双向链表的尾节点
                    hashMap.remove(tail.key);
                    tail.prev.next = null;
                    tail = tail.prev;
                }
            }
        }

        private void moveNodeToFirstPos(ListNode current) {
            if (current == null || current == head) {   // 当前节点为空节点或者首节点，无需进行处理
                return;
            } else if (current == tail) {   // 当前节点为尾节点，进行特殊处理
                tail.prev.next = null;
                tail = tail.prev;

                current.next = head;
                head.prev = current;
                head = current;
            } else {    // 当前节点尾中间节点，一般情况处理
                ListNode prev = current.prev;
                ListNode next = current.next;

                prev.next = next;
                next.prev = prev;

                current.next = head;
                head.prev = current;
                head = current;
            }
        }
    }

}
