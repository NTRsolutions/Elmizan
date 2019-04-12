package com.sismatix.Elmizan.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.Premium_Lawyer_adapter;
import com.sismatix.Elmizan.Fregment.MediaImages;
import com.sismatix.Elmizan.Fregment.Premimum_Lawyer_freg;
import com.sismatix.Elmizan.R;

import me.relex.circleindicator.CircleIndicator;

public class MediaImageSlider extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private CircleIndicator indicator;
    ImageButton left_nav, right_nav;
    String position,old_image;
    ImageView delete_img_slide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_image_slider);
       /* getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        position = String.valueOf(getIntent().getExtras().getInt("pos"));
        Log.e("positionmis", "" + position);

      //  delete_img_slide = (ImageView) findViewById(R.id.delete_img_slide);
        left_nav = (ImageButton) findViewById(R.id.left_nav);
        right_nav = (ImageButton) findViewById(R.id.right_nav);
        mPager = (ViewPager) findViewById(R.id.pager_img);
        indicator = (CircleIndicator) findViewById(R.id.indicator_img);
        mPager.addOnPageChangeListener(this);
        //startAutoScrollViewPager();
        mPager.setAdapter(new Sliding_media_img_Adapter(MediaImageSlider.this, Premimum_Lawyer_freg.premium_lawyer_models));
        mPager.setCurrentItem(Integer.parseInt(position));
        indicator.setViewPager(mPager);


       /* delete_img_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_image=MediaImages.premium_lawyer_models.get(Integer.parseInt(position)).getOld_img();
                Log.e("old_img_62",""+old_image);
                Log.e("old_img_56",""+Integer.parseInt(position));
                Toast.makeText(MediaImageSlider.this, old_image+"old_imag=>>"+old_image, Toast.LENGTH_SHORT).show();

            }
        });*/

        left_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = mPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    mPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    mPager.setCurrentItem(tab);
                }
            }
        });

// Images right navigatin
        right_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = mPager.getCurrentItem();
                tab++;
                mPager.setCurrentItem(tab);
            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}