package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.Home_News_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
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
public class Home_freg extends Fragment {
    View view;
    RecyclerView recycler_news_home;
    private List<News_Model> news_model = new ArrayList<News_Model>();
    private Home_News_Adapter news_adapter;
    public static List<String> category_id = new ArrayList<String>();
    public static List<String> category_title = new ArrayList<String>();

    ProgressBar progressBar_home;
    public Home_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        
        Allocate_Memory(view);

        category_title.clear();
        category_id.clear();
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Home_News_API();
            CALL_categoryList_API();


        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        news_adapter = new Home_News_Adapter(getActivity(), news_model);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_news_home.setLayoutManager(mLayoutManager);
        recycler_news_home.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_news_home.setAdapter(news_adapter);

        return view;
    }
    private void CALL_categoryList_API() {
        //  progressBar_home.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> category_list = api.get_category_list();
        category_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_category", "" + response.body().toString());
                //   progressBar_home.setVisibility(View.GONE);
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
                                JSONObject category_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + category_object.getString("category_id"));
                                category_id.add(category_object.getString("category_id"));
                                category_title.add(category_object.getString("category_title"));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
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

    private void CALL_Home_News_API() {

        progressBar_home.setVisibility(View.VISIBLE);
        news_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_News_list(ApiClient.PAGE,ApiClient.PER_PAGE,ApiClient.user_status);

        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_news", "" + response.body().toString());
                progressBar_home.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                         JSONArray data_array=jsonObject.getJSONArray("data");

                         for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name",""+news_object.getString("news_title"));
                                news_model.add(new News_Model(news_object.getString("news_id"),
                                        news_object.getString("news_title"),
                                        news_object.getString("news_description"),
                                        news_object.getString("news_media"),
                                        news_object.getString("news_status"),
                                        news_object.getString("news_status"),
                                        news_object.getString("news_created_at_format"),
                                        news_object.getString("news_created_at_format_day"),
                                        news_object.getString("news_created_at_format_month"),
                                        news_object.getString("news_created_at_format_year")
                                ));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                news_adapter.notifyItemChanged(i);
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
        recycler_news_home = (RecyclerView) view.findViewById(R.id.recycler_news_home);
        progressBar_home = view.findViewById(R.id.progressBar_home);

    }

}
