package com.sismatix.Elmizan.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<ResponseBody> get_news_detail(@Field("news_id") String news_id,
                                       @Field("user_id") String user_id);

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
                                        @Query("article_status[]") String article_status,
                                        @Query("inserted_by") String inserted_by);

    //get article detail
    //https://elmizan.demoproject.info/api/article_details.php
    @POST("article_details.php")
    @FormUrlEncoded
    Call<ResponseBody> get_article_detail(@Field("article_id") String Article_id,
                                          @Field("user_id") String user_id);


    //Article add comment
    //https://elmizan.demoproject.info/api/article_comment_add.php
    @POST("article_comment_add.php")
    @FormUrlEncoded
    Call<ResponseBody> get_article_add_comment(@Field("article_id") String article_id,
                                               @Field("user_id") String user_id,
                                               @Field("article_comment_content") String article_comment_content);

    //article like api
    //https://elmizan.demoproject.info/api/article_like_add.php
    @POST("article_like_add.php")
    @FormUrlEncoded
    Call<ResponseBody> get_article_like(@Field("article_id") String article_id,
                                        @Field("user_id") String user_id);


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
    Call<ResponseBody> get_register(@Field("user_fullname") String user_name,
                                    @Field("user_email") String user_email,
                                    @Field("user_phone") String user_phone,
                                    @Field("user_password") String user_password,
                                    @Field("confirm_password") String confirm_password,
                                    @Field("agree_terms") String agree_terms,
                                    @Field("register_as_lawyer") String register_as_lawyer);


    //User List
    //https://elmizan.demoproject.info/api/users_list.php

    @GET("users_list.php")
    Call<ResponseBody> get_User_list(@Query("page") String page,
                                     @Query("per_page") String per_page,
                                     @Query("user_type[]") String user_type,
                                     @Query("user_status[]") String user_status,
                                     @Query("search") String search);

    //Get User Detail
    //https://elmizan.demoproject.info/api/user_details.php
    @POST("user_details.php")
    @FormUrlEncoded
    Call<ResponseBody> get_user_detail(@Field("user_id") String user_id);

    //Get country list
    //https://elmizan.demoproject.info/api/country_list.php
    @GET("country_list.php")
    Call<ResponseBody> get_country_list();

    //Add article
    //article_update.php
    //upload image
    @Multipart
    @POST("article_update.php")
    Call<ResponseBody> add_article_image(
            @Part("inserted_by") RequestBody inserted_by,
            @Part("article_id") RequestBody article_id,
            @Part("old_article_images[]") RequestBody old_article_images,
            @Part("article_title") RequestBody article_title,
            @Part("article_description") RequestBody article_description,
            @Part("article_status") RequestBody article_status,
            @Part MultipartBody.Part file);

    ////image blank
    @Multipart
    @POST("article_update.php")
    Call<ResponseBody> add_article_image_blank(
            @Part("inserted_by") RequestBody inserted_by,
            @Part("article_id") RequestBody article_id,
            @Part("old_article_images[]") RequestBody old_article_images,
            @Part("article_title") RequestBody article_title,
            @Part("article_description") RequestBody article_description,
            @Part("article_status") RequestBody article_status,
            @Part("article_images[]") RequestBody article_images
          );


    //Add article
    //article_update.php
    //upload youtube link
    @Multipart
    @POST("article_update.php")
    Call<ResponseBody> add_article_youtube_link(
            @Part("inserted_by") RequestBody inserted_by,
            @Part("video_url[]") RequestBody video_url,
            @Part("article_id") RequestBody article_id,
            @Part("old_article_images[]") RequestBody old_article_images,
            @Part("article_title") RequestBody article_title,
            @Part("article_description") RequestBody article_description,
            @Part("article_status") RequestBody article_status);


}
