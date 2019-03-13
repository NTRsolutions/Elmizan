package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Library_adapter;
import com.sismatix.Elmizan.Adapter.Video_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Library_model;
import com.sismatix.Elmizan.Model.Video_Model;
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

public class Dynamic_Library_freg extends Fragment {

    public static RecyclerView recycler_library;
    public static List<Library_model> library_models = new ArrayList<Library_model>();
    public static Library_adapter library_adapter;

    public static   ProgressBar progressBar_library;
    public static Context context=null;
    int val,tab_position;
    TextView c;
    public  static   String category_id;
    View view;
    public static Dynamic_Library_freg newInstance() {
        return new Dynamic_Library_freg();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_library_freg, container, false);

        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.library));

        AllocateMemory(view);
        initViews(view);


        library_adapter = new Library_adapter(getActivity(), library_models);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recycler_library.setLayoutManager(mLayoutManager);
        recycler_library.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_library.setAdapter(library_adapter);
        return view;
    }

    private void initViews(View view) {

        TextView textView=view.findViewById(R.id.textView);
        val = getArguments().getInt("position");
        // tab_position=Library_freg.position;
        category_id=Home_freg.category_id.get(val);
        //Log.e("tab_position_61",""+tab_position);
        Log.e("category_id_64",""+category_id);
        Log.e("val",""+val);
       // textView.setText(String.valueOf("Category :  "+getArguments().getInt("position"))+"=categoryid =>" +category_id);
        context=getActivity();

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
           // Call_Library_API(category_id);
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
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
                Log.e("response", "" + response.body().toString());
                progressBar_library.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


/**
 * A simple {@link Fragment} subclass.
 *//*

public class Dynamic_Library_freg extends Fragment {

   public static RecyclerView recycler_library;
    public static List<Library_model> library_models = new ArrayList<Library_model>();
    public static Library_adapter library_adapter;

  public static   ProgressBar progressBar_library;
  public static Context context=null;
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

    int val,tab_position;
    TextView c;
  public  static   String category_id;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_dynamic_library_freg, container, false);
        val = getArguments().getInt("someInt", 0);
        tab_position=Library_freg.position;
        category_id=Home_freg.category_id.get(val);
        Log.e("tab_position_61",""+tab_position);
        Log.e("category_id_64",""+category_id);
        Log.e("val",""+val);

        context=getActivity();
        c = view.findViewById(R.id.textView);
        c.setText("" + val);


        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.library));

        AllocateMemory(view);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            Call_Library_API(category_id);
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
    public static void Call_Library_API(String cat_id) {

        Log.e("category_id_108", "" + cat_id);

        library_models.clear();
        //progressBar.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> get_library_list = api.get_library_list(ApiClient.PAGE,ApiClient.PER_PAGE,cat_id);


        get_library_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

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

    }


}
*/