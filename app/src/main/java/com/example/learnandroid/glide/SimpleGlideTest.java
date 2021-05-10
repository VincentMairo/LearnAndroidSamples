package com.example.learnandroid.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.learnandroid.MainActivity;

public class SimpleGlideTest {

    public void test(Context context) {
        Glide.with(context).load("");
    }

}
