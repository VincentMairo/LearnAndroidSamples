package com.example.learnandroid.cache;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * a simple cache class
 *
 * @param <T>
 */
public class MyCache<T> {
    private static final String TAG = MyCache.class.getSimpleName();

    private static final long DEFAULT_OVERDUETIME = 3000;

    private Map<String, CacheNode<T>> mCacheMap = new HashMap<>();

    public MyCache() {

    }

    /**
     * 缓存
     *
     * @param key
     * @param value
     */
    public void cache(String key, T value) {
        Log.d(TAG, "cache: key=> " + key);
        cache(key, value, DEFAULT_OVERDUETIME);
    }

    public void cache(String key, T value, long overdueTime) {
        Log.d(TAG, "cache: key=> " + key);
        CacheNode<T> cacheNode;
        if (mCacheMap.containsKey(key)) {
            cacheNode = mCacheMap.get(key);
        } else {
            cacheNode = new CacheNode();
        }
        cacheNode.value = value;
        cacheNode.expirationTime = System.currentTimeMillis() + overdueTime;
        mCacheMap.put(key, cacheNode);
    }

    /**
     * 取出缓存
     *
     * @param key
     * @return
     */
    public T getCache(String key) {
        if (!mCacheMap.containsKey(key)) {
            Log.d(TAG, "getCache: 无缓存");
            return null;
        }
        CacheNode<T> cacheNode = mCacheMap.get(key);
        if (System.currentTimeMillis() > cacheNode.expirationTime) {
            Log.d(TAG, "getCache: 缓存过期，清除缓存");
            return null;
        }
        return cacheNode.value;
    }

    static class CacheNode<T> {
        T value;
        long expirationTime;
    }
}
