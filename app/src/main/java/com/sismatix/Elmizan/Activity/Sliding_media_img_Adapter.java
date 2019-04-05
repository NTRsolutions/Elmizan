package com.sismatix.Elmizan.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Sliding_media_img_Adapter extends PagerAdapter {
    Context context;
    private List<Premium_Lawyer_Model> sliderimage_models;
    private LayoutInflater inflater;
    String screen;

    public Sliding_media_img_Adapter(Context context, List<Premium_Lawyer_Model> sliderimage_models) {

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

        final ImageView image;
        final ProgressBar progress_slider;

        View view = null;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.media_sliding_row, container, false);
        final Premium_Lawyer_Model sm = sliderimage_models.get(pos);

        image = (ImageView) view.findViewById(R.id.image);
        progress_slider = (ProgressBar) view.findViewById(R.id.progress_slider);
        // Log.e("packagegridviewimage",""+Package_slider.position);
        // progress_slider.setVisibility(View.VISIBLE);

        /*Glide.with(context).load(sm.getLawyer_image())
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progress_slider.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress_slider.setVisibility(View.GONE);
                        return false;
                    }

                }).placeholder(R.drawable.ic_launcher_background).fallback(R.drawable.ic_launcher_background).into(image);*/
         Glide.with(context).load(sm.getLawyer_image()).into(image);
        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }
}