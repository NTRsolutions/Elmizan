package com.sismatix.Elmizan.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    //https://elmizan.demoproject.info/api/user_login.php
    @POST("user_login.php")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("user_name") String user_name,
                             @Field("user_password") String user_password);

//news list
//https://elmizan.demoproject.info/api/news_list.php

    @GET("news_list.php")
    Call<ResponseBody> get_News_list(@Query("page") String page,
                                     @Query("per_page") String per_page,
                                     @Query("news_status[]") String news_status);

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
    Call<ResponseBody> get_article_list(@Query("page") String page,
                                        @Query("per_page") String per_page,
                                        @Query("article_status[]") String article_status
                                        );

    //category list
//https://elmizan.demoproject.info/api/category_list.php
    @GET("category_list.php")
    Call<ResponseBody> get_category_list();

    //library list
//https://elmizan.demoproject.info/api/library_list.php?page=1&per_page=10&library_status[]=1&category_id[]=1
//prameter pass =>category_id[],page,per_page
    @GET("library_list.php")
    Call<ResponseBody> get_library_list(@Query("page") String page,
                                        @Query("per_page") String per_page,
                                        @Query("category_id[]") String category_id,
                                        @Query("library_status[]") String library_status);

//Register api
//https://elmizan.demoproject.info/api/user_registration.php
    @POST("user_registration.php")
    @FormUrlEncoded
    Call<ResponseBody> get_register(@Field("user_name") String user_name,
                                        @Field("user_email") String user_email,
                                        @Field("user_phone") String user_phone,
                                        @Field("user_password") String user_password,
                                        @Field("confirm_password") String confirm_password,
                                        @Field("agree_terms") String agree_terms );



    //User List
    //https://elmizan.demoproject.info/api/users_list.php

    @GET("users_list.php")
    Call<ResponseBody> get_User_list(@Query("page") String page,
                                     @Query("per_page") String per_page,
                                     @Query("user_type[]") String user_type,
                                     @Query("user_status[]") String user_status);
    //Get User Detail
    //https://elmizan.demoproject.info/api/user_details.php
    @POST("user_details.php")
    @FormUrlEncoded
    Call<ResponseBody> get_user_detail(@Field("user_id") String user_id
                                     );



}
