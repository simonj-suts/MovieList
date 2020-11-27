package com.example.movielist;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ImageSingleton {
    private static ImageSingleton mySingleton;
    private final ImageLoader imageLoader;

    private ImageSingleton(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(4);

            @Override
            public Bitmap getBitmap(String url) {
                Bitmap bmp = cache.get(url);
                if (bmp==null)
                    System.out.println("Image not in cache");
                else
                    System.out.println("Image in cache");
                return bmp;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                System.out.println("put Bitmap");
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized ImageSingleton getInstance(Context context){
        if (mySingleton==null){
            mySingleton = new ImageSingleton(context);
        }
        return mySingleton;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
