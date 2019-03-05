package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.PlansPagerAdapter;
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
public class Library_freg extends Fragment {
    TabLayout tabLayout;
    ViewPager viewpager;
    public static int position;
    View view;
   // public static List<String> category_id = new ArrayList<String>();

    public Library_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_library_freg, container, false);

        Allocate_Memory(view);

       /* if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_categoryList_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }*/
        initViews();
        return view;
    }

    private void initViews(){

        Set_Dybamic_TAB_VIEW();
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewpager.setOffscreenPageLimit(1);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.e("tab_position", "" + position);
                String cattid=Home_freg.category_id.get(position);
                Log.e("tab_cattid_78", "" + cattid);
                // Dynamic_Library_freg.Call_Library_API(cattid);

                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (tabLayout.getTabCount() == 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }




    }

    private void Set_Dybamic_TAB_VIEW() {
        for (int i=0;i<Home_freg.category_title.size();i++)
        {
            tabLayout.addTab(tabLayout.newTab().setText(Home_freg.category_title.get(i)));
            PlansPagerAdapter adapter = new PlansPagerAdapter
                    (getChildFragmentManager(), tabLayout.getTabCount());
            viewpager.setAdapter(adapter);
            viewpager.setCurrentItem(0);
        }

    }

/*
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
                                tabLayout.addTab(tabLayout.newTab().setText(category_object.getString("category_title")));

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
*/

    private void Allocate_Memory(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewpager = (ViewPager) view.findViewById(R.id.frameLayout);


    }

}
