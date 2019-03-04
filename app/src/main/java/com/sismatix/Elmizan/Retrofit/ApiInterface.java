package com.sismatix.Elmizan.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    //news list
    //https://elmizan.demoproject.info/api/news_list.php

    @GET("news_list.php")
    Call<ResponseBody> get_News_list();

    //news detail api
    //https://elmizan.demoproject.info/api/news_details.php
    //prameter pass =>news_id
    @POST("news_details.php")
    @FormUrlEncoded
    Call<ResponseBody> get_news_detail(@Field("news_id") String news_id);

    //Add Comment
    //https://elmizan.demoproject.info/api/news_comment_add.php
    //prameter pass =>news_id,user_id,news_comment_content
    @POST("news_comment_add.php")
    @FormUrlEncoded
    Call<ResponseBody> get_news_add_comment(@Field("news_id") String news_id,
                                            @Field("user_id") String user_id,
                                            @Field("news_comment_content") String news_comment_content);

    //News like
    //https://elmizan.demoproject.info/api/news_like_add.php
    //prameter pass =>news_id,user_id
    @POST("news_like_add.php")
    @FormUrlEncoded
    Call<ResponseBody> get_news_like(@Field("news_id") String news_id,
                                            @Field("user_id") String user_id);

    //article list
    //https://elmizan.demoproject.info/api/article_list.php

    @GET("article_list.php")
    Call<ResponseBody> get_article_list();

    //category list
    //https://elmizan.demoproject.info/api/category_list.php
    @GET("category_list.php")
    Call<ResponseBody> get_category_list();

    //library list
    //https://elmizan.demoproject.info/api/library_list.php?page=1&per_page=10&library_status[]=1&category_id[]=1
    //prameter pass =>category_id[],page,per_page
    @POST("library_list.php")
    @FormUrlEncoded
    Call<ResponseBody> get_library_list(@Field("page") String page,
                                     @Field("user_id") String per_page,
                                        @Field("category_id[]") String category_id );


}
