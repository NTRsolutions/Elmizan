package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.Premium_Lawyer_adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaImages extends Fragment {

    RecyclerView recycler_media_images;
    private List<Premium_Lawyer_Model> premium_lawyer_models = new ArrayList<Premium_Lawyer_Model>();
    private Premium_Lawyer_adapter premium_lawyer_adapter;

    public MediaImages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_images, container, false);

        AllocateMemory(v);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_GET_MEDIAIMAGES_API();


        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        premium_lawyer_adapter = new Premium_Lawyer_adapter(getActivity(), premium_lawyer_models);
        recycler_media_images.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recycler_media_images.setItemAnimator(new DefaultItemAnimator());
        recycler_media_images.setAdapter(premium_lawyer_adapter);


        return v;
    }

    private void CALL_GET_MEDIAIMAGES_API() {
        premium_lawyer_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.getMedia(Media.u_id);
        Log.e("uid", "" +Media.u_id);


        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                premium_lawyer_models.clear();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_media", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String msg_en = jsonObject.getString("data");
                        Log.e("data_189", "" + msg_en);

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("media_data", "" + data_obj);

                        String user_media_status = data_obj.getString("user_media_status");
                        Log.e("user_media_status_med", "" + user_media_status);

                        String article_media_url = data_obj.getString("arr_user_media_content_url");
                        Log.e("media_image", "" + article_media_url);

                        JSONObject image_obj = data_obj.getJSONObject("arr_user_media_content_url");
                        Log.e("imgg", "" + image_obj);

                        String image = image_obj.getString("image");
                        Log.e("imageeee", "" + image);

                        JSONArray imag_array = image_obj.getJSONArray("image");

                        if (imag_array != null && imag_array.isNull(0) != true) {
                            Log.e("imag_array", "" + imag_array);

                            for (int j = 0; j < imag_array.length(); j++) {
                                try {
                                    image = imag_array.getString(j);
                                    Log.e("image_prem", "" + image);

                                    if (image == "" || image == null || image == "null" || image.equalsIgnoreCase(null)
                                            || image.equalsIgnoreCase("null")) {
                                        Toast.makeText(getActivity(), "data problem", Toast.LENGTH_SHORT).show();
                                    } else {

                                        premium_lawyer_models.add(new Premium_Lawyer_Model(image));

                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    premium_lawyer_adapter.notifyItemChanged(j);
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AllocateMemory(View v) {
        recycler_media_images = (RecyclerView) v.findViewById(R.id.recycler_media_images);
    }

}
