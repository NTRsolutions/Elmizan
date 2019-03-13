package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.Model.Video_Model;
import com.sismatix.Elmizan.R;

import java.util.List;


public class Articles_Adapter extends RecyclerView.Adapter<Articles_Adapter.MyViewHolder> {
    private Context context;
    private List<Article_model> models;


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

        Navigation_activity.Check_String_NULL_Value(holder.tv_article_description,article_model.getArticle_description());
        Navigation_activity.Check_String_NULL_Value(holder.tv_article_title,article_model.getArticle_title());

       // holder.tv_article_description.setText(article_model.getArticle_description());
            //holder.tv_article_title.setText(article_model.getArticle_title());
          //  holder.tv_article_date.setText(article_model.getArticle_date());
        Glide.with(context).load(article_model.getImage()).into(holder.iv_article_image);

        //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            holder.lv_article_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Bundle b=new Bundle();
                            b.putString("article_id",article_model.getArticle_id());
                            // b.putString("name",product_model.getCategory_name());
                            Log.e("article_id",""+article_model.getArticle_id());
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new News_Detail_freg();
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();
                        }
                    }, 1000);
                }
            });


    }


    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_article_description, tv_article_title, tv_article_date;
        LinearLayout lv_article_click;
        ImageView iv_article_image;

        public MyViewHolder(View view) {
            super(view);
            tv_article_description = (TextView) view.findViewById(R.id.tv_article_description);
            tv_article_title = (TextView) view.findViewById(R.id.tv_article_title);
            tv_article_date = (TextView) view.findViewById(R.id.tv_article_date);
            lv_article_click = (LinearLayout) view.findViewById(R.id.lv_article_click);
            iv_article_image = (ImageView) view.findViewById(R.id.iv_article_image);


        }
    }


}






