package com.sismatix.Elmizan.Adapter;

import android.content.Context;
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
import com.sismatix.Elmizan.Model.Country_model;
import com.sismatix.Elmizan.R;

import java.util.List;

    public class Country_Adapter extends RecyclerView.Adapter<Country_Adapter.MyViewHolder> {

        private List<Country_model> model;
        private Context context;
        LayoutInflater inflater;
        public static String name,image;



        public Country_Adapter(Context context, List<Country_model> model) {
            this.context = context;
            this.model = model;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.country_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            final Country_model rmd = model.get(position);
            Log.e("nmae_44",""+rmd.getCountry_name());
            holder.tv_country.setTypeface(Navigation_activity.typeface);
            Navigation_activity.Check_String_NULL_Value(holder.tv_country,rmd.getCountry_name());


          //  holder.tv_country.setText(rmd.getCountry_name());
            holder.lv_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name=rmd.getCountry_name();
                    image=rmd.getCountry_image_url();
                   // Picasso.with(context).load(rmd.getCountry_image_url()).into(Home.iv_currency);
                 //   Home.popup.dismiss();
                }
            });
            Glide.with(context).load(rmd.getCountry_image_url()).into(holder.ivflag);

        }

        @Override
        public int getItemCount() {
            return model.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_country;
            public ImageView ivflag;
            LinearLayout lv_click;

            public MyViewHolder(View itemView) {
                super(itemView);

                tv_country = (TextView) itemView.findViewById(R.id.tv_country);
                ivflag = (ImageView) itemView.findViewById(R.id.iv_flag);
                lv_click=(LinearLayout)itemView.findViewById(R.id.lv_click);

            }
        }
    }


