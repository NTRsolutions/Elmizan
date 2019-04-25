package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.Media_Video_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Media_Video_Model;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Videos_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaVideos extends Fragment {


    public static RecyclerView recycler_media_videos;
    public static List<Media_Video_Model> media_video_models = new ArrayList<Media_Video_Model>();
    public static Media_Video_Adapter media_video_adapter;
    public static String old_video;
    public static Context context = null;
    public static ProgressBar progressBar_video;
    public static TextView tv_media_not_found;


    public MediaVideos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_videos, container, false);

        AllocateMemory(v);
        context = getActivity();
        lang_arbi();
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_GET_MEDIAVIDEOS_API(Media.u_id);
            } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        /*premium_lawyer_adapter = new Premium_Lawyer_adapter(getActivity(), premium_lawyer_models);
        recycler_media_videos.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recycler_media_videos.setItemAnimator(new DefaultItemAnimator());
        recycler_media_videos.setAdapter(premium_lawyer_adapter);*/

        /*articles_adapter = new Premium_article_adpter(getActivity(), article_models);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_media_videos.setLayoutManager(mLayoutManager);
        recycler_media_videos.setItemAnimator(new DefaultItemAnimator());
        recycler_media_videos.setAdapter(articles_adapter);*/

        media_video_adapter = new Media_Video_Adapter(getActivity(), media_video_models);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_media_videos.setLayoutManager(mLayoutManager1);
        recycler_media_videos.setItemAnimator(new DefaultItemAnimator());
        recycler_media_videos.setAdapter(media_video_adapter);


        return v;
    }

    public static void CALL_GET_MEDIAVIDEOS_API(String user_id) {
        progressBar_video.setVisibility(View.VISIBLE);
        media_video_models.clear();
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylistt = apii.getMedia(user_id);
        Log.e("uid", "" + Media.u_id);


        categorylistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                media_video_models.clear();

                JSONObject jsonObject1 = null;
                try {
                    progressBar_video.setVisibility(View.GONE);
                    jsonObject1 = new JSONObject(response.body().string());
                    String status = jsonObject1.getString("status");
                    Log.e("status_media_vids", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String msg_en = jsonObject1.getString("data");
                        Log.e("data_vids", "" + msg_en);

                        JSONObject data_obj1 = jsonObject1.getJSONObject("data");
                        Log.e("media_data", "" + data_obj1);

                        String user_media_status = data_obj1.getString("user_media_status");
                        Log.e("user_media_status_med", "" + user_media_status);


                        ///////////////////////////////////////////////////////////
                        ///get video....
                        JSONObject media_obj = data_obj1.getJSONObject("arr_user_media_content");
                        Log.e("media_obj", "" + media_obj);

                        JSONArray jsonArray_video = media_obj.getJSONArray("video");
                        Log.e("json_video_168", "" + jsonArray_video);

                        if (jsonArray_video.equals("[]") == true) {
                            Log.e("jsonarray_blanck", "" + jsonArray_video);
                            recycler_media_videos.setVisibility(View.GONE);
                            tv_media_not_found.setVisibility(View.VISIBLE);
                            tv_media_not_found.setText(context.getResources().getString(R.string.data_not_found));


                        } else {
                            for (int j = 0; j < jsonArray_video.length(); j++) {
                                try {
                                    String video = jsonArray_video.getString(j);

                                    if (video.equalsIgnoreCase("") == true || video.equalsIgnoreCase("null") == true
                                            || video == null) {
                                        Log.e("image_null", "" + video);
                                    } else {
                                        old_video = jsonArray_video.getString(j);
                                        Log.e("old_video_183", "" + old_video);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }

                            }
                        }


                        //////////////////////////////////////////////////////////////////////////




                        String article_media_url = data_obj1.getString("arr_user_media_content_url");
                        Log.e("media_image", "" + article_media_url);

                        JSONObject vid_obj = data_obj1.getJSONObject("arr_user_media_content_url");
                        Log.e("imgg", "" + vid_obj);

                        String video = vid_obj.getString("video");
                        Log.e("videoooo", "" + video);

                        JSONArray vidss_array = vid_obj.getJSONArray("video");

                        if (vidss_array != null && vidss_array.isNull(0) != true) {
                            Log.e("vid_array", "" + vidss_array);

                            for (int j = 0; j < vidss_array.length(); j++) {
                                try {

                                    JSONObject object = vidss_array.getJSONObject(j);
                                    Log.e("url_187", "" + object.getString("url"));
                                    Log.e("object", "" + object);
                                    media_video_models.add(new Media_Video_Model(object.getString("url"), old_video));

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    media_video_adapter.notifyItemChanged(j);
                                }
                            }
                        } else {
                            recycler_media_videos.setVisibility(View.GONE);
                            tv_media_not_found.setVisibility(View.VISIBLE);
                            tv_media_not_found.setText(context.getResources().getString(R.string.data_not_found));

                        }
                    } else if (status.equalsIgnoreCase("error")) {

                        progressBar_video.setVisibility(View.GONE);
                        recycler_media_videos.setVisibility(View.GONE);
                        tv_media_not_found.setVisibility(View.VISIBLE);
                        tv_media_not_found.setText(context.getResources().getString(R.string.data_not_found));

                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private void AllocateMemory(View v) {
        recycler_media_videos = (RecyclerView) v.findViewById(R.id.recycler_media_videos);
        tv_media_not_found = (TextView) v.findViewById(R.id.tv_media_not_found);
        progressBar_video = (ProgressBar) v.findViewById(R.id.progressBar_video);
    }

}
