package com.sismatix.Elmizan.Retrofit;

import java.util.List;

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

    //article delete api
    //https://elmizan.demoproject.info/api/article_delete.php
    @POST("article_delete.php")
    @FormUrlEncoded
    Call<ResponseBody> article_delete(@Field("article_id") String Article_id,
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
                                    @Field("register_as_lawyer") String register_as_lawyer,
                                    @Field("user_country_id") String user_country_id);


    //User List
    //https://elmizan.demoproject.info/api/users_list.php

    @GET("users_list.php")
    Call<ResponseBody> get_User_list(@Query("page") String page,
                                     @Query("per_page") String per_page,
                                     @Query("user_type[]") String user_type,
                                     @Query("user_status[]") String user_status,
                                     @Query("search") String search,
                                     @Query("country") String country);

    //Get User Detail
    //https://elmizan.demoproject.info/api/user_details.php
    @POST("user_details.php")
    @FormUrlEncoded
    Call<ResponseBody> get_user_detail(@Field("user_id") String user_id);

    //Get country list
    //https://elmizan.demoproject.info/api/country_list.php
    @GET("country_list.php")
    Call<ResponseBody> get_country_list(@Query("country_status[]") String country_status);

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






    //update  premium lawyer profile
    //user_update.php

    @Multipart
    @POST("user_update.php")
    Call<ResponseBody> CALL_User_update(
            @Part("user_id") RequestBody user_id,
            @Part("user_fullname") RequestBody user_fullname,
            @Part("user_description") RequestBody user_description,
            @Part("user_about") RequestBody user_about,
            @Part("user_email") RequestBody user_email,
            @Part("user_phone") RequestBody user_phone,
            @Part("user_address") RequestBody user_address,
            @Part("user_website") RequestBody user_website,
            @Part("user_country_id") RequestBody user_country_id,
            @Part("user_instagram") RequestBody user_instagram,
            @Part("user_twitter") RequestBody user_twitter,
            @Part("user_facebook") RequestBody user_facebook,
            @Part("old_user_images") RequestBody old_user_images,
            @Part MultipartBody.Part file
            );


    //update  premium lawyer profile blank
    //user_update.php

    @Multipart
    @POST("user_update.php")
    Call<ResponseBody> CALL_User_update_img_blank(
            @Part("user_id") RequestBody user_id,
            @Part("user_fullname") RequestBody user_fullname,
            @Part("user_description") RequestBody user_description,
            @Part("user_about") RequestBody user_about,
            @Part("user_email") RequestBody user_email,
            @Part("user_phone") RequestBody user_phone,
            @Part("user_address") RequestBody user_address,
            @Part("user_website") RequestBody user_website,
            @Part("user_country_id") RequestBody user_country_id,
            @Part("user_instagram") RequestBody user_instagram,
            @Part("user_twitter") RequestBody user_twitter,
            @Part("user_facebook") RequestBody user_facebook,
            @Part("old_user_images") RequestBody old_user_images,
            @Part("user_avatar") RequestBody user_avatar

    );

    //upload photos and media
    @Multipart
    @POST("user_media_update.php")
    Call<ResponseBody> uploadMultiple_files(
            @Part("video_url[]") List<RequestBody> video_url,
            @Part("inserted_by") RequestBody inserted_by,
            @Part("old_user_media_images[]") RequestBody old_user_media_images,
            @Part("user_id") RequestBody user_id,
            @Part List<MultipartBody.Part> files);


    @POST("user_login.php")
    @FormUrlEncoded
    Call<ResponseBody> google_login(@Field("google_id") String google_id,
                                    @Field("google_email") String google_email,
                                    @Field("google_user_fullname") String google_user_fullname,
                                    @Field("google_image") String google_image);

    @GET("about_us.php")
    Call<ResponseBody> get_about_us();

    @GET("contact_us.php")
    Call<ResponseBody> get_contact_us();

    @POST("user_media.php")
    @FormUrlEncoded
    Call<ResponseBody> getMedia(@Field("user_id") String user_id);


}
