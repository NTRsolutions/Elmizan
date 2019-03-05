package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Directory_Adapter;
import com.sismatix.Elmizan.Adapter.Home_News_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Directory_Model;
import com.sismatix.Elmizan.Model.News_Model;
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
public class Directory_freg extends Fragment {
    RecyclerView recycler_directory;
    private List<Directory_Model> directory_model = new ArrayList<Directory_Model>();
    private Directory_Adapter directory_adapter;

    ProgressBar progressBar_directory;
    View view;
    public Directory_freg() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_directory_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.directory));

        AllocateMemory(view);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Directory_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        directory_adapter = new Directory_Adapter(getActivity(), directory_model);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_directory.setLayoutManager(mLayoutManager);
        recycler_directory.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_directory.setAdapter(directory_adapter);
        return view;
    }
    private void CALL_Directory_API() {
        /*for(int i=0;i<10;i++)
        {
            directory_model.add(new Directory_Model(""));
            // news_adapter.notifyDataSetChanged();
            // news_adapter.notifyItemChanged(i);
        }*/

        progressBar_directory.setVisibility(View.VISIBLE);
        directory_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_list = api.get_User_list(ApiClient.PAGE,ApiClient.PER_PAGE,ApiClient.user_type,ApiClient.user_status);

        user_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_user", "" + response.body().toString());
                progressBar_directory.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray data_array = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject user_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + user_object.getString("user_id"));
                                directory_model.add(new Directory_Model(user_object.getString("user_id"),
                                        user_object.getString("user_name"),
                                        user_object.getString("user_email"),
                                        user_object.getString("user_phone"),
                                        user_object.getString("user_address"),
                                        user_object.getString("user_firstname"),
                                        user_object.getString("user_lastname"),
                                        user_object.getString("user_avatar"),
                                        user_object.getString("user_type"),
                                        user_object.getString("user_website"),
                                        user_object.getString("basic_premium"),
                                        user_object.getString("user_status"),
                                        user_object.getString("is_online")
                                        ,user_object.getString("user_updated_at"),
                                        user_object.getString("user_verify_token")
                                        ,user_object.getString("user_created_at"),
                                        user_object.getString("user_avatar_url")));
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                 directory_adapter.notifyItemChanged(i);

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

    private void AllocateMemory(View view) {
        recycler_directory = (RecyclerView) view.findViewById(R.id.recycler_directory);
        progressBar_directory = view.findViewById(R.id.progressBar_directory);

    }

}
