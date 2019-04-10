package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Directory_of_lawyers_freg extends Fragment {

    TextView tv_name_dircto,tv_shortdicription_dircto,tv_phonenumber_dircto,tv_address_dircto,tv_email_direct,tv_site_dircto;
    CircleImageView iv_lawyer_profile;
    ProgressBar progressBar_user_detail;
    LinearLayout lv_user_detail;
    Bundle bundle;
    String user_id;
    public Directory_of_lawyers_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_directory_of_lawyers_freg, container, false);
        Allocate_Memory(v);
        bundle = this.getArguments();

        if(bundle!=null){
            user_id=bundle.getString("user_id");
            Log.e("user_id_58",""+user_id);
        }
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_USER_DETAIL_API();

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void CALL_USER_DETAIL_API() {
        progressBar_user_detail.setVisibility(View.VISIBLE);
        lv_user_detail.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_detail = api.get_user_detail(user_id);


        user_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);


                progressBar_user_detail.setVisibility(View.GONE);
                lv_user_detail.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

                        JSONObject data_obj=jsonObject.getJSONObject("data");
                        Log.e("status_data_obj",""+data_obj);

                        tv_name_dircto.setTypeface(Navigation_activity.typeface);
                        tv_address_dircto.setTypeface(Navigation_activity.typeface);
                        tv_phonenumber_dircto.setTypeface(Navigation_activity.typeface);
                        tv_email_direct.setTypeface(Navigation_activity.typeface);
                        tv_site_dircto.setTypeface(Navigation_activity.typeface);
                        tv_shortdicription_dircto.setTypeface(Navigation_activity.typeface);



                        Navigation_activity.Check_String_NULL_Value(tv_name_dircto,data_obj.getString("user_name"));
                        Navigation_activity.Check_String_NULL_Value(tv_address_dircto,data_obj.getString("user_address"));
                        Navigation_activity.Check_String_NULL_Value(tv_phonenumber_dircto,data_obj.getString("user_phone"));
                        Navigation_activity.Check_String_NULL_Value(tv_email_direct,data_obj.getString("user_email"));
                        Navigation_activity.Check_String_NULL_Value(tv_site_dircto,data_obj.getString("user_website"));
                        Navigation_activity.Check_String_NULL_Value(tv_shortdicription_dircto,data_obj.getString("user_description"));
                        /*//tv_name_dircto.setText(data_obj.getString("user_name"));
                        tv_address_dircto.setText(data_obj.getString("user_address"));
                        tv_email_direct.setText(data_obj.getString("user_email"));
                        tv_phonenumber_dircto.setText(data_obj.getString("user_phone"));
                        tv_shortdicription_dircto.setText(data_obj.getString("user_description"));
                        tv_site_dircto.setText(data_obj.getString("user_website"));*/
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
        iv_lawyer_profile=(CircleImageView) v.findViewById(R.id.iv_lawyer_profile);

        lv_user_detail=(LinearLayout) v.findViewById(R.id.lv_user_detail);
        progressBar_user_detail=(ProgressBar) v.findViewById(R.id.progressBar_user_detail);

    }

    /*@Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }*/

}
