package com.sismatix.Elmizan.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sismatix.Elmizan.Model.Media_images_model;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Sliding_media_img_Adapter extends PagerAdapter {
    Context context;
    private List<Media_images_model> sliderimage_models;
    private LayoutInflater inflater;
    String screen,old_image;

    public Sliding_media_img_Adapter(Context context, List<Media_images_model> sliderimage_models) {

        this.context = context;
        this.sliderimage_models = sliderimage_models;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return sliderimage_models.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {

        final ImageView image,delete_img_slide;
        final ProgressBar progress_slider;

        View view = null;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.media_sliding_row, container, false);
        final Media_images_model sm = sliderimage_models.get(pos);

        image = (ImageView) view.findViewById(R.id.image);
       // delete_img_slide = (ImageView) view.findViewById(R.id.delete_img_slide);
        progress_slider = (ProgressBar) view.findViewById(R.id.progress_slider);

       /* delete_img_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_image=sm.getOld_img();
                Log.e("old_img_62",""+old_image);
                Toast.makeText(context, old_image+"old_imag=>>"+old_image, Toast.LENGTH_SHORT).show();

            }
        });
        */


         Glide.with(context).load(sm.getImages()).into(image);
        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }
}