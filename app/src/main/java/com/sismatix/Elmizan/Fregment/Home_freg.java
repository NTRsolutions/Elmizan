package com.sismatix.Elmizan.Fregment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
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
import java.util.Locale;

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

    ProgressBar progressBar_home,progressBar_bottom_recv;
    NestedScrollView nested_home_scroll;
    TextView tv_data_not_found_home;
    //varible for pagination
    int page_no=1,page;


    LinearLayoutManager layoutManager;
    boolean isLoading = true;
    int pastvisibleitem, visibleitemcount, totalitemcount, previous_total = 0;

    public Home_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setVisibility(View.GONE);

        lang_arbi();
        Allocate_Memory(view);

        category_title.clear();
        category_id.clear();
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Home_News_API(String.valueOf(page_no));


        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        news_adapter = new Home_News_Adapter(getActivity(), news_model);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_news_home.setLayoutManager(layoutManager);
        recycler_news_home.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_news_home.setAdapter(news_adapter);

        return view;
    }

    private void CALL_Home_News_API(String pageno) {

        progressBar_home.setVisibility(View.VISIBLE);
        news_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_News_list(pageno,ApiClient.PER_PAGE,ApiClient.user_status);

        page= Integer.parseInt(pageno);

        Log.e("pageno_108",""+page);
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

                      //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

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
                                        news_object.getString("news_created_at_format_day"),
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
                        if (news_model.size() == 0) {
                            tv_data_not_found_home.setVisibility(View.VISIBLE);
                        } else {
                            tv_data_not_found_home.setVisibility(View.GONE);
                        }

                    }else if (status.equalsIgnoreCase("error")){
                        tv_data_not_found_home.setVisibility(View.VISIBLE);
                        progressBar_home.setVisibility(View.GONE);
                        recycler_news_home.setVisibility(View.GONE);

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



     /*   recycler_news_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleitemcount=layoutManager.getChildCount();
                totalitemcount=layoutManager.getItemCount();
                pastvisibleitem=layoutManager.findFirstVisibleItemPosition();
                if(dy>0)
                {
                    if(isLoading){
                        if(totalitemcount>previous_total)
                        {
                            isLoading=false;
                            previous_total=totalitemcount;
                        }
                    }
                    if(!isLoading&&(totalitemcount-visibleitemcount)<=(pastvisibleitem+view_threshold)){

                        page++;
                        PerformPagination(page);
                        Log.e("pagnation187","sdfs"+page);

                        progressBar_bottom_recv.setVisibility(View.VISIBLE);

                        isLoading=true;
                    }
                }
            }
        });

*/

        nested_home_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleitemcount = layoutManager.getChildCount();
                        totalitemcount = layoutManager.getItemCount();
                        pastvisibleitem = layoutManager.findFirstVisibleItemPosition();

                        if (isLoading) {

                            if ((visibleitemcount + pastvisibleitem) >= totalitemcount) {
                                page++;
                                Log.e("isloading", "loadscroll" + page);
                                progressBar_bottom_recv.setVisibility(View.VISIBLE);

                                PerformPagination(page);
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });

    }

    private void PerformPagination(final int page) {
        Log.e("pagnation","sdfs");
        Log.e("page_pagination_236","sdfs"+page);
        progressBar_bottom_recv.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_News_list(String.valueOf(page),ApiClient.PER_PAGE,ApiClient.user_status);

     //   page= Integer.parseInt(page);


        Log.e("pageno_108",""+page);
        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_news", "" + response.body().toString());
                progressBar_home.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                   // progressBar_bottom_recv.setVisibility(View.GONE);
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                     //   Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

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
                                        news_object.getString("news_created_at_format_day"),
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
                        progressBar_bottom_recv.setVisibility(View.GONE);
                       // Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                       // tv_data_not_found_home.setVisibility(View.VISIBLE);


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

        // CALL_Home_News_API(String.valueOf(page));
    }

    private void Allocate_Memory(View view) {
        recycler_news_home = (RecyclerView) view.findViewById(R.id.recycler_news_home);
        nested_home_scroll = (NestedScrollView) view.findViewById(R.id.nested_home_scroll);
        progressBar_bottom_recv = (ProgressBar) view.findViewById(R.id.progressBar_bottom_recv);
        progressBar_home = view.findViewById(R.id.progressBar_home);
        tv_data_not_found_home = view.findViewById(R.id.tv_data_not_found_home);
        tv_data_not_found_home.setTypeface(Navigation_activity.typeface);

    }
    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_fram_layout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.commit();
            }
        }
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
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

                    pushFragment(new Home_freg(),"");
                    //getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }*/

}
