package com.example.learnandroid.use;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learnandroid.R;
import com.example.learnandroid.use.api.WanAndroidApi;
import com.example.learnandroid.use.bean.ProjectBean;
import com.example.learnandroid.use.bean.ProjectItem;
import com.example.learnandroid.use.util.HttpUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UseActivity.class.getSimpleName();

    private WanAndroidApi api;

    private Button mProjectFenleiBtn;
    private Button mProjectListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);

        api = HttpUtil.getOnlineCookieRetrofit().create(WanAndroidApi.class);
        initView();
    }

    private void initView() {
        mProjectFenleiBtn = ((Button) findViewById(R.id.btn_projectfenlei));
        mProjectListBtn = ((Button) findViewById(R.id.btn_projectlistaction));
        mProjectFenleiBtn.setOnClickListener(this);
        mProjectListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_projectfenlei:
                getProjectAction();
                break;
            case R.id.btn_projectlistaction:
                getProjectListAction();
                break;
            default:
                break;
        }
    }

    public void getProjectAction() {
        api.getProject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProjectBean>() {
                    @Override
                    public void accept(ProjectBean projectBean) throws Exception {
                        Log.d(TAG, "accept: " + projectBean.toString());
                    }
                });
    }

    public void getProjectListAction() {
        api.getProjectItem(1, 294)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProjectItem>() {
                    @Override
                    public void accept(ProjectItem projectItem) throws Exception {
                        Log.d(TAG, "accept: " + projectItem.toString());
                    }
                });
    }

}
