package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sismatix.Elmizan.Configgg;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Videos_Model;
import com.sismatix.Elmizan.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Premium_Lawyer_Video_adapter extends RecyclerView.Adapter<Premium_Lawyer_Video_adapter.MyViewHolder> {
    private Context context;
    private List<Premium_Lawyer_Videos_Model> models;
    String video_idd, spll, viddiidd;

    public Premium_Lawyer_Video_adapter(Context context, List<Premium_Lawyer_Videos_Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.premium_video_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Premium_Lawyer_Videos_Model premvid_model = models.get(position);

        viddiidd = premvid_model.getVideo();

        Log.e("vidid", "" + viddiidd);

        Youtube(viddiidd, holder);
        getYoutubeVideoID(viddiidd);

        //Glide.with(context).load(product_model.getLawyer_image()).into(holder.imageview);

    }

    private void Youtube(final String video, final MyViewHolder holder) {

        Log.e("vidssssss", "" + video);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment_prem, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    holder.YPlayerr = youTubePlayer;
                    holder.YPlayerr.setFullscreen(false);
                    holder.YPlayerr.loadVideo(video_idd);
                    holder.YPlayerr.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            holder.YPlayerr.pause();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                    holder.YPlayerr.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    holder.YPlayerr.setShowFullscreenButton(true);
                    holder.YPlayerr.getCurrentTimeMillis();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(activity, "Video Problemmmmm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getYoutubeVideoID(String video) {
        if (TextUtils.isEmpty(video)) {
            return "";
        }
        video_idd = "";

        String expression1 = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = video;
        Pattern pattern = Pattern.compile(expression1, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex2 = matcher.group(7);
            if (groupIndex2 != null && groupIndex2.length() == 11)
                video_idd = groupIndex2;
        }
        if (TextUtils.isEmpty(video_idd)) {
            if (video.contains("youtu.be/")) {
                spll = video.split("youtu.be/")[1];
                if (spll.contains("\\?")) {
                    video_idd = spll.split("\\?")[0];
                } else {
                    video_idd = spll;
                }
            }
        }
        Log.e("vidid_prem", "" + video_idd);
        return video_idd;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static YouTubePlayer YPlayerr;

        public MyViewHolder(View view) {
            super(view);
        }
    }
}