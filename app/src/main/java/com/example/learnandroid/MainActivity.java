package com.example.learnandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.learnandroid.cache.MyCache;
import com.example.learnandroid.simplerx.MyRxTest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String PATH = "http://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.juimg.com%2Ftuku%2Fyulantu%2F140712%2F330651-140G21354039.jpg&refer=http%3A%2F%2Fimg.juimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622904519&t=7b8e2bd1aae568f2c11e010e7625e7e8";

    private ImageView mImg;
    private ProgressDialog dialog;
    private MyCache<Bitmap> mMyBitmapCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initView();
    }

    private void init() {
        mMyBitmapCache = new MyCache<>();
    }

    private void initView() {
        findViewById(R.id.btn_invokerx).setOnClickListener(this);
        findViewById(R.id.btn_rxdownloadpic).setOnClickListener(this);
        mImg = ((ImageView) findViewById(R.id.img));
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.btn_invokerx:
                new MyRxTest().rxtest();
                break;
            case R.id.btn_rxdownloadpic:
                rxDownloadPic();
                break;
            default:
                break;
        }
    }

    private void rxDownloadPic() {
        Observable
                .just(PATH)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Log.d(TAG, "apply: => " + s + " " + Thread.currentThread().getName());
                        if (mMyBitmapCache.getCache(s) != null) {
                            Log.d(TAG, "apply: 缓存中有，直接从缓存中取");
                            return mMyBitmapCache.getCache(s);
                        }
                        Log.d(TAG, "apply: 缓存中没有，从网络获取");
                        URL url = new URL(s);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(3000);
                        int responseCode = httpURLConnection.getResponseCode();
                        Log.d(TAG, "apply: code => " + responseCode);
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Log.d(TAG, "apply: 从网络取出了图片，存入缓存");
                            mMyBitmapCache.cache(s,bitmap);
                            return bitmap;
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + Thread.currentThread().getName());
                        dialog = new ProgressDialog(MainActivity.this);
                        dialog.setTitle("download run");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                        mImg.setImageBitmap(bitmap);
                        dialog.hide();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        dialog.hide();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

}
