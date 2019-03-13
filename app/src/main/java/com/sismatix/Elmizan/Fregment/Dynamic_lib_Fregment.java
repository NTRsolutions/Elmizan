package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Library_adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Library_model;
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
public class Dynamic_lib_Fregment extends Fragment {

    private static Navigation_activity mActivity;
    public static RecyclerView recycler_library;
    public static List<Library_model> library_models = new ArrayList<Library_model>();
    public static Library_adapter library_adapter;

    public static ProgressBar progressBar_library;
    public static Context context=null;
    int val,tab_position;
    public  static TextView c,tv_data_not_found_library;
    public  static   String category_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (Navigation_activity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_library_freg, container, false);

       // setupRecyclerView(rv);
        AllocateMemory(view);
        TextView textView=view.findViewById(R.id.textView);
        val = getArguments().getInt("type");
        context=getActivity();



        library_adapter = new Library_adapter(getActivity(), library_models);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_library.setLayoutManager(mLayoutManager);
        recycler_library.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_library.setAdapter(library_adapter);


        if(val==0)
        {
            category_id=Home_freg.category_id.get(val);
            Log.e("category_id_64",""+category_id);
            Log.e("val",""+val);
            textView.setText(String.valueOf("Category :  "+getArguments().getInt("type"))+"=categoryid =>" +category_id);

            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                Call_Library_API(category_id);
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }else if(val==1) {
            category_id=Home_freg.category_id.get(val);
            Log.e("category_id_95",""+category_id);
            Log.e("val_95",""+val);
            textView.setText(String.valueOf("Category :  "+getArguments().getInt("type"))+"=categoryid =>" +category_id);
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                Call_Library_API(category_id);
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }else {
            textView.setText(String.valueOf("Category :  "+getArguments().getInt("type"))+"=categoryid =>" +category_id);

        }
/*

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
             Call_Library_API(category_id);
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
*/

        return view;
    }
    public static void Call_Library_API(String cat_id) {

        Log.e("category_id_108", "" + cat_id);
        library_models.clear();
        progressBar_library.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(ApiClient.PAGE,ApiClient.PER_PAGE,cat_id,ApiClient.user_status);


        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_libr", "" + response.body().toString());
                progressBar_library.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){
                        tv_data_not_found_library.setVisibility(View.GONE);
                        recycler_library.setVisibility(View.VISIBLE);

                        JSONArray data_array=jsonObject.getJSONArray("data");
                        Log.e("data_array",""+data_array);


                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject data_object = data_array.getJSONObject(i);
                                library_models.add(new Library_model(data_object.getString("library_id")
                                        ,data_object.getString("library_title"),
                                        data_object.getString("library_link"),
                                        data_object.getString("category_id"),
                                        data_object.getString("library_created_at"),
                                        data_object.getString("library_status")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                library_adapter.notifyItemChanged(i);
                            }

                        }

                    }else if (status.equalsIgnoreCase("error")){

                        tv_data_not_found_library.setVisibility(View.VISIBLE);
                        recycler_library.setVisibility(View.GONE);
                        progressBar_library.setVisibility(View.GONE);

                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AllocateMemory(View view) {
        recycler_library = (RecyclerView) view.findViewById(R.id.recycler_library);
        progressBar_library = view.findViewById(R.id.progressBar_library);
        tv_data_not_found_library = view.findViewById(R.id.tv_data_not_found_library);

    }

   /* private void setupRecyclerView(RecyclerView recyclerView) {
      *//*  if (ImageListFragment.this.getArguments().getInt("type") == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else if (ImageListFragment.this.getArguments().getInt("type") == 2) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), 3);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }*//*
        String[] items=null;
        if (ImageListFragment.this.getArguments().getInt("type") == 1){
            items =ImageUrlUtils.getOffersUrls();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 2){
            items =ImageUrlUtils.getElectronicsUrls();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 3){
            items =ImageUrlUtils.getLifeStyleUrls();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 4){
            items =ImageUrlUtils.getHomeApplianceUrls();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 5){
            items =ImageUrlUtils.getBooksUrls();
        }else {
            items = ImageUrlUtils.getImageUrls();
        }
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView, items));
    }
*/
}
