package com.sismatix.Elmizan.Fregment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
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
public class News_Detail_freg extends Fragment implements View.OnClickListener {
    View view;
    String news_id;
    TextView  tv_detail_news_title,tv_news_detail_date,tv_news_detail_description,tv_detail_news_add_comment,tv_posted_by,tv_posted;
    ImageView iv_news_detail_like,iv_news_detail_bookmark,iv_news_detail_share,iv_news_detail_image;
    EditText edt_news_detail_comment;
    LinearLayout lv_news_detail_send;

    public News_Detail_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news__detail_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);

        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.single_news_page));

        Allocate_Memory(view);
        Bundle bundle = this.getArguments();

        if (bundle != null){

            news_id = bundle.getString("news_id");
            Log.e("news_id_43", "" + news_id);
            }


        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_News_Detail_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lv_news_detail_send.setOnClickListener(this);
        iv_news_detail_like.setOnClickListener(this);

        return view;

    }

    private void CALL_NEWS_ADD_COMMENT() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("edittext_value",""+edt_news_detail_comment.getText().toString());
        String userid="38";
        Log.e("News",""+news_id+" userid "+userid+" comment"+edt_news_detail_comment.getText().toString());
        Call<ResponseBody> add_comment = api.get_news_add_comment(news_id,userid,edt_news_detail_comment.getText().toString());

        add_comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  Log.e("response_addcoment", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")) {


                    String message=jsonObject.getString("msg");
                        Log.e("msg",""+message);
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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

    private void CALL_News_Detail_API() {
        //progressBar.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> news_detail = api.get_news_detail(news_id);


        news_detail.enqueue(new Callback<ResponseBody>() {
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

                        tv_detail_news_title.setText(data_obj.getString("news_title"));
                        tv_news_detail_date.setText(data_obj.getString("news_created_at"));
                        tv_news_detail_description.setText(data_obj.getString("news_description"));
                        tv_posted_by.setText(data_obj.getString("news_created_by"));

                        JSONObject image_obj=data_obj.getJSONObject("news_media_urls");
                        Log.e("img_obj",""+image_obj);
                        JSONArray jsonArray=image_obj.getJSONArray("image");
                        Log.e("jsonArray_news",""+jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                String image=jsonArray.getString(i);
                                Glide.with(getActivity()).load(image).into(iv_news_detail_image);

                                // JSONObject vac_object = jsonArray.getJSONObject(i);
                                Log.e("image_news",""+image);
                               // product_model.add(new Product_Category_model(vac_object.getString("name"),vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                             //   product_category_adapter.notifyItemChanged(i);
                            }

                        }

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

    private void Allocate_Memory(View view) {

        tv_posted = (TextView) view.findViewById(R.id.tv_posted);
        tv_posted_by = (TextView) view.findViewById(R.id.tv_posted_by);
        tv_detail_news_title = (TextView) view.findViewById(R.id.tv_detail_news_title);
        tv_news_detail_date = (TextView) view.findViewById(R.id.tv_news_detail_date);
        tv_news_detail_description = (TextView) view.findViewById(R.id.tv_news_detail_description);
        tv_detail_news_add_comment = (TextView) view.findViewById(R.id.tv_detail_news_add_comment);
        iv_news_detail_like = (ImageView) view.findViewById(R.id.iv_news_detail_like);
        iv_news_detail_bookmark = (ImageView) view.findViewById(R.id.iv_news_detail_bookmark);
        iv_news_detail_share = (ImageView) view.findViewById(R.id.iv_news_detail_share);
        iv_news_detail_image = (ImageView) view.findViewById(R.id.iv_news_detail_image);
        edt_news_detail_comment = view.findViewById(R.id.edt_news_detail_comment);
        lv_news_detail_send = view.findViewById(R.id.lv_news_detail_send);

    }

    @Override
    public void onClick(View view) {
        if(view==lv_news_detail_send)
        {
            if(edt_news_detail_comment.getText().length()==0){
                Toast.makeText(getActivity(), "Enter Comment", Toast.LENGTH_SHORT).show();
            }else {

                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    CALL_NEWS_ADD_COMMENT();
                } else {
                    Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(view==iv_news_detail_like)
        {
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                CALL_News_Like_API();
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void CALL_News_Like_API() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        String userid="38";
        Call<ResponseBody> news_like = api.get_news_like(news_id,userid);

        news_like.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  Log.e("response_addcoment", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")) {

                        String checked_value=jsonObject.getString("check_if_news_liked");
                        Log.e("checked_value",""+checked_value);
                        if(checked_value.equalsIgnoreCase("true")==true)
                        {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        }else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }
                        String message=jsonObject.getString("msg");
                        Log.e("msg",""+message);
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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
}
