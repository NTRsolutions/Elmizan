package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sismatix.Elmizan.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_premium_lawyer_profile extends Fragment {
    TextView tv_edit_your_img,tv_edit_user_name,tv_edit_appeal,tv_edit_personal_data,tv_edit_phone_text;
    EditText edt_title_name,edt_appeal;
    LinearLayout lv_edit_title,lv_edit_appeal;
    View v;
    CircleImageView iv_edit_lawyer_profile;
    ImageView iv_edit_camera;

    public Edit_premium_lawyer_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_edit_premium_lawyer_profile, container, false);
        AllocateMemory(v);
        return v;
    }

    private void AllocateMemory(View v) {
    }

}
