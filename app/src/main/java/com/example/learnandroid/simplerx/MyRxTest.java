package com.example.learnandroid.simplerx;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * this class test
 * rxjava operation
 */
public class MyRxTest {
    public static final String TAG = MyRxTest.class.getSimpleName();

    private Integer flag = 100;

    public void rxtest() {
        Log.d(TAG, "rxtest: ");
//        testMap();
//        testFlatMap();
//        testFlatMap2();
//        testConcatMap();
//        testBuffer();
//        testConcat();
//        testDefer();
//        testTimer();
        testInterval();
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

    public void testFlatMap() {
        Log.d(TAG, "testFloatMap: ");
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("商品" + i);
        }
        Observable
                .fromIterable(datas)
                .flatMap(new Function<String, ObservableSource<String>>() {
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

    private void testFlatMap2() {
        Log.d(TAG, "testFlatMap2: ");
        Observable
                .fromArray(1, 2, 3, 4, 5, 6)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: integer => " + integer);
                        return Observable.fromArray("商品 =》 " + integer.toString());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: s => " + s);
                    }
                });
    }

    private void testConcatMap() {
        Log.d(TAG, "testConcatMap: ");
        Observable
                .just(1, 2, 3, 4, 5)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: => " + integer.intValue());
                        List<String> datas = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            datas.add("拆分 " + integer + " " + i);
                        }
                        return Observable.fromIterable(datas);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept:  => " + s);
                    }
                });
    }

    private void testBuffer() {
        Log.d(TAG, "testBuffer: ");
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "Good boy " + integer;
                    }
                })
                .buffer(3, 2)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Log.d(TAG, "onNext: " + strings.size());
                        for (int i = 0; i < strings.size(); i++) {
                            String ret = strings.get(i);
                            Log.d(TAG, "onNext: " + ret);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void testConcat() {
        Log.d(TAG, "testConcat: ");
        Observable<Integer> observable1 = Observable
                .just(1, 2, 3, 4, 5);
        Observable<Integer> observable2 = Observable
                .just(6, 7, 8, 9, 10);
        Observable<Integer> observable3 = Observable
                .just(11, 12, 13, 14, 15);
        Observable<Integer> observable4 = Observable
                .just(16, 17, 18, 19, 20);

        Observable
                .concat(observable1, observable2, observable3, observable4)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: => " + integer.intValue());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }


    private void testDefer() {
        Log.d(TAG, "testDefer: ");

        Observable<Integer> observable = Observable
                .defer(new Callable<ObservableSource<Integer>>() {

                    @Override
                    public ObservableSource<Integer> call() throws Exception {
                        return Observable.just(flag);
                    }
                });

        flag = 200;

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: => " + integer.intValue());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    private void testTimer(){
        Log.d(TAG, "testTimer: 延迟");
        Observable
                .timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext: => " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    private void testInterval(){
        Log.d(TAG, "testInterval: 测试周期执行任务");

        Observable
                .interval(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext: "+ aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }

}
