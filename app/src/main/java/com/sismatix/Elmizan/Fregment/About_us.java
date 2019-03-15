package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class About_us extends Fragment {

    View v;
    TextView tv_detail1,tv_detail,tv_balance_detail,tv_about_us;
    public About_us() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_about_us, container, false);
        AllocateMemory(v);
        return  v;
    }

    private void AllocateMemory(View v) {
        tv_about_us=(TextView)v.findViewById(R.id.tv_about_us);
        tv_balance_detail=(TextView)v.findViewById(R.id.tv_balance_detail);
        tv_detail=(TextView)v.findViewById(R.id.tv_detail);
        tv_detail1=(TextView)v.findViewById(R.id.tv_detail1);

        tv_about_us.setTypeface(Navigation_activity.typeface);
        tv_balance_detail.setTypeface(Navigation_activity.typeface);
        tv_detail.setTypeface(Navigation_activity.typeface);
        tv_detail1.setTypeface(Navigation_activity.typeface);

    }

}
