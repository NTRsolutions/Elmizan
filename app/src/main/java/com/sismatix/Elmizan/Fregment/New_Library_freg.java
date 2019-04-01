package com.sismatix.Elmizan.Fregment;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.TAb_PAGER_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Librarytablist_model;
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
public class New_Library_freg extends Fragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Librarytablist_model> category_list;
    LinearLayout lv_library;
    ProgressBar progressBar_lib;

    View v;

    public New_Library_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_library_freg, container, false);

        //tab layout
        tabLayout = v.findViewById(R.id.tabs);
        progressBar_lib = v.findViewById(R.id.progressBar_lib);
        lv_library = v.findViewById(R.id.lv_library);
        //view pager
        viewPager = v.findViewById(R.id.viewpager);
        category_list = new ArrayList<>();

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_CATEGORYLIST_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

        return v;
    }

    private void CALL_CATEGORYLIST_API() {
        progressBar_lib.setVisibility(View.VISIBLE);
        lv_library.setVisibility(View.GONE);
        category_list.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> category_list_api = api.get_category_list();

        category_list_api.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_category", "" + response.body().toString());
                //   progressBar_home.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {

                    progressBar_lib.setVisibility(View.GONE);
                    lv_library.setVisibility(View.VISIBLE);
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

                                Librarytablist_model model = new Librarytablist_model(category_object.getString("category_id"), category_object.getString("category_title"));
                                category_list.add(model);


                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                            }

                        }
                        setUpViewPager();
                        tabLayout.setupWithViewPager(viewPager);
                     //   viewPager.setCurrentItem(0);

                        //setUpCustomTabs();

                    } else if (status.equalsIgnoreCase("error")) {
                        progressBar_lib.setVisibility(View.GONE);

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


    private void setUpCustomTabs() {
        for (int i = 0; i < category_list.size(); i++) {

            tabLayout.addTab(tabLayout.newTab().setText(category_list.get(i).getCategory_title()));

          /*  TextView customTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.livrary_tab, null);//get custom view
              customTab.setText(category_list.get(i).getCategory_title());//set text over view
             customTab.setTextColor(getResources().getColorStateList(R.color.black));
            TabLayout.Tab tab = tabLayout.getTabAt(i);//get tab via position
            if (tab != null)
                tab.setCustomView(customTab);//set custom view
        }*/
        }
    }

    private void setUpViewPager() {
        TAb_PAGER_Adapter adapter = new TAb_PAGER_Adapter(getChildFragmentManager());
        New_dynamic_Library_frag fView;
        for (int i = 0; i < category_list.size(); i++) {
            fView = new New_dynamic_Library_frag();
            adapter.addFrag(fView, category_list.get(i));
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(category_list.size());

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

   /* class ViewPagerAdapter extends FragmentPagerAdapter {
        New_dynamic_Library_frag frag;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Librarytablist_model> mFragmentTitleList = new ArrayList<>();
        private ViewPager viewPager;
        boolean visible;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return New_dynamic_Library_frag.newInstance(
                    mFragmentTitleList.get(position).getCategory_id(), visible,
                    mFragmentTitleList.get(position).getCategory_title());
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, Librarytablist_model title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).getCategory_title();
        }
    }
*/
}
