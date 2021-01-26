package com.monpro.generics.util;

import java.util.*;

class Interval {
  int start, end;

  Interval(int start, int end) {
    this.start = start;
    this.end = end;
  }
}

public class ArrayUtils {
  private ArrayUtils() {}

  public static boolean skipToLastIndex(int[] nums) {
    /**
     * O(n^2) solution
     *
     * <p>boolean[] dp = new boolean[nums.length]; dp[0] = true; int n = nums.length; for(int i = 1;
     * i < n; i++) { for(int j = 0; j < i; j++) { if(dp[j] == true&& j + nums[j] >= i) { dp[i] =
     * true; } } } return dp[n - 1];
     */
    int maxSkipPosition = 0;
    for (int i = 0; i < nums.length; i++) {
      if (maxSkipPosition < i) {
        return false;
      }
      if (i + nums[i] > maxSkipPosition) {
        maxSkipPosition = i + nums[i];
      }
    }
    return true;
  }

  public static int minStepsToLastIndex(int[] nums) {
    int result = 0;
    int curFurthest = 0, curEnd = 0;

    for (int cur = 0; cur < nums.length - 1; cur++) {
      curFurthest = Math.max(cur + nums[cur], curFurthest);
      if (cur == curEnd) {
        result += 1;
        curEnd = curFurthest;
      }
    }
    return result;
  }

  public static boolean canReachToIndexWithValueZero(int[] nums, int start) {
    Stack<Integer> stack = new Stack<>();
    Set<Integer> set = new HashSet<>();
    stack.add(start);

    while (!stack.isEmpty()) {
      int index = stack.pop();
      if (nums[index] == 0) {
        return true;
      }
      int left = index - nums[index];
      int right = index + nums[index];
      if (left >= 0 && !set.contains(left)) {
        stack.add(left);
        set.add(left);
      }
      if (right < nums.length && !set.contains(right)) {
        stack.add(right);
        set.add(right);
      }
    }
    return false;
  }

