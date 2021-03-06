package com.monpro.generics.map;

import java.util.TreeMap;

class TreeMapRangeInterval {
  // startIndex - endIndex
  private TreeMap<Integer, Integer> map;

  private TreeMapRangeInterval() {
    map = new TreeMap<>();
  }

  public static void main(String[] args) {

    TreeMapRangeInterval intervals = new TreeMapRangeInterval();
    intervals.addRange(1, 5);
    intervals.addRange(6, 9);
    intervals.addRange(7, 15);
    intervals.removeRange(8, 10);
    System.out.println(intervals.queryRange(1, 4));
    System.out.println(intervals.queryRange(7, 9));
    System.out.println(intervals.map.toString());
  }

  private void addRange(int left, int right) {
    Integer start = map.floorKey(left);
    Integer end = map.floorKey(right);

    if (start == null && end == null) {
      map.put(left, right);
    } else if (start != null && map.get(start) >= left) {
      map.put(start, Math.max(right, Math.max(map.get(start), map.get(end))));
    } else {
      map.put(left, Math.max(right, map.get(end)));
    }

    // clear up interval
    map.subMap(left, false, right, true).clear();
  }

  private boolean queryRange(int left, int right) {
    Integer start = map.floorKey(left);

    return start != null && map.get(start) >= right;
  }

  private void removeRange(int left, int right) {
    if (right <= left) {
      return;
    }
    Integer start = map.floorKey(left);
    Integer end = map.floorKey(right);
    if (end != null && map.get(end) > right) {
      map.put(right, map.get(end));
    }
    if (start != null && map.get(start) > left) {
      map.put(start, left);
    }

    map.subMap(left, true, right, false).clear();
  }
}
