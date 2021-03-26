package com.easy.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 图片加载
 * Created by chenyouyu on 3/25/21 5:11 PM
 */
public class ImageLoader {
    ImageCache mImageCache = new ImageCache();

    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // 获取 UI 线程的 looper
    Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 显示网络图片
     *
     * @param url       网络图片地址
     * @param imageView 显示图片的ImageView组件
     */
    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImageView(imageView, bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 图片下载
     *
     * @param imageUrl 网络图片地址
     * @return
     */
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
