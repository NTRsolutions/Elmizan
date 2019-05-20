package com.sismatix.Elmizan.Fregment;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Directory_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Directory_Model;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;
import com.sismatix.Elmizan.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Directory_freg extends Fragment implements SearchView.OnQueryTextListener {
    RecyclerView recycler_directory;
    private List<Directory_Model> directory_model = new ArrayList<Directory_Model>();
    private Directory_Adapter directory_adapter;
    TextView tv_data_not_found;
    ProgressBar progressBar_directory,progressBar_bottom_directory;
    NestedScrollView nested_directory_scroll;
    SearchView searchView;
    LinearLayout lv_directory_parent;

    LinearLayoutManager layoutManager;
    int page_no=1,page;
    boolean isLoading = true;
    int pastvisibleitem, visibleitemcount, totalitemcount, previous_total = 0;

    View view;
    public Directory_freg() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_directory_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.directory));
        Navigation_activity. tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        Login_freg.hideSoftKeyboard(getActivity());
        AllocateMemory(view);
        setupUI(lv_directory_parent);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_Directory_API("null", String.valueOf(page_no));
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        directory_adapter = new Directory_Adapter(getActivity(), directory_model);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        layoutManager=new LinearLayoutManager(getActivity());
        recycler_directory.setLayoutManager(layoutManager);
        recycler_directory.setAdapter(directory_adapter);


        searchView.setOnQueryTextListener(this);

        return view;
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


    private void CALL_Directory_API(final String text, String pageeno) {
        directory_model.clear();
        String serched_text=text;
        Log.e("serched_text_83",""+serched_text);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_list;
        String country_id ="";
        if(serched_text.equalsIgnoreCase("null")==true || serched_text==null)
        {
            progressBar_directory.setVisibility(View.VISIBLE);
            directory_model.clear();
            Log.e("derecotyr",""+serched_text);
            serched_text = "";

        }
        if(My_Preference.getCountry_name(getActivity()).equalsIgnoreCase("1")==false) {
            progressBar_directory.setVisibility(View.VISIBLE);
            directory_model.clear();
            Log.e("country_id_104",""+My_Preference.getCountry_name(getActivity()));
            country_id = My_Preference.getCountry_name(getActivity());
        }else {
            Log.e("country_idelse_108",""+My_Preference.getCountry_name(getActivity()));
            country_id = My_Preference.getCountry_name(getActivity());
        }
        user_list = api.get_User_list(pageeno,ApiClient.PER_PAGE,ApiClient.user_type,ApiClient.user_status,serched_text,country_id);
        Log.e("dir_167serched_text",""+serched_text);
        Log.e("dir_167country_id",""+country_id);
        Log.e("dir_167pageeno",""+pageeno);
        Log.e("dir_167PER_PAGE",""+ApiClient.PER_PAGE);
        Log.e("dir_167user_type",""+ApiClient.user_type);
        Log.e("dir_167user_status",""+ApiClient.user_status);

        page= Integer.parseInt(pageeno);
        Log.e("page_122",""+page);

        user_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_user", "" + response.body().toString());
                progressBar_directory.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    directory_model.clear();
                    tv_data_not_found.setVisibility(View.GONE);
                    recycler_directory.setVisibility(View.VISIBLE);
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray data_array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data_array.length(); i++) {
                            try {
                                JSONObject user_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + user_object.getString("user_id"));
                             //   My_Preference.set_premium_lawyer(getActivity(), user_object.getString("basic_premium"));

                                directory_model.add(new Directory_Model(user_object.getString("user_id"),
                                        user_object.getString("user_name"),
                                        user_object.getString("user_email"),
                                        user_object.getString("user_phone"),
                                        user_object.getString("user_address"),
                                        user_object.getString("user_fullname"),
                                        user_object.getString("user_avatar"),
                                        user_object.getString("user_type"),
                                        user_object.getString("user_website"),
                                        user_object.getString("basic_premium"),
                                        user_object.getString("user_status"),
                                        user_object.getString("is_online")
                                        ,user_object.getString("user_updated_at"),
                                        user_object.getString("user_verify_token")
                                        ,user_object.getString("user_created_at"),
                                        user_object.getString("user_avatar_url"),
                                        user_object.getString("user_description")));
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                 directory_adapter.notifyItemChanged(i);
                            }
                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        tv_data_not_found.setTypeface(Navigation_activity.typeface);
                        tv_data_not_found.setVisibility(View.VISIBLE);
                       // recycler_directory.setVisibility(View.GONE);
                        tv_data_not_found.setText(message);
                        progressBar_directory.setVisibility(View.GONE);
                       // Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_directory.setVisibility(View.GONE);
            }
        });


        ////pagination
        nested_directory_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                Log.e("isloading", "loadscroll===" + page);
                                progressBar_bottom_directory.setVisibility(View.VISIBLE);

                                PerformPagination(text,page);
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });


    }

    private void PerformPagination(String text, final int page) {


        String serched_text=text;
        Log.e("pagina_83",""+serched_text);
        Log.e("page_273",""+page);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_list;
        String country_id ="";
        if(serched_text.equalsIgnoreCase("null")==true || serched_text==null)
        {
            progressBar_bottom_directory.setVisibility(View.VISIBLE);
            Log.e("derecotyr",""+serched_text);
            serched_text = "";

        }
        if(My_Preference.getCountry_name(getActivity()).equalsIgnoreCase("1")==false) {
            progressBar_bottom_directory.setVisibility(View.VISIBLE);

            Log.e("cou_pagination",""+My_Preference.getCountry_name(getActivity()));
            country_id = My_Preference.getCountry_name(getActivity());
        }else {
            Log.e("country_pagination",""+My_Preference.getCountry_name(getActivity()));

            country_id = My_Preference.getCountry_name(getActivity());

        }

        user_list = api.get_User_list(String.valueOf(page),ApiClient.PER_PAGE,ApiClient.user_type,ApiClient.user_status,serched_text,country_id);

        user_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_pagination","page="+page + response.body().toString());
                progressBar_directory.setVisibility(View.GONE);
                progressBar_bottom_directory.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    tv_data_not_found.setVisibility(View.GONE);
                    recycler_directory.setVisibility(View.VISIBLE);

                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_pagi", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message_pai", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray data_array = jsonObject.getJSONArray("data");
                     //   Toast.makeText(getActivity(), "pageee"+String.valueOf(page), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject user_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + user_object.getString("user_id"));
                                directory_model.add(new Directory_Model(user_object.getString("user_id"),
                                        user_object.getString("user_name"),
                                        user_object.getString("user_email"),
                                        user_object.getString("user_phone"),
                                        user_object.getString("user_address"),
                                        user_object.getString("user_fullname"),
                                        user_object.getString("user_avatar"),
                                        user_object.getString("user_type"),
                                        user_object.getString("user_website"),
                                        user_object.getString("basic_premium"),
                                        user_object.getString("user_status"),
                                        user_object.getString("is_online")
                                        ,user_object.getString("user_updated_at"),
                                        user_object.getString("user_verify_token")
                                        ,user_object.getString("user_created_at"),
                                        user_object.getString("user_avatar_url"),
                                        user_object.getString("user_description")));
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                directory_adapter.notifyItemChanged(i);
                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                       // tv_data_not_found.setTypeface(Navigation_activity.typeface);
                        progressBar_directory.setVisibility(View.GONE);
                        progressBar_bottom_directory.setVisibility(View.GONE);

                      //  tv_data_not_found.setVisibility(View.VISIBLE);
                     //   recycler_directory.setVisibility(View.GONE);
                      //  tv_data_not_found.setText(message);
                      //  Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar_directory.setVisibility(View.GONE);
                progressBar_bottom_directory.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AllocateMemory(View view) {
        recycler_directory = (RecyclerView) view.findViewById(R.id.recycler_directory);
        progressBar_directory = view.findViewById(R.id.progressBar_directory);
        progressBar_bottom_directory = view.findViewById(R.id.progressBar_bottom_directory);
        searchView = (SearchView) view.findViewById(R.id.search); // inititate a search view
        tv_data_not_found = (TextView) view.findViewById(R.id.tv_data_not_found); // inititate a search view
        nested_directory_scroll = (NestedScrollView) view.findViewById(R.id.nested_directory_scroll); // inititate a search view
        lv_directory_parent = (LinearLayout) view.findViewById(R.id.lv_directory_parent); // inititate a search view

        tv_data_not_found.setTypeface(Navigation_activity.typeface);


        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchView.findViewById(id);
        searchText.setTypeface(Navigation_activity.typeface);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        Log.e("serch_text",""+text);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            directory_model.clear();
            CALL_Directory_API(text, String.valueOf(page_no));
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
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
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }
}
