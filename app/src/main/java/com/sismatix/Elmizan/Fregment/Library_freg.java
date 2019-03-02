package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sismatix.Elmizan.Adapter.PlansPagerAdapter;
import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Library_freg extends Fragment {
    TabLayout tabLayout;
    ViewPager viewpager;
    View view;

    public Library_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_library_freg, container, false);

        tabLayout= (TabLayout)view. findViewById(R.id.tablayout);
        viewpager= (ViewPager)view. findViewById(R.id.frameLayout);

        for (int k = 0; k <5; k++) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.international_law) +""+ k));
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                Log.e("tab_position",""+position);
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        PlansPagerAdapter adapter = new PlansPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(1);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() == 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        return view;
    }

}
