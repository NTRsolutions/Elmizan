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
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Library_adapter;
import com.sismatix.Elmizan.Adapter.Video_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Library_model;
import com.sismatix.Elmizan.Model.Video_Model;
import com.sismatix.Elmizan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dynamic_Library_freg extends Fragment {

    RecyclerView recycler_library;
    private List<Library_model> library_models = new ArrayList<Library_model>();
    private Library_adapter library_adapter;

    ProgressBar progressBar_library;
    public Dynamic_Library_freg() {
        // Required empty public constructor
    }
    public static Dynamic_Library_freg newInstance(int val) {
        Log.e("val=",""+val);
        Dynamic_Library_freg fragment = new Dynamic_Library_freg();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        return fragment;
    }

    int val;
    TextView c;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_dynamic_library_freg, container, false);
        val = getArguments().getInt("someInt", 0);
        c = view.findViewById(R.id.textView);
        c.setText("" + val);

        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.library));

        AllocateMemory(view);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            Call_Library_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        library_adapter = new Library_adapter(getActivity(), library_models);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_library.setLayoutManager(mLayoutManager);
        recycler_library.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_library.setAdapter(library_adapter);
        return view;

    }
    private void Call_Library_API() {
        for(int i=0;i<10;i++)
        {
            library_models.add(new Library_model("","",""));
            // news_adapter.notifyDataSetChanged();
            // news_adapter.notifyItemChanged(i);
        }
/*
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.categorylist("all");

        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    if (status.equalsIgnoreCase("success")){
                        String category=jsonObject.getString("category");
                        Log.e("catttt_prod_cat",""+category);
                        JSONArray jsonArray=jsonObject.getJSONArray("category");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                JSONObject vac_object = jsonArray.getJSONObject(i);
                                Log.e("Name",""+vac_object.getString("name"));
                                product_model.add(new Product_Category_model(vac_object.getString("name"),vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                product_category_adapter.notifyItemChanged(i);
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
        });*/
    }

    private void AllocateMemory(View view) {
        recycler_library = (RecyclerView) view.findViewById(R.id.recycler_library);
        progressBar_library = view.findViewById(R.id.progressBar_library);

    }


}
