package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.MediaImageSlider;
import com.sismatix.Elmizan.Activity.YPlayer;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.R;

import java.util.List;

public class Premium_Lawyer_adapter extends RecyclerView.Adapter<Premium_Lawyer_adapter.MyViewHolder> {
    private Context context;
    public static List<Premium_Lawyer_Model> models;

    public Premium_Lawyer_adapter(Context context, List<Premium_Lawyer_Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.premium_lawyer_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Premium_Lawyer_Model product_model = models.get(position);

        Glide.with(context).load(product_model.getLawyer_image()).into(holder.imageview);
        holder.lv_prem_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position_pass= String.valueOf(position);
                Toast.makeText(context, position_pass+" =>>"+product_model.getLawyer_image(), Toast.LENGTH_SHORT).show();
                //AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(context, MediaImageSlider.class);
                intent.putExtra("pos", position);
                context.startActivity(intent);
                //activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView tv_category_name ;
        LinearLayout lv_prem_images;
        ImageView imageview;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView)view.findViewById(R.id.imageview);
            lv_prem_images = (LinearLayout) view.findViewById(R.id.lv_prem_images);
        }
    }
}