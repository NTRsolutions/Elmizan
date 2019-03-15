package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class New_dynamic_Library_frag extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_CATEGORY_TITLE = "category_title";
    private static final String ARG_VISIBLE = "visible";
    public static List<Library_model> itemArrayList = new ArrayList<Library_model>();
    private RecyclerView recyclerviewSubCategory;

    private Library_adapter adapter;
    private String category_id, category_title;
    private ProgressBar progressBar_library;
    private TextView tvNoDataFound;

    private boolean isVisible;
    public static List<String> cat_id_list = new ArrayList<String>();
    TextView textView;

    public static New_dynamic_Library_frag newInstance(String sectionNumber, boolean visible, String category_title) {
        New_dynamic_Library_frag fragment = new New_dynamic_Library_frag();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, sectionNumber);
        args.putBoolean(ARG_VISIBLE, visible);
        args.putString(ARG_CATEGORY_TITLE, category_title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_dynamic_library_frag, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cat_id_list.clear();

        ALLOCATEMEMORY(view);

        if (getArguments() != null) {
            category_id = getArguments().getString(ARG_CATEGORY_ID, "");
            isVisible = getArguments().getBoolean(ARG_VISIBLE, false);
            category_title = getArguments().getString(ARG_CATEGORY_TITLE, "");
        }

        cat_id_list.add(category_id);
        Log.e("Tab_service_114", "" + category_id);
        textView.setText(category_id + " =" + category_title);
       // Log.e("Tab_name_115", "" + category_title);
        //Log.e("Tab_visible_115", "" + isVisible);

        for (int i = 0; i < cat_id_list.size(); i++) {

           // itemArrayList.clear();
            Log.e("service_else_143", "" + cat_id_list.get(i));

            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                CALL_LIBRARY_LIST_API(cat_id_list.get(i));
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
        cat_id_list.clear();


    }

    private void ALLOCATEMEMORY(View view) {
        textView = (TextView) view.findViewById(R.id.textView);
        //recycler view
        recyclerviewSubCategory = view.findViewById(R.id.recycler_library);
        //progress bar
        progressBar_library = view.findViewById(R.id.progressBar_library);
        //text view
        tvNoDataFound = view.findViewById(R.id.tv_data_not_found_library);

    }

    private void CALL_LIBRARY_LIST_API(String category_id1) {
        Log.e("category_id_108", "" + category_id1);
        itemArrayList.clear();
        progressBar_library.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(ApiClient.PAGE, ApiClient.PER_PAGE, category_id1, ApiClient.user_status);
        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.e(" ", "" + response.body().toString());
                progressBar_library.setVisibility(View.GONE);

                try {
                    if (response.isSuccessful()) {
                        itemArrayList.clear();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        recyclerviewSubCategory.setVisibility(View.VISIBLE);
                        tvNoDataFound.setVisibility(View.GONE);

                        Log.e("status_news_detail", "" + status);
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String library_id = object.getString("library_id");
                                Log.e("library_id", "" + library_id);
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
                                        library_status);

                                itemArrayList.add(model);
                            }
                            adapter = new Library_adapter(getActivity(), itemArrayList);
                            recyclerviewSubCategory.setAdapter(adapter);
                            recyclerviewSubCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

                            if (itemArrayList.size() == 0) {
                                tvNoDataFound.setVisibility(View.VISIBLE);
                            } else {
                                tvNoDataFound.setVisibility(View.GONE);
                            }

                        } else {

                            recyclerviewSubCategory.setVisibility(View.GONE);
                            tvNoDataFound.setText(message);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar_library.setVisibility(View.GONE);
                    Log.e("exception", "" + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //  Utils.showErrorSnackBar(getView().getRootView(), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar_library.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Failure", "" + t.getLocalizedMessage());
            }
        });


    }

/*
    public  void Call_Library_API(String cat_id) {

        Log.e("category_id_108", "" + cat_id);

        itemArrayList.clear();
        progressBar_library.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(ApiClient.PAGE,ApiClient.PER_PAGE,cat_id,ApiClient.user_status);


        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                progressBar_library.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    String message=jsonObject.getString("msg");
                    if (status.equalsIgnoreCase("success")){

                        JSONArray data_array=jsonObject.getJSONArray("data");
                        Log.e("data_array",""+data_array);
                        recyclerviewSubCategory.setVisibility(View.VISIBLE);
                        tvNoDataFound.setVisibility(View.GONE);
                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject data_object = data_array.getJSONObject(i);

                                String date=data_object.getString("library_created_at_format_day")+" "+
                                        data_object.getString("library_created_at_format_month")+" "+
                                        data_object.getString("library_created_at_format_year");

                                Log.e("library_id",""+data_object.getString("library_id"));

                                itemArrayList.add(new Library_model(data_object.getString("library_id")
                                        ,data_object.getString("library_title"),
                                        data_object.getString("library_link"),
                                        data_object.getString("category_id"),
                                        date,
                                        data_object.getString("library_status")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                adapter.notifyItemChanged(i);
                            }

                        }
                        if (itemArrayList.size() == 0) {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            tvNoDataFound.setVisibility(View.GONE);
                        }

                    }else if (status.equalsIgnoreCase("error")){
                        recyclerviewSubCategory.setVisibility(View.GONE);
                        tvNoDataFound.setText(message);
                        tvNoDataFound.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }
*/


}