  public static int shortestPathInBinaryMatrix(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) {
      return -1;
    }
    boolean[][] visited = new boolean[m][n];
    Queue<Point> queue = new LinkedList<>();
    queue.add(new Point(0, 0, 1));
    int[][] directions =
        new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, -1}, {-1, 1}, {1, 1}, {-1, -1}};

    while (!queue.isEmpty()) {
      int size = queue.size();
      Point point = queue.poll();
      if (point.x == m - 1 && point.y == n - 1) {
        return point.val;
      }
      for (int[] direction : directions) {
        int nextX = point.x + direction[0];
        int nextY = point.y + direction[1];
        if (isValid(grid, visited, nextX, nextY)) {
          queue.add(new Point(nextX, nextY, point.val + 1));
          grid[nextX][nextY] = 1;
        }
      }
    }
    return -1;
  }

  public static boolean isValid(int[][] grid, boolean[][] visited, int x, int y) {
    int m = grid.length, n = grid[0].length;
    return x >= 0 && x <= m - 1 && y >= 0 && y <= n - 1 && grid[x][y] == 0;
  }

  public static int getSumOfDiagonal(int[][] mat) {
    int result = 0;
    int x = 0, y = 0;
    int m = mat.length;
    while (x < m && y < m) {
      result += mat[x][y];
      x += 1;
      y += 1;
    }

    x = 0;
    y = m - 1;
    while (x < m && y >= 0) {
      result += mat[x][y];
      x += 1;
      y -= 1;
    }

    if (m % 2 == 1) {
      result -= mat[m / 2][m / 2];
    }
    return result;
  }

  public static int[] transfromCells(int[] cells, int N) {
    Map<String, Integer> cache = new HashMap<>();
    while (N > 0) {
      int[] nextCells = new int[8];
      cache.put(Arrays.toString(cells), N);
      N -= 1;
      for (int i = 1; i < 7; i++) {
        nextCells[i] = cells[i - 1] == cells[i + 1] ? 1 : 0;
      }
      cells = nextCells;
      String key = Arrays.toString(cells);
      if (cache.containsKey(key)) {
        N %= cache.get(key) - N;
      }
    }
    return cells;
  }

  public static int maxPointsOnSameLine(Point[] points) {
    if (points == null) {
      return 0;
    }
    int result = 0;
    Map<String, Integer> count = new HashMap<>();
    for (int i = 0; i < points.length; i++) {
      count.clear();
      int sameLine = 0, vertical = 0, overlap = 0;
      for (int j = i + 1; j < points.length; j++) {
        if (points[j].x == points[i].x) {
          if (points[j].y == points[i].y) {
            overlap += 1;
          } else {
            vertical += 1;
          }
          continue;
        }
        int dy = points[j].y - points[i].y;
        int dx = points[j].x - points[i].x;
        int gcd = getGcd(dy, dx);
        dy /= gcd;
        dx /= gcd;
        String key = dy + "/" + dx;
        count.put(key, count.getOrDefault(key, 0) + 1);
        sameLine = Math.max(sameLine, count.get(key));
      }
      sameLine = Math.max(sameLine, vertical);
      result = Math.max(result, sameLine + overlap + 1);
    }
    return result;
  }

  private static int getGcd(int a, int b) {
    if (b == 0) {
      return a;
    } else {
      return getGcd(b, a % b);
    }
  }

  public static int minimumPointsCombineArray(List<Integer> sticks) {
    // write your code here
    PriorityQueue<Integer> queue = new PriorityQueue<>();
    for (Integer stick : sticks) {
      queue.add(stick);
    }
    int result = 0;
    while (queue.size() > 1) {
      Integer stick = queue.poll();
      stick += queue.poll();
      result += stick;
      queue.add(stick);
    }
    return result;
  }

  public static int throwCount(float[] bags, float limit) {
    int left = 0, right = bags.length - 1;
    int result = 0;
    Arrays.sort(bags);

    while (left <= right) {
      while (left < right && bags[left] + bags[right] > limit) {
        right -= 1;
        result += 1;
      }

      if (left == right || bags[left] + bags[right] <= limit) {
        right -= 1;
        left += 1;
        result += 1;
      }
    }
    return result;
  }

  public static boolean canPartition(int[] nums) {
    if (nums.length < 5) return false;
    int prefix = 0, suffix = nums[nums.length - 1];
    Map<Integer, Integer> prefix_sum = new HashMap<>();
    for (int i = 1; i < nums.length - 3; i++) {
      prefix += nums[i - 1];
      prefix_sum.put(prefix, i);
    }
    int total = 0;
    for (int n : nums) total += n;
    for (int i = nums.length - 2; i >= 3; i--) {
      if (prefix_sum.containsKey(suffix) && prefix_sum.get(suffix) <= i - 2) {
        int mid = total - 2 * suffix - nums[i] - nums[prefix_sum.get(suffix)];
        if (mid == suffix) return true;
      }
      suffix += nums[i];
    }
    return false;
  }

  public static List<String> convertToPostfixNotation(String[] expression) {
    if (expression == null || expression.length == 0) {
      return null;
    }

    List<String> result = new ArrayList<>();
    Stack<String> stack = new Stack<>();

    for (String str : expression) {
      if (str.equals("(")) {
        stack.push(str);
      } else if (str.equals(")")) {
        while (!stack.peek().equals("(")) {
          result.add(stack.pop());
        }
        stack.pop();
      } else if (Character.isDigit(str.charAt(0))) {
        result.add(str);
      } else {
        // stack only includes symbols
        // we need to add the symbol with highest priority first
        while (!stack.isEmpty() && getPriority(stack.peek()) >= getPriority(str)) {
          result.add(stack.pop());
        }
        stack.add(str);
      }
    }

    while (!stack.isEmpty()) {
      result.add(stack.pop());
    }

    return result;
  }

  private static int getPriority(String str) {
    if (str.equals("*") || str.equals("/")) {
      return 3;
    } else if (str.equals("+") || str.equals("-")) {
      return 2;
    } else if (str.equals("(")) {
      return 1;
    }
    return 0;
  }

  public static List<Long> getSumOfIntervals(int[] nums, List<Interval> intervals) {
    List<Long> result = new ArrayList<>();
    if (intervals == null || intervals.size() == 0 || nums == null || nums.length == 0) {
      return result;
    }
    long[] prefixSum = new long[nums.length + 1];
    long prefix = 0;
    for (int i = 1; i < nums.length + 1; i++) {
      prefix += nums[i - 1];
      prefixSum[i] = prefix;
    }
    for (Interval interval : intervals) {
      int start = interval.start;
      int end = interval.end;
      result.add(prefixSum[end + 1] - prefixSum[start]);
    }
    return result;
  }

  public static boolean bipartitionArray(int N, int[][] differentGroup) {
    // if we tried to do the partition
    // then dislike[i] cannot be in same group
    // we need to find the dislike mapping
    // [1: {2, 3}]
    Map<Integer, List<Integer>> mapping = new HashMap<>();
    for (int i = 1; i <= N; i++) {
      mapping.put(i, new ArrayList<Integer>());
    }

    for (int[] dislike : differentGroup) {
      int dislikeFront = dislike[0], dislikeEnd = dislike[1];
      List<Integer> frontList = mapping.get(dislikeFront);
      List<Integer> endList = mapping.get(dislikeEnd);
      frontList.add(dislikeEnd);
      endList.add(dislikeFront);
      mapping.put(dislikeFront, frontList);
      mapping.put(dislikeEnd, endList);
    }
    // 1 means first group
    // -1 means second group
    int[] group = new int[N + 1];
    for (int i = 1; i < N; i++) {
      if (group[i] == 0 && !dfs(mapping, group, i, -1)) {
        return false;
      }
    }
    return true;
  }

  public static boolean dfs(
      Map<Integer, List<Integer>> mapping, int[] group, int index, int color) {
    if (group[index] == 0) {
      group[index] = color;
      int anotherColor = color == -1 ? 1 : -1;
      for (int i : mapping.get(index)) {
        if (!dfs(mapping, group, i, anotherColor)) {
          return false;
        }
      }
    } else {
      if (group[index] != color) {
        return false;
      }
    }
    return true;
  }

  public static boolean bipartitionArrayTraverse(int N, int[][] differentGroup) {
    // if we tried to do the partition
    // then dislike[i] cannot be in same group
    // we need to find the dislike mapping
    // [1: {2, 3}]
    Map<Integer, List<Integer>> mapping = new HashMap<>();
    for (int i = 1; i <= N; i++) {
      mapping.put(i, new ArrayList<Integer>());
    }

    for (int[] dislike : differentGroup) {
      int dislikeFront = dislike[0], dislikeEnd = dislike[1];
      List<Integer> frontList = mapping.get(dislikeFront);
      List<Integer> endList = mapping.get(dislikeEnd);
      frontList.add(dislikeEnd);
      endList.add(dislikeFront);
      mapping.put(dislikeFront, frontList);
      mapping.put(dislikeEnd, endList);
    }
    // 1 means first group
    // -1 means second group
    int[] group = new int[N + 1];
    for (int i = 1; i <= N; i++) {
      if (group[i] == 0) {
        group[i] = -1;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(i);
        while (!queue.isEmpty()) {
          int node = queue.poll();
          for (int dislike : mapping.get(node)) {
            if (group[dislike] == 0) {
              group[dislike] = group[node] == 1 ? -1 : 1;
              queue.offer(dislike);
            } else {
              if (group[dislike] == group[node]) {
                return false;
              }
            }
          }
        }
      }
    }
    return true;
  }

  public static List<Integer> smallestSetOfStartingPoints(int n, List<List<Integer>> edges) {
    List<Integer> result = new ArrayList<>();
    int[] visited = new int[n];
    for (List<Integer> edge : edges) {
      visited[edge.get(1)] = 1;
    }
    for (int i = 0; i < n; i++) {
      if (visited[i] == 0) {
        result.add(i);
      }
    }
    return result;
  }

  public static boolean isPossibleEquations(String[] equations) {
    // when will a == b, b
    int[] parent = new int[26];
    for (int i = 0; i < 26; i++) {
      parent[i] = i;
    }
    for (String equation : equations) {
      if (equation.charAt(1) == '=') {
        parent[find(equation.charAt(0) - 'a', parent)] = find(equation.charAt(3) - 'a', parent);
      }
    }
    for (String equation : equations) {
      if (equation.charAt(1) == '!'
          && (find(equation.charAt(0) - 'a', parent) == find(equation.charAt(3) - 'a', parent))) {
        return false;
      }
    }
    return true;
  }

  public static int minimumCostForConnections(int N, int[][] connections) {
    int result = 0, numOfNodes = N;
    Arrays.sort(connections, (a, b) -> a[2] - b[2]);
    int[] parent = new int[N];
    for (int i = 0; i < N; i++) {
      parent[i] = i;
    }

    for (int[] connection : connections) {
      int start = connection[0] - 1, end = connection[1] - 1;
      int startParent = find(start, parent), endParent = find(end, parent);
      if (startParent != endParent) {
        parent[startParent] = endParent;
        numOfNodes -= 1;
        result += connection[2];
      }
    }

    return numOfNodes == 1 ? result : -1;
  }

  public static int find(int x, int[] parent) {
    if (x != parent[x]) {
      parent[x] = find(parent[x], parent);
    }
    return parent[x];
  }

  public static int getDelayTime(int[][] times, int n, int k) {
    int result = 0;
    // [source: {target: dist}]
    Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
    boolean[] visited = new boolean[n + 1];
    Queue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.add(new int[] {0, k});
    for (int[] time : times) {
      map.putIfAbsent(time[0], new HashMap<>());
      map.get(time[0]).put(time[1], time[2]);
    }

    while (!pq.isEmpty()) {
      int[] element = pq.remove();
      int curDist = element[0], curNode = element[1];
      if (visited[curNode]) {
        continue;
      }
      visited[curNode] = true;
      result = curDist;
      n -= 1;
      if (map.containsKey(curNode)) {
        for (int nextNode : map.get(curNode).keySet()) {
          pq.add(new int[] {curDist + map.get(curNode).get(nextNode), nextNode});
        }
      }
    }

    return n == 0 ? result : -1;
  }

  public static boolean isValidPathForStreets(int[][] grid) {

    if (grid == null || grid.length == 0) {
      return false;
    }
    int m = grid.length, n = grid[0].length;
    int[][][] dirs =
        new int[][][] {
          {{0, -1}, {0, 1}},
          {{-1, 0}, {1, 0}},
          {{0, -1}, {1, 0}},
          {{0, 1}, {1, 0}},
          {{-1, 0}, {0, -1}},
          {{-1, 0}, {0, 1}}
        };
    boolean[][] visited = new boolean[m][n];
    Queue<int[]> queue = new LinkedList<>();
    queue.add(new int[] {0, 0});
    visited[0][0] = true;

    while (!queue.isEmpty()) {
      int[] coordinate = queue.poll();
      int curRow = coordinate[0], curCol = coordinate[1];
      int num = grid[curRow][curCol] - 1;
      if (curRow == m - 1 && curCol == n - 1) {
        return true;
      }
      for (int[] dir : dirs[num]) {
        int nextRow = curRow + dir[0], nextCol = curCol + dir[1];
        if (nextRow < 0
            || nextRow >= m
            || nextCol < 0
            || nextCol >= n
            || visited[nextRow][nextCol]) {
          continue;
        }
        for (int[] backDir : dirs[grid[nextRow][nextCol] - 1]) {
          if (nextRow + backDir[0] == curRow && nextCol + backDir[1] == curCol) {
            visited[nextRow][nextCol] = true;
            queue.add(new int[] {nextRow, nextCol});
          }
        }
      }
    }
    return false;
  }


  public static List<List<String>> accountsMerge(List<List<String>> accounts) {
    // for each email we need to know it's owner
    // for each email we need to konw it's parent
    // after that we need to iterate all emails
    // parent is key
    // email in sorted order TreeSet<>();


    Map<String, String> owner = new HashMap<>();
    Map<String, String> parents = new HashMap<>();
    Map<String, TreeSet<String>> unions = new HashMap<>();
    List<List<String>> result = new ArrayList<>();

    for(List<String> list: accounts) {
      for(int i = 1; i < list.size(); i++) {
        parents.put(list.get(i), list.get(i));
        owner.put(list.get(i), list.get(0));
      }
    }
    for(List<String> list: accounts) {
      String parent = accountFind(list.get(1), parents);
      for(int i = 2; i < list.size(); i++) {
        parents.put(accountFind(list.get(i), parents), parent);
      }
    }
    // finish the find then try to union
    // list.get(1) is the parent of all remaining elements in list
    for(List<String> list: accounts) {
      String parent = accountFind(list.get(1), parents);
      unions.putIfAbsent(parent, new TreeSet<>());
      for(int i = 1; i < list.size(); i++) {
        unions.get(parent).add(list.get(i));
      }
    }

    for(String parent: unions.keySet()) {
      String name = owner.get(parent);
      List<String> emails = new ArrayList<>(unions.get(parent));
      emails.add(0, name);
      result.add(emails);
    }

    return result;
  }

  private static String accountFind(String email, Map<String, String> parents) {
    if(email != parents.get(email)) {
      parents.put(email, accountFind(parents.get(email), parents));
      return parents.get(email);
    }
    return parents.get(email);
  }


  public static int minCostToConnectPoints(int[][] points) {

    int m = points.length;
    int result = 0, island = m;
    int[] parents = new int[m];
    for(int i = 0; i < m; i++) {
      parents[i] = i;
    }
    int[][] dist = new int[m][m];
    PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> dist[a[0]][a[1]] - dist[b[0]][b[1]]);

    for(int i = 0; i < m; i++) {
      for(int j = 1; j < m; j++) {
        dist[i][j] = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
        queue.offer(new int[]{i, j});
      }
    }

    while(!queue.isEmpty() && island > 1) {
      int[] node = queue.poll();
      if(pointsUnion(node[0], node[1], parents)) {
        result += dist[node[0]][node[1]];
        island -= 1;
      }
    }
    return result;
  }

  private static boolean pointsUnion(int node1, int node2, int[] parents) {
    int p1 = pointsFind(node1, parents);
    int p2 = pointsFind(node2, parents);
    if(p1 == p2) {
      return false;
    }
    parents[p1] = p2;
    return true;
  }

  private static int pointsFind(int node, int[] parents) {
    int parent = parents[node];
    if(parent == node) {
      return parent;
    }
    parents[node] = pointsFind(parent, parents);
    return parents[node];
  }
}
