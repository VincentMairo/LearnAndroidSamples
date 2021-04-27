package com.example.learnandroid.simplerx;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MyRxTest {
    public static final String TAG = MyRxTest.class.getSimpleName();

    public void rxtest() {
        Log.d(TAG, "rxtest: ");
//        testMap();
        testFlatMap();
    }

    private void testMap() {
        Log.d(TAG, "textMap: ");
        Observable
                .just(1, 2, 3, 4)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return Integer.toString(integer);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: ".concat(s));
                    }
                });
    }

    public void testFlatMap(){
        Log.d(TAG, "testFloatMap: ");
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("商品" + i);
        }
        Observable
                .fromIterable(datas)
                .flatMap(new Function<String, ObservableSource<String>>(){
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        Log.d(TAG, "apply: ".concat(s));
                        List<String> datas = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            datas.add(s + " - " + i);
                        }
                        return Observable.fromIterable(datas);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: ".concat(s));
                    }
                });
    }

    

}
