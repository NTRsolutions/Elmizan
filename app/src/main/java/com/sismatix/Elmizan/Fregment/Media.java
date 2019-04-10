package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Media extends Fragment {

    public  static String u_id;
    public
    Media() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media, container, false);

        final TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.my_images)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.my_videos)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            u_id = bundle.getString("useridd");

            Log.e("uidd_med", "" + u_id);

        }

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager_tabs);
        final MediaTabAdapter adapter = new MediaTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());//getFragmentManager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    //tv_tab_titles.setText("MY ACCOUNT");
                    //tv_myaccount_title.setText("MY ACCOUNT");
                } else if (tab.getPosition() == 1) {
                    //tv_tab_titles.setText("MY ORDERS");
                    //tv_myaccount_title.setText("MY ORDERS");
                } else if (tab.getPosition() == 2) {
                    //Meassge_fragment.toolbar_messg.setVisibility(View.GONE);
                    //tv_tab_titles.setText("MESSAGES");
                    //tv_myaccount_title.setText("MESSAGES");
                } else if (tab.getPosition() == 3) {
                    //Wishlist_fragment.toolbar_mywishlist.setVisibility(View.GONE);
                    //tv_tab_titles.setText("WISHLIST");
                    //tv_myaccount_title.setText("WISHLIST");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return v;
    }

}
