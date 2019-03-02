package com.sismatix.Elmizan.Fregment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class News_Detail_freg extends Fragment {
    View v;
    Toolbar toolbar_News_detail;

    public News_Detail_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_news__detail_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        /*toolbar_News_detail = (android.support.v7.widget.Toolbar) v.findViewById(R.id.toolbar_News_detail);

        ((Navigation_activity) getActivity()).setSupportActionBar(toolbar_News_detail);
        ((Navigation_activity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Navigation_activity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_right_white_36dp);
         //getActivity().setTitle("Shopping Cart");
*/
            Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
            Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.single_news_page));
            //Navigation_activity.toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //  Navigation_activity.toolbar.setTitle(getResources().getString(R.string.single_news_page));
        return v;
    }

}
