package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Articles_Adapter extends RecyclerView.Adapter<Articles_Adapter.MyViewHolder> {
    private Context context;
    private List<Article_model> models;
    String video_id, spl, youtubeUrl;

    public Articles_Adapter(Context context, List<Article_model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public Articles_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articles_row, parent, false);

        return new Articles_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Articles_Adapter.MyViewHolder holder, final int position) {
        final Article_model article_model = models.get(position);
        holder.tv_article_description.setTypeface(Navigation_activity.typeface);
        holder.tv_article_title.setTypeface(Navigation_activity.typeface);
        holder.tv_article_date.setTypeface(Navigation_activity.typeface);
        holder.tv_more_article.setTypeface(Navigation_activity.typeface);

        holder.tv_more_article.setText("["+ context.getResources().getString(R.string.more) +"]");

        youtubeUrl = article_model.getVideo();
        Log.e("yid", "" + youtubeUrl);

//Youtube(holder);
        String tst = getYoutubeID(youtubeUrl);

        Log.e("videooooiddd", "" + tst);

        Glide.with(context)
                .load("http://img.youtube.com/vi/" + tst + "/mqdefault.jpg")
                .into(holder.iv_thumb);

        Navigation_activity.Check_String_NULL_Value(holder.tv_article_description, article_model.getArticle_description());
        Navigation_activity.Check_String_NULL_Value(holder.tv_article_title, article_model.getArticle_title());
        Navigation_activity.Check_String_NULL_Value(holder.tv_article_date, article_model.getArticle_date());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.app_icon);
        requestOptions.error(R.drawable.app_icon);

        holder.lv_article_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Bundle b = new Bundle();
                        b.putString("article_id", article_model.getArticle_id());
// b.putString("name",product_model.getCategory_name());
                        Log.e("article_id", "" + article_model.getArticle_id());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out);
                        Fragment newFragment = new News_Detail_freg();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack("Newsdetail").commit();
                        newFragment.setArguments(b);
// Start the animated transition.
                        ft.commit();
/*Fragment myFragment = new News_Detail_freg();
myFragment.setArguments(b);
activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();*/
                    }
                }, 1000);
            }
        });

        holder.tv_more_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Bundle b = new Bundle();
                        b.putString("article_id", article_model.getArticle_id());
// b.putString("name",product_model.getCategory_name());
                        Log.e("article_id", "" + article_model.getArticle_id());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out);
                        Fragment newFragment = new News_Detail_freg();
                        newFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack("Newsdetail").commit();
// Start the animated transition.
                        ft.commit();

/*Fragment myFragment = new News_Detail_freg();
myFragment.setArguments(b);
activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();*/
                    }
                }, 1000);

            }
        });

//Youtube(holder);

        if (article_model.getImage().equalsIgnoreCase("") || article_model.getImage().equalsIgnoreCase("null")) {
            holder.lv_image.setVisibility(View.GONE);
            holder.lv_thumbnail.setVisibility(View.VISIBLE);
//Youtube(holder);
        } else {
            holder.iv_article_image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(article_model.getImage()).into(holder.iv_article_image);
        }

        if (article_model.getVideo().equalsIgnoreCase("") || article_model.getVideo().equalsIgnoreCase("null")) {
            holder.lv_image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(article_model.getImage()).into(holder.iv_article_image);
            holder.lv_thumbnail.setVisibility(View.GONE);
        } else {
            holder.lv_thumbnail.setVisibility(View.VISIBLE);
//Youtube(holder);
        }

/* if (article_model.getImage().equalsIgnoreCase("")||article_model.getImage().equalsIgnoreCase("null")){
holder.lv_image.setVisibility(View.GONE);
holder.lv_video.setVisibility(View.VISIBLE);
//Youtube(holder);
}else {
holder.iv_article_image.setVisibility(View.VISIBLE);
Glide.with(context)
.setDefaultRequestOptions(requestOptions)
.load(article_model.getImage()).into(holder.iv_article_image);
}

if (article_model.getVideo().equalsIgnoreCase("")||article_model.getVideo().equalsIgnoreCase("null")){
holder.lv_image.setVisibility(View.VISIBLE);
Glide.with(context)
.setDefaultRequestOptions(requestOptions)
.load(article_model.getImage()).into(holder.iv_article_image);
holder.lv_video.setVisibility(View.GONE);
}else {
holder.lv_video.setVisibility(View.VISIBLE);
Youtube(holder);
}*/

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

        TextView tv_article_description, tv_article_title, tv_more_article, tv_article_date;
        LinearLayout lv_article_click, lv_image, lv_thumbnail;
        ImageView iv_article_image, iv_thumb;
        VideoView videoView;
        private YouTubePlayer YPlayer;
        View view;

        public MyViewHolder(View view) {
            super(view);
            tv_article_description = (TextView) view.findViewById(R.id.tv_article_description);
            tv_article_title = (TextView) view.findViewById(R.id.tv_article_title);
            tv_article_date = (TextView) view.findViewById(R.id.tv_article_date);
            tv_more_article = (TextView) view.findViewById(R.id.tv_more_article);
            lv_article_click = (LinearLayout) view.findViewById(R.id.lv_article_click);
            lv_thumbnail = (LinearLayout) view.findViewById(R.id.lv_thumbnailll);
            lv_image = (LinearLayout) view.findViewById(R.id.lv_image);
            iv_article_image = (ImageView) view.findViewById(R.id.iv_article_image);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
//videoView = (VideoView) view.findViewById(R.id.videoView);
        }
    }


}





