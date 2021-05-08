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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mImg;
    private ProgressDialog dialog;
    private MyCache<Bitmap> mBitmapCache;
    private List<String> mPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initView();
    }

    private void init() {
        mBitmapCache = new MyCache<>();

        String path1 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3355464299,584008140&fm=26&gp=0.jpg";
        String path2 = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=78356917,4120977343&fm=224&gp=0.jpg";
        String path3 = "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/3b87e950352ac65cd8bcc0aafef2b21192138aaa.jpg";
        String path4 = "https://pics4.baidu.com/feed/dc54564e9258d10994c70d296164f1b96d814dc3.jpeg?token=8911932d3f3c4631fa431359490dfe45&s=C0A590520853E3E948782DEE0300F0A2";
        String path5 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.17qq.com%2Fuploads%2Fclnnkhdhlv.jpeg&refer=http%3A%2F%2Fpic.17qq.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623056127&t=324a58be2384b53b9857b1560486c4cb";
        String path6 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.yao51.com%2Fjiankangtuku%2Fgllgdhclmv.jpeg&refer=http%3A%2F%2Fimg.yao51.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623056127&t=79a287a5d86145ab52fa203462e19cab";
        String path7 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.17qq.com%2Fimg_biaoqing%2F21102387.jpeg&refer=http%3A%2F%2Fwww.17qq.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623056127&t=bce9bdfdd66aace8666782daa4e1f885";
        String path8 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F12146131984%2F1000.jpg&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623056127&t=29595cbbd8823aa7eceb98887915e546";

        mPaths = new ArrayList<>();
        mPaths.add(path1);
        mPaths.add(path2);
        mPaths.add(path3);
        mPaths.add(path4);
        mPaths.add(path5);
        mPaths.add(path6);
        mPaths.add(path7);
        mPaths.add(path8);
    }

    private void initView() {
        findViewById(R.id.btn_invokerx).setOnClickListener(this);
        findViewById(R.id.btn_rxdownloadpic).setOnClickListener(this);
        mImg = ((ImageView) findViewById(R.id.img_pic));
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

        Random random = new Random();
        int index = random.nextInt(mPaths.size());
        Log.d(TAG, "rxDownloadPic: index => " + index);
        String path = mPaths.get(index);
        Observable
                .just(path)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Log.d(TAG, "apply: " + Thread.currentThread().getName());
                        Log.d(TAG, "apply: => " + s);
                        Bitmap bitmap = mBitmapCache.getCache(s);
                        if (bitmap != null) {
                            Log.d(TAG, "apply: 命中缓存");
                            return bitmap;
                        }
                        URL url = new URL(s);
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        httpsURLConnection.setConnectTimeout(3000);
                        int responseCode = httpsURLConnection.getResponseCode();
                        Log.d(TAG, "apply: responseCode " + responseCode);
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            InputStream inputStream = httpsURLConnection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            mBitmapCache.cache(s, bitmap);
                            return bitmap;
                        }

                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        dialog = new ProgressDialog(MainActivity.this);
                        dialog.setTitle("download run");
                        dialog.show();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.d(TAG, "onNext: ");
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
