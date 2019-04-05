package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Demo_freg extends Fragment {

    private LinearLayout parentLinearLayout;
        Button add_field_button,delete_button;
        View v;
    public Demo_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_demo_freg, container, false);
        parentLinearLayout = (LinearLayout)v.findViewById(R.id.parent_linear_layout);
        add_field_button = (Button) v.findViewById(R.id.add_field_button);
        delete_button = (Button) v.findViewById(R.id.delete_button);

        add_field_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentLinearLayout.removeView((View) v.getParent());

            }
        });


        return v;
    }
   /* public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater)getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }*/

}
