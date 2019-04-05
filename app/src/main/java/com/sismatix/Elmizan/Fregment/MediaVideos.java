package com.sismatix.Elmizan.Fregment;


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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaVideos extends Fragment {


    RecyclerView recycler_media_videos;
    private List<Media_Video_Model> media_video_models = new ArrayList<Media_Video_Model>();
    private Media_Video_Adapter media_video_adapter;

    public MediaVideos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_videos, container, false);

        AllocateMemory(v);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_GET_MEDIAVIDEOS_API();


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

    private void CALL_GET_MEDIAVIDEOS_API() {

        media_video_models.clear();
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylistt = apii.getMedia(Media.u_id);
        Log.e("uid", "" + Media.u_id);


        categorylistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                media_video_models.clear();

                JSONObject jsonObject1 = null;
                try {
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
                                    video = vidss_array.getString(0);
                                    Log.e("video_prem", "" + video);

                                    if (video == "" || video == null || video == "null" || video.equalsIgnoreCase(null)
                                            || video.equalsIgnoreCase("null")) {

                                        Toast.makeText(getActivity(), "data problem", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Log.e("sizeeee",""+media_video_models.size());
                                        media_video_models.add(new Media_Video_Model(video));
                                        /*slidervideo_models.add(new slidervideo_model(video));
                                        mPager.setAdapter(new SlidingVideo_Adapter(getActivity(), slidervideo_models));*/

                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    media_video_adapter.notifyItemChanged(j);
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AllocateMemory(View v) {
        recycler_media_videos = (RecyclerView) v.findViewById(R.id.recycler_media_videos);
    }

}
