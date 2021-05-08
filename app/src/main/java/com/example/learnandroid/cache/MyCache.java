package com.example.learnandroid.cache;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MyCache<T> {
    private static final String TAG = MyCache.class.getSimpleName();

    private static final long DEFAULT_OVERDUETIME = 120 * 1000;

    private Map<String, CacheNode<T>> mCacheMap = new HashMap<>();

    public void cache(String key, T value) {
        CacheNode cacheNode;
        if (mCacheMap.containsKey(key)) {
            Log.d(TAG, "cache: 更新缓存");
            cacheNode = mCacheMap.get(key);
        } else {
            Log.d(TAG, "cache: 新建缓存");
            cacheNode = new CacheNode();
        }
        cacheNode.value = value;
        cacheNode.overdueTime = System.currentTimeMillis() + DEFAULT_OVERDUETIME;
        mCacheMap.put(key, cacheNode);
    }

    public T getCache(String key) {
        if (!mCacheMap.containsKey(key)) {
            Log.d(TAG, "getCache: 未命中缓存");
            return null;
        }
        CacheNode<T> cacheNode = mCacheMap.get(key);
        if (cacheNode.overdueTime < System.currentTimeMillis()) {
            Log.d(TAG, "getCache: 缓存过期,清除缓存");
            mCacheMap.remove(key);
            cacheNode = null;
            return null;
        }
        Log.d(TAG, "getCache: 返回缓存");
        return cacheNode.value;
    }

    // TODO: 2021/5/8 cacheNodePool
    static class CacheNode<T> {
        T value;
        long overdueTime;
    }
}
