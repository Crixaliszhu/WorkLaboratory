package com.yupao.happynewd.lru;

import android.os.Build;

import java.util.LinkedHashMap;

/**
 * LRUCache 实现使用现成的数据结构 LinkedHashMap
 */
public class LRUCache extends LinkedHashMap<Integer, Integer> {

    int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    public int get(int key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return super.getOrDefault(key, -1);
        } else {
            if (super.containsKey(key)) {
                return super.get(key);
            } else {
                return -1;
            }
        }
    }

    public void put(int key, int value) {
        super.put(key,value);
    }

    @Override
    protected boolean removeEldestEntry(Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}
