package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sismatix.Elmizan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register_freg extends Fragment {
    EditText editTextname_reg,editTextEmail_reg,editTextphone_reg;
    TextInputEditText editTextpassword_reg;
    Button btn_register;


    public Register_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register_freg, container, false);
        AllocateMemory();
        editTextname_reg=(EditText)v.findViewById(R.id.editTextname_reg);
        editTextEmail_reg=(EditText)v.findViewById(R.id.editTextEmail_reg);
        editTextphone_reg=(EditText)v.findViewById(R.id.editTextphone_reg);
        editTextpassword_reg=(TextInputEditText)v.findViewById(R.id.editTextpassword_reg);
        btn_register=(Button)v.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }

    private void AllocateMemory() {

    }

}
