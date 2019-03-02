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

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Home_News_API();
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

    private void CALL_Home_News_API() {
        for(int i=0;i<10;i++)
        {
            news_model.add(new News_Model("22 من آلاف ,2019","من آلاف الناشرين والمج","أخبار Google عبارة عن أداة تجميع أخبار وتطبيق تم تطويرهما بواسطة Google. يقدم تدفق مستمر وقابل للتخصيص من المقالات المنظمة من آلاف الناشرين والمج"));
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

    private void Allocate_Memory(View view) {
        recycler_news_home = (RecyclerView) view.findViewById(R.id.recycler_news_home);
        progressBar_home = view.findViewById(R.id.progressBar_home);

    }

}
