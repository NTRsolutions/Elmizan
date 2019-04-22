package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.net.Uri;
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
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.FileUtils;
import com.sismatix.Elmizan.Fregment.Directory_freg;
import com.sismatix.Elmizan.Model.Country_model;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;

import java.util.ArrayList;
import java.util.List;


public class Image_adapter extends RecyclerView.Adapter<Image_adapter.MyViewHolder> {

    ArrayList<Uri> arrayList;
    private Context context;
    LayoutInflater inflater;
    public static String name, image;


    public Image_adapter(Context context, ArrayList<Uri> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Image_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_items, parent, false);
        return new Image_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Image_adapter.MyViewHolder holder, final int position) {

        //final Country_model rmd = model.get(position);
        holder.imagePath.setText(FileUtils.getPath(context, arrayList.get(position)));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(arrayList.get(position))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView imagePath;
        public ImageView imageView;
        LinearLayout lv_click;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            imagePath = itemView.findViewById(R.id.imagePath);


        }
    }
}




