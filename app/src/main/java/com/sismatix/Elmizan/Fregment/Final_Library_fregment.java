package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Category_Adapter;
import com.sismatix.Elmizan.Adapter.Library_adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Category_Model;
import com.sismatix.Elmizan.Model.Library_model;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;
import com.sismatix.Elmizan.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Final_Library_fregment extends Fragment {
    Category_Adapter category_adapter;
    private List<Category_Model> cat_model = new ArrayList<Category_Model>();
    public static ProgressBar progressBar_library_cat,progressBar_librarylist,progressBar_librarylist_bottom;
    View v;
    public static RecyclerView recycler_category,recycler_librarylist;
    LinearLayoutManager layoutManagerrr;
    LinearLayout lv_library_click_page;
    public static Library_adapter adapter;
    public static TextView tv_data_not_found_librarylist;
    public static WrapContentLinearLayoutManager layoutManager;
    public static List<Library_model> itemArrayList = new ArrayList<Library_model>();
    public static Context context=null;

    public static int page_no=1,page;
    public  static boolean isLoading = true;
    public static int pastvisibleitem, visibleitemcount, totalitemcount, previous_total = 0;


    public  static NestedScrollView nested_library_final;
    //varible for pagination
    public Final_Library_fregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_final__library_fregment, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setVisibility(View.GONE);

        AllocateMemory(v);
        context=getActivity();

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            Call_CATEGORY_API();
           // Log.e("catid_84",""+Category_Adapter.category_id);
           // CALL_LIBRARY_LIST_API(Category_Adapter.category_id,page_no);
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        category_adapter = new Category_Adapter(getActivity(), cat_model);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        layoutManagerrr=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recycler_category.setLayoutManager(layoutManagerrr);
        recycler_category.setAdapter(category_adapter);


        ///library api
        adapter = new Library_adapter(getActivity(), itemArrayList);
        layoutManager=new WrapContentLinearLayoutManager(getActivity());
        recycler_librarylist.setLayoutManager(layoutManager);
        recycler_librarylist.setAdapter(adapter);


        return v;
    }


    public static void CALL_LIBRARY_LIST_API(final String category_id1, int page_no) {
        Log.e("category_id_108", "" + category_id1);
        itemArrayList.clear();
        progressBar_librarylist.setVisibility(View.VISIBLE);
        tv_data_not_found_librarylist.setVisibility(View.GONE);
        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(String.valueOf(page_no), ApiClient.PER_PAGE, category_id1, ApiClient.user_status);
        page=page_no;
        Log.e("page_140", "" + page);

        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e(" ", "" + response.body().toString());
                progressBar_librarylist.setVisibility(View.GONE);

                try {
                    if (response.isSuccessful()) {
                        itemArrayList.clear();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        recycler_librarylist.setVisibility(View.VISIBLE);
                        tv_data_not_found_librarylist.setVisibility(View.GONE);

                        Log.e("status_library_134", "" + status);
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {

                                try {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String library_id = object.getString("library_id");
                                    Log.e("library_id", "" + library_id);
                                    Log.e("library_description", "" + object.getString("library_description"));
                                    String library_title = object.getString("library_title");
                                    String library_link = object.getString("library_link");
                                    String category_id = object.getString("category_id");
                                    String library_status = object.getString("library_status");

                                    String date = object.getString("library_created_at_format_day") + " " +
                                            object.getString("library_created_at_format_month") + " " +
                                            object.getString("library_created_at_format_year");

                                    Library_model model = new Library_model(library_id,
                                            library_title,
                                            library_link,
                                            category_id,
                                            date,
                                            library_status, object.getString("library_description"));

                                    itemArrayList.add(model);

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    adapter.notifyItemChanged(i);
                                }

                            }

                            Log.e("model_size_200",""+itemArrayList.size());
                            if (itemArrayList.size() == 0) {
                                tv_data_not_found_librarylist.setVisibility(View.VISIBLE);
                            } else {
                                tv_data_not_found_librarylist.setVisibility(View.GONE);
                            }

                        } else {

                            recycler_librarylist.setVisibility(View.GONE);
                            tv_data_not_found_librarylist.setText(message);
                            tv_data_not_found_librarylist.setVisibility(View.VISIBLE);
                            progressBar_librarylist.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar_librarylist.setVisibility(View.GONE);
                    Log.e("exception", "" + e.getLocalizedMessage());
                  //  Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //  Utils.showErrorSnackBar(getView().getRootView(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar_librarylist.setVisibility(View.GONE);
              //  Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Failure", "" + t.getLocalizedMessage());
            }
        });


        ////pagination
        nested_library_final.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                Log.e("isloading_238", "loadscroll" + page);
                                progressBar_librarylist_bottom.setVisibility(View.VISIBLE);


                                PerformPagination(page,category_id1);
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });


    }

    public static void PerformPagination(int pageee, String category_id1) {
        progressBar_librarylist_bottom.setVisibility(View.VISIBLE);

        final ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(String.valueOf(pageee), ApiClient.PER_PAGE, category_id1, ApiClient.user_status);
        page=pageee;
        Log.e("page_140", "" + pageee);

        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e(" ", "" + response.body().toString());
                progressBar_librarylist_bottom.setVisibility(View.GONE);

                try {
                    if (response.isSuccessful()) {

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        recycler_librarylist.setVisibility(View.VISIBLE);
                        tv_data_not_found_librarylist.setVisibility(View.GONE);

                        Log.e("pagination_library_253", "" + status);
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {

                                try {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String library_id = object.getString("library_id");
                                    Log.e("library_id", "" + library_id);
                                    Log.e("library_description", "" + object.getString("library_description"));
                                    String library_title = object.getString("library_title");
                                    String library_link = object.getString("library_link");
                                    String category_id = object.getString("category_id");
                                    String library_status = object.getString("library_status");

                                    String date = object.getString("library_created_at_format_day") + " " +
                                            object.getString("library_created_at_format_month") + " " +
                                            object.getString("library_created_at_format_year");

                                    Library_model model = new Library_model(library_id,
                                            library_title,
                                            library_link,
                                            category_id,
                                            date,
                                            library_status, object.getString("library_description"));

                                    itemArrayList.add(model);

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    adapter.notifyItemChanged(i);
                                }

                            }

                            Log.e("model_size_200",""+itemArrayList.size());
                            if (itemArrayList.size() == 0) {
                                tv_data_not_found_librarylist.setVisibility(View.VISIBLE);
                            } else {
                                tv_data_not_found_librarylist.setVisibility(View.GONE);
                            }

                        } else {

                           // recycler_librarylist.setVisibility(View.GONE);
                           // tv_data_not_found_librarylist.setText(message);
                            //tv_data_not_found_librarylist.setVisibility(View.VISIBLE);
                            progressBar_librarylist.setVisibility(View.GONE);
                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar_librarylist.setVisibility(View.GONE);
                    Log.e("exception", "" + e.getLocalizedMessage());
                    //Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //  Utils.showErrorSnackBar(getView().getRootView(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar_librarylist.setVisibility(View.GONE);
              //  Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Failure", "" + t.getLocalizedMessage());
            }
        });

    }


    private void Call_CATEGORY_API() {

        progressBar_library_cat.setVisibility(View.VISIBLE);
        lv_library_click_page.setVisibility(View.GONE);
        cat_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> category_list_api = api.get_category_list();

        category_list_api.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_category_93", "" + response.body().toString());
                //   progressBar_home.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    cat_model.clear();
                    progressBar_library_cat.setVisibility(View.GONE);
                    lv_library_click_page.setVisibility(View.VISIBLE);
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

                                Category_Model model = new Category_Model(category_object.getString("category_id"), category_object.getString("category_title"));
                                cat_model.add(model);


                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                category_adapter.notifyItemChanged(i);
                            }

                        }
                       //   viewPager.setCurrentItem(0);

                        //setUpCustomTabs();

                    } else if (status.equalsIgnoreCase("error")) {
                        lv_library_click_page.setVisibility(View.GONE);
                        progressBar_library_cat.setVisibility(View.GONE);

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
        recycler_librarylist=v.findViewById(R.id.recycler_librarylist);
        recycler_category=v.findViewById(R.id.recycler_category);
        lv_library_click_page=v.findViewById(R.id.lv_library_click_page);
        progressBar_library_cat=v.findViewById(R.id.progressBar_library_cat);
        progressBar_librarylist=v.findViewById(R.id.progressBar_librarylist);
        tv_data_not_found_librarylist=v.findViewById(R.id.tv_data_not_found_librarylist);
        nested_library_final=v.findViewById(R.id.nested_library_final);
        progressBar_librarylist_bottom=v.findViewById(R.id.progressBar_librarylist_bottom);

        tv_data_not_found_librarylist.setTypeface(Navigation_activity.typeface);
    }

}
