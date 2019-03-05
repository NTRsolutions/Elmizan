package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Directory_of_lawyers_freg extends Fragment {

    TextView tv_name_dircto,tv_shortdicription_dircto,tv_phonenumber_dircto,tv_address_dircto,tv_email_direct,tv_site_dircto;
    ImageView iv_lawyer_profile;

    public Directory_of_lawyers_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_directory_of_lawyers_freg, container, false);
        Allocate_Memory(v);

        CALL_USER_DETAIL_API();
        return v;
    }

    private void CALL_USER_DETAIL_API() {

        String user_id=Login_preference.getuser_id(getActivity());
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_detail = api.get_user_detail(user_id);


        user_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

                        JSONObject data_obj=jsonObject.getJSONObject("data");
                        Log.e("status_data_obj",""+data_obj);

                        tv_name_dircto.setText(data_obj.getString("user_name"));
                        tv_address_dircto.setText(data_obj.getString("user_address"));
                       tv_email_direct.setText(data_obj.getString("user_email"));
                        tv_phonenumber_dircto.setText(data_obj.getString("user_phone"));
                       // tv_shortdicription_dircto.setText(data_obj.getString("user_phone"));
                        tv_site_dircto.setText(data_obj.getString("user_website"));
                        Glide.with(getActivity()).load(data_obj.getString("user_avatar_url")).into(iv_lawyer_profile);


                    }else if (status.equalsIgnoreCase("error")){
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Allocate_Memory(View v) {
        tv_address_dircto=(TextView)v.findViewById(R.id.tv_address_dircto);
        tv_name_dircto=(TextView)v.findViewById(R.id.tv_name_dircto);
        tv_shortdicription_dircto=(TextView)v.findViewById(R.id.tv_shortdicription_dircto);
        tv_phonenumber_dircto=(TextView)v.findViewById(R.id.tv_phonenumber_dircto);
        tv_email_direct=(TextView)v.findViewById(R.id.tv_email_direct);
        tv_site_dircto=(TextView)v.findViewById(R.id.tv_site_dircto);
        iv_lawyer_profile=(ImageView) v.findViewById(R.id.iv_lawyer_profile);

    }

}
