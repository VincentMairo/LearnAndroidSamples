package com.example.learnandroid.use.api;

import com.example.learnandroid.use.bean.ProjectBean;
import com.example.learnandroid.use.bean.ProjectItem;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WanAndroidApi {

    //总数据
    @GET("project/tree/json")
    Observable<ProjectBean> getProject();

    //item
    @GET("project/list/{pageIndex}/json")
    Observable<ProjectItem> getProjectItem(@Path("pageIndex") int pageIndex, @Query("cid") int cid);
}
