package com.easy.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 图片缓存
 * Created by chenyouyu on 3/26/21 11:20 AM
 */
public class ImageCache {
    // 使用 LruCache 的方式缓存图片
    LruCache<String, Bitmap> mLruCache;

    public ImageCache() {
        initImageCache();
    }

    /**
     * 初始化缓存
     */
    private void initImageCache() {
        // 计算可以使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    /**
     * 缓存图片
     *
     * @param url
     * @param bitmap
     */
    public void put(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }

    /**
     * 获取缓存的图片
     *
     * @param url
     * @return
     */
    public Bitmap get(String url) {
        return mLruCache.get(url);
    }
}
