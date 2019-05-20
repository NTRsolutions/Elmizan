package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.Elmizan.Activity.YPlayer;
import com.sismatix.Elmizan.Configgg;
import com.sismatix.Elmizan.Fregment.Login_freg;
import com.sismatix.Elmizan.Fregment.MediaVideos;
import com.sismatix.Elmizan.Model.Media_Video_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Media_Video_Adapter extends RecyclerView.Adapter<Media_Video_Adapter.MyViewHolder> {
    private Context context;
    private List<Media_Video_Model> models;
    String video_id, spl, youtubeUrl;
    String title;

    public Media_Video_Adapter(Context context, List<Media_Video_Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_video_row_thumbnails, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Media_Video_Model media_video_model = models.get(position);

        youtubeUrl = media_video_model.getVideo();
        Log.e("yid_med", "" + youtubeUrl);

        String tst1 = getYoutubeID(youtubeUrl);

        getVideoInfo(video_id,holder);

        Log.e("videooooiddd_med", "" + tst1);
        Log.e("idddd_82", "" + media_video_model.getVideo());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load("http://img.youtube.com/vi/" + tst1 + "/hqdefault.jpg")
                .into(holder.iv_thumb);

        holder.lv_media_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AppCompatActivity activity = (AppCompatActivity) v.getContext();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        String video_id_pass = getYoutubeID(media_video_model.getVideo());

                        Log.e("video_id_pass",""+video_id_pass);
                       // Toast.makeText(context, video_id_pass, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, YPlayer.class);
                        intent.putExtra("videoId", video_id_pass);
                        activity.startActivity(intent);
                       // activity.finish();

                    }
                }, 1000);
            }
        });


        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {

            if (My_Preference.get_premium_lawyer(context).equals("premium") == true) {
                String user_id=Login_preference.getuser_id(context);

                //  Toast.makeText(getActivity(), article_id+"delete article"+user_id, Toast.LENGTH_SHORT).show();

                holder.delete_video.setVisibility(View.VISIBLE);
            }else {
                holder.delete_video.setVisibility(View.GONE);
            }
        } else {
            holder.delete_video.setVisibility(View.GONE);
        }

        holder.delete_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_video=media_video_model.getVideo();
                Log.e("old_video_80", "" + old_video);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {

                    if (My_Preference.get_premium_lawyer(context).equals("premium") == true) {
                        String user_id=Login_preference.getuser_id(context);

                        //  Toast.makeText(getActivity(), article_id+"delete article"+user_id, Toast.LENGTH_SHORT).show();

                        Call_Delete_Video_api(old_video,user_id,activity);
                    }
                } else {

                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out);
                    Fragment newFragment = new Login_freg();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack(null).commit();

                }

                }
        });


    }


    private void Call_Delete_Video_api(String old_video, final String user_id, final AppCompatActivity activity) {

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> register = apii.Call_delete_api(user_id,"",old_video);
        Log.e("old_video_171", "" + old_video);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("response_deletevideo", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    Log.e("jsonObject_delete_173", "" + jsonObject);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {

                        MediaVideos.CALL_GET_MEDIAVIDEOS_API(user_id);
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();



                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVideoInfo(String video_id, final MyViewHolder holder) {
        Log.e("vididddd",""+video_id);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://www.googleapis.com/youtube/v3/videos?id=" + video_id+ "&key=" +
                        Configgg.YOUTUBE_API_KEY +
                        "&part=snippet,contentDetails,statistics,status",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            JSONObject object = jsonArray.getJSONObject(0);
                            JSONObject snippet = object.getJSONObject("snippet");

                            title = snippet.getString("title");
                            Log.e("vid_title",""+title);

                            holder.vid_title.setText(title);

                            Log.d("stuff: ", "" + title);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(getActivity(), "not get Response"+error, Toast.LENGTH_SHORT).show();
                        Log.e("error",""+error);
                    }

                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        // Request (if not using Singleton [RequestHandler]
        // RequestQueue requestQueue = Volley.newRequestQueue(this);
        // requestQueue.add(stringRequest);

        // Request with RequestHandler (Singleton: if created)


    }

    private String getYoutubeID(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = youtubeUrl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (youtubeUrl.contains("youtu.be/")) {
                spl = youtubeUrl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                } else {
                    video_id = spl;
                }
            }
        }
        Log.e("vidid", "" + video_id);
        return video_id;
    }



    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView  iv_thumb,delete_video;
        TextView vid_title;
        LinearLayout lv_media_videos;
        View view;

        public MyViewHolder(View view) {
            super(view);

            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumbnail);
            delete_video = (ImageView) view.findViewById(R.id.delete_video);
            vid_title = (TextView) view.findViewById(R.id.vid_title);
            lv_media_videos = (LinearLayout)view.findViewById(R.id.lv_media_videos);

        }
    }


}
