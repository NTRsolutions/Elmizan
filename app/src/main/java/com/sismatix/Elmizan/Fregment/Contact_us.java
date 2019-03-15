package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact_us extends Fragment {

    View v;
    public Contact_us() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_contact_us, container, false);
        return  v;
    }

}
