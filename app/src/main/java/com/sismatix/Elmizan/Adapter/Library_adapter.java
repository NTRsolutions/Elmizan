package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.Elmizan.Activity.Navigation_activity;
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
            holder.tv_library_title.setTypeface(Navigation_activity.typeface);
            holder.tv_library_link.setTypeface(Navigation_activity.typeface);
            holder.tv_library_date.setTypeface(Navigation_activity.typeface);
            holder.tv_library_desc.setTypeface(Navigation_activity.typeface);
            holder.tv_more.setTypeface(Navigation_activity.typeface);

            Navigation_activity.Check_String_NULL_Value(holder.tv_library_title,product_model.getLibrary_title());
            Navigation_activity.Check_String_NULL_Value(holder.tv_library_link,product_model.getLibrary_link());
            Navigation_activity.Check_String_NULL_Value(holder.tv_library_desc,product_model.getLibrary_description());


            holder.tv_library_date.setText(product_model.getLibrary_date());
           // holder.tv_library_link.setText(product_model.getLibrary_link());
          //  holder.tv_library_title.setText(product_model.getLibrary_title());


            if (product_model.getLibrary_link() == "" || product_model.getLibrary_link() == null || product_model.getLibrary_link() == "null" || product_model.getLibrary_link().equalsIgnoreCase(null)
                    || product_model.getLibrary_link().equalsIgnoreCase("null") || product_model.getLibrary_link().equalsIgnoreCase("")) {

                holder.tv_more.setVisibility(View.GONE);
            }else {

               holder.tv_more.setVisibility(View.VISIBLE);
                holder.tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product_model.getLibrary_link()));
                        context.startActivity(browserIntent);
                    }
                });
            }



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

            TextView tv_library_date, tv_library_title, tv_library_link,tv_more,tv_library_desc;
            LinearLayout lv_library;

            public MyViewHolder(View view) {
                super(view);
                tv_library_date = (TextView) view.findViewById(R.id.tv_library_date);
                tv_library_title = (TextView) view.findViewById(R.id.tv_library_title);
                tv_library_link = (TextView) view.findViewById(R.id.tv_library_link);
                tv_more = (TextView) view.findViewById(R.id.tv_more);
                lv_library = (LinearLayout) view.findViewById(R.id.lv_library);
                tv_library_desc = (TextView) view.findViewById(R.id.tv_library_desc);


            }
        }


    }






