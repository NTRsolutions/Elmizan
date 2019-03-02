package com.sismatix.Elmizan.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //news list
    //https://elmizan.demoproject.info/api/news_list.php

    @GET("news_list.php")
    Call<ResponseBody> get_News_list();

}
