package com.company;

import javafx.util.Pair;

import java.util.*;

public class Solution50To55 {

    // 49. 字母异位词分组
    // 哈希表应用：本题实际上很简单，实际上只要记录每个单词中的字母以及字母的出现次数，以这个生成唯一哈希值作为key，然后即可使用哈希表进行归类了
    // 实际上本题还有多种解法，比如可以根据质数的性质，给26个小写字母都给定一个质数代表，字符串的各个字母代表的质数相乘作为key，这样由相同字母只是顺序不同的字符串乘出来的结果就是相同的，或者对每个单词做从小到大按顺序排序等
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<HashMap<Character, Integer>, List<String>> res = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            HashMap<Character, Integer> characters = new HashMap<>();
            for (int j = 0; j < strs[i].length(); j++) {
                characters.put(strs[i].charAt(j), characters.getOrDefault(strs[i].charAt(j), 0) + 1);
            }
            List<String> subRes = res.getOrDefault(characters, new ArrayList<>());
            subRes.add(strs[i]);
            res.put(characters, subRes);
        }
        return new ArrayList<>(res.values());
    }

    // 207. 课程表
    // 判断有向图是否存在环：本题的核心在于判断有向图中是否存在环，判断有向图是否存在环标准方法是拓扑排序，只有有向无环图可以顺利完成拓扑排序算法
    // 拓扑排序算法：每次从有向图中取出一个入度为0的节点，然后从图中删除该节点以及该节点连接的所有边，循环上述操作直到图中没有节点，则这时拓扑排序完成，有向图中没有环，如果循环中出现找不到一个节点入度为0，则图中存在环
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        boolean[][] map = new boolean[numCourses][numCourses];
        for (int i = 0; i < prerequisites.length; i++) {
            map[prerequisites[i][1]][prerequisites[i][0]] = true;   // 构建有向图，假设先学i课程才能学j课程，则map[i][j]=true
        }
        HashMap<Integer, Integer> hashMap = new HashMap<>();    // 拓扑排序简单实现可以直接用哈希表，键为节点标识，值为节点入度
        for (int i = 0; i < numCourses; i++) {
            int degree = 0;
            for (int j = 0; j < numCourses; j++) {
                if (map[j][i]) {
                    degree += 1;
                }
            }
            hashMap.put(i, degree);
        }
        while (!hashMap.isEmpty()) {
            int targetNode = -1;
            for (Integer key: hashMap.keySet()) {
                if (hashMap.get(key) == 0) {    // 每次选出一个入度为0的节点
                    targetNode = key;
                    break;
                }
            }
            if (targetNode == -1) { // 循环中不存在入度为0的节点，则有向图中存在环
                return false;
            } else {
                hashMap.remove(targetNode);
                for (int i = 0; i < numCourses; i++) {  // 从图中去掉该节点和对应的边，然后更新各个节点的入度
                    if (map[targetNode][i]) {
                        map[targetNode][i] = false;
                        hashMap.put(i, hashMap.get(i) - 1);
                    }
                }
            }
        }
        return true;
    }

    // 215. 数组中的第K个最大元素
    // 基本排序算法：各种排序算法可以参考数据结构的复习部分实现，这里直接调用库函数
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // 238. 除自身以外数组的乘积
    // 数组特殊问题：实际上用除法的话直接计算整个数组的乘积和再用除法去除自身值即可，本题不允许使用除法的话，可以一次循环计算前数若干个数字和倒数若干个数字的乘积，再进行组合乘法即可
    public int[] productExceptSelf(int[] nums) {
        int[] order = new int[nums.length];
        int[] reverse = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                order[0] = nums[0];
                reverse[nums.length - 1] = nums[nums.length - 1];
            } else {
                order[i] = order[i - 1] * nums[i];
                reverse[nums.length - 1 - i] = reverse[nums.length - i] * nums[nums.length - 1 - i];
            }
        }

        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = (i - 1 >= 0? order[i - 1]: 1) * (i + 1 < nums.length? reverse[i + 1]: 1);
        }

        return res;
    }

    // 240. 搜索二维矩阵 II
    // 二叉搜索树：按照题目给出的矩阵性质，矩阵的每一行从左到右递增，再考虑到矩阵的每一列从上到下递增
    // 这时只要从右上角开始出发，向左走一步则元素值减小，向下走一步则元素值增大。相当于将右上角当作根节点，整个矩阵变为一个二叉搜索树，每个元素的左侧元素为该元素的左子节点，每个元素的下侧元素为该元素的右子节点
    public boolean searchMatrix(int[][] matrix, int target) {
        int i = 0, j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            if (matrix[i][j] < target) {    // 当前节点值小于目标值，往右子树即下侧走
                i += 1;
            } else if (matrix[i][j] > target) {     // 当前节点值大于目标值，往左子树即左侧走
                j -= 1;
            } else {
                return true;
            }
        }
        return false;
    }

}
