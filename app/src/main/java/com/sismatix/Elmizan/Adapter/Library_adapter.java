package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.Elmizan.Model.Library_model;
import com.sismatix.Elmizan.Model.Video_Model;
import com.sismatix.Elmizan.R;

import java.util.List;


    public class Library_adapter extends RecyclerView.Adapter<Library_adapter.MyViewHolder> {
        private Context context;
        private List<Library_model> models;


        public Library_adapter(Context context, List<Library_model> models) {
            this.context = context;
            this.models = models;
        }

        @Override
        public Library_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.library_row, parent, false);

            return new Library_adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Library_adapter.MyViewHolder holder, final int position) {
            final Library_model product_model = models.get(position);
           /* holder.tv_date_news.setText(product_model.getNews_date());
            holder.tv_title_news.setText(product_model.getNews_title());
            holder.tv_detail_news.setText(product_model.getNews_detail());
*/
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));

            /*holder.lv_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                       *//* Bundle b=new Bundle();
                        b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        Log.e("categotyidd",""+product_model.getValue());
                     *//*   AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new News_Detail_freg();
                            //myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();
                        }
                    }, 1000);
                }
            });*/


        }


        @Override
        public int getItemCount() {
            return models.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_date_news, tv_title_news, tv_detail_news;
            LinearLayout lv_news;

            public MyViewHolder(View view) {
                super(view);
              /*  tv_title_news = (TextView) view.findViewById(R.id.tv_title_news);
                tv_detail_news = (TextView) view.findViewById(R.id.tv_detail_news);
                tv_date_news = (TextView) view.findViewById(R.id.tv_date_news);
                lv_news = (LinearLayout) view.findViewById(R.id.lv_news);
*/


            }
        }


    }






