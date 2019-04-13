package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Home_News_Adapter extends RecyclerView.Adapter<Home_News_Adapter.MyViewHolder>
{
    private Context context;
    private List<News_Model> models;


    public Home_News_Adapter(Context context, List<News_Model> models) {
        this.context = context;
        this.models = models;
    }
    @Override
    public Home_News_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_news_row, parent, false);

        return new Home_News_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Home_News_Adapter.MyViewHolder holder, final int position) {
        final News_Model model = models.get(position);

        /*String dateee=model.getNews_date();
        DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = inputFormatter1.parse(dateee);
            Log.e("date_news",""+date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat outputFormatter1 = new SimpleDateFormat("dd MMM, yyyy");
        String output1 = outputFormatter1.format(date1);
        Log.e("date_news_61",""+output1);
*/
        holder.tv_date_news.setTypeface(Navigation_activity.typeface);
        holder.tv_title_news.setTypeface(Navigation_activity.typeface);
        holder.tv_detail_news.setTypeface(Navigation_activity.typeface);
        holder.tv_date_news.setTypeface(Navigation_activity.typeface);

        holder.tv_date_news.setText(Html.fromHtml(model.getNews_day()+" "+model.getNews_month()+", "+model.getNews_year()));
        Navigation_activity.Check_String_NULL_Value(holder.tv_title_news,model.getNews_title());
        Navigation_activity.Check_String_NULL_Value(holder.tv_detail_news,model.getNews_description());
        //Navigation_activity.Check_String_NULL_Value(holder.tv_date_news,model.getNews_date());
        //holder.tv_title_news.setText(Html.fromHtml(model.getNews_title()));
        //holder.tv_detail_news.setText(Html.fromHtml(model.getNews_description()));

        //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        if(position %2 == 1)
        {
            holder.lv_news.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }

        holder.lv_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Bundle b=new Bundle();
                        b.putString("news_id",model.getNews_id());
                       // b.putString("name",product_model.getCategory_name());
                        Log.e("news_id",""+model.getNews_id());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new News_Detail_freg();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack("Newsdetail").commit();
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

        TextView tv_date_news,tv_title_news, tv_detail_news;
        LinearLayout lv_news;

        public MyViewHolder(View view) {
            super(view);
            tv_title_news = (TextView) view.findViewById(R.id.tv_title_news);
            tv_detail_news = (TextView) view.findViewById(R.id.tv_detail_news);
            tv_date_news = (TextView) view.findViewById(R.id.tv_date_news);
            lv_news = (LinearLayout) view.findViewById(R.id.lv_news);





        }
    }


}
