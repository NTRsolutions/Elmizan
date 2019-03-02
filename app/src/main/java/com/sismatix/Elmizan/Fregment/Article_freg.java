package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Articles_Adapter;
import com.sismatix.Elmizan.Adapter.Video_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.Model.Video_Model;
import com.sismatix.Elmizan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Article_freg extends Fragment {
    View view;
    RecyclerView recycler_article;
    private List<Article_model> article_models = new ArrayList<Article_model>();
    private Articles_Adapter articles_adapter;

    ProgressBar progressBar_article;
    public Article_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_article_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.articles));

        AllocateMemory(view);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Directory_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        articles_adapter = new Articles_Adapter(getActivity(), article_models);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_article.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_article.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_article.setAdapter(articles_adapter);
        return view;
    }
    private void CALL_Directory_API() {
        for(int i=0;i<10;i++)
        {
            article_models.add(new Article_model("","",""));
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
        recycler_article = (RecyclerView) view.findViewById(R.id.recycler_article);
        progressBar_article = view.findViewById(R.id.progressBar_article);

    }


}
