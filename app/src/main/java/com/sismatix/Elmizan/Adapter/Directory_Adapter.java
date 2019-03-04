package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Fregment.Premimum_Lawyer_freg;
import com.sismatix.Elmizan.Model.Directory_Model;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.R;

import java.util.List;

public class Directory_Adapter extends RecyclerView.Adapter<Directory_Adapter.MyViewHolder> {
    private Context context;
    private List<Directory_Model> models;


    public Directory_Adapter(Context context, List<Directory_Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public Directory_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_row, parent, false);

        return new Directory_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Directory_Adapter.MyViewHolder holder, final int position) {
        final Directory_Model product_model = models.get(position);
           /* holder.tv_date_news.setText(product_model.getNews_date());
            holder.tv_title_news.setText(product_model.getNews_title());
            holder.tv_detail_news.setText(product_model.getNews_detail());
*/
        //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));

            holder.lv_directory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                        /*Bundle b=new Bundle();
                        b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        *///Log.e("categotyidd",""+product_model.getValue());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Premimum_Lawyer_freg();
                            //myFragment.setArguments(b);
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

        TextView tv_date_news, tv_title_news, tv_detail_news;
        LinearLayout lv_directory;

        public MyViewHolder(View view) {
            super(view);
            lv_directory = (LinearLayout) view.findViewById(R.id.lv_directory);

              /*  tv_title_news = (TextView) view.findViewById(R.id.tv_title_news);
                tv_detail_news = (TextView) view.findViewById(R.id.tv_detail_news);
                tv_date_news = (TextView) view.findViewById(R.id.tv_date_news);
*/


        }
    }


}


