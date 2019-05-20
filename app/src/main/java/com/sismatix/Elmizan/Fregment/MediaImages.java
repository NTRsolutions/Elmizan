package com.sismatix.Elmizan.Fregment;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.Premium_Lawyer_adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Media_images_model;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
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
public class MediaImages extends Fragment {

    public static RecyclerView recycler_media_images;
    public static List<Media_images_model> premium_lawyer_models = new ArrayList<Media_images_model>();
    public static Premium_Lawyer_adapter premium_lawyer_adapter;
    public static ProgressBar progressBar_images;
    public static TextView tv_media_img_not_found;

    public static String old_video,screen;
    public static Context context=null;
    public MediaImages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_media_images, container, false);

        context=getActivity();
        AllocateMemory(v);
        lang_arbi();
        Login_freg.hideSoftKeyboard(getActivity());
        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_GET_MEDIAIMAGES_API(Media.u_id);


        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        screen="media_images";

        premium_lawyer_adapter = new Premium_Lawyer_adapter(getActivity(), premium_lawyer_models,screen);
        recycler_media_images.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_media_images.setItemAnimator(new DefaultItemAnimator());
        recycler_media_images.setAdapter(premium_lawyer_adapter);
        return v;
    }

    public static void CALL_GET_MEDIAIMAGES_API(String userid) {
        premium_lawyer_models.clear();
        progressBar_images.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.getMedia(userid);
        Log.e("uid", "" +userid);
        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                premium_lawyer_models.clear();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_media", "" + status);
                    if (status.equalsIgnoreCase("success")) {
                        progressBar_images.setVisibility(View.GONE);
                        String msg_en = jsonObject.getString("data");
                        Log.e("data_189", "" + msg_en);

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("media_data", "" + data_obj);


                      /*  ////////////////////////////////old media images/////////////////////////////

                        JSONObject media_obj=data_obj.getJSONObject("arr_user_media_content");
                        Log.e("media_obj",""+media_obj);

                        ///get images....
                        JSONArray jsonArray_image = media_obj.getJSONArray("image");
                        Log.e("json_image_168", "" + jsonArray_image);

                        if (jsonArray_image.equals("[]") == true) {
                            Log.e("jsonarray_blanck", "" + jsonArray_image);
                            recycler_media_images.setVisibility(View.GONE);
                            progressBar_images.setVisibility(View.GONE);
                            tv_media_img_not_found.setVisibility(View.VISIBLE);
                            tv_media_img_not_found.setText(context.getResources().getString(R.string.data_not_found));


                        } else {
                            for (int i = 0; i < jsonArray_image.length(); i++) {
                                try {
                                    String oldimage = jsonArray_image.getString(i);

                                    if (oldimage.equalsIgnoreCase("") == true || oldimage.equalsIgnoreCase("null") == true
                                            || oldimage == null) {
                                        Log.e("image_null", "" + oldimage);
                                    } else {
                                        oldurl_pass_imag = jsonArray_image.getString(i);
                                        Log.e("oldurl_pass_183", "" + oldurl_pass_imag);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }

                            }
                        }*/
                        String old_img = null;

                        JSONObject old_img_obj = data_obj.getJSONObject("arr_user_media_content");
                        JSONArray imag_array_old = old_img_obj.getJSONArray("image");

                        if (imag_array_old != null && imag_array_old.isNull(0) != true) {
                            Log.e("old_imgarr", "" + imag_array_old);

                            for (int i = 0; i < imag_array_old.length(); i++) {
                                try {
                                     old_img = imag_array_old.getString(i);
                                    Log.e("old_imgarr", "" + old_img);

                                    if (old_img == "" || old_img == null || old_img == "null" || old_img.equalsIgnoreCase(null)
                                            || old_img.equalsIgnoreCase("null")) {
                                       // Toast.makeText(context, "data problem", Toast.LENGTH_SHORT).show();
                                    } else {

                                        old_img=imag_array_old.getString(i);
                                        Log.e("old_img_167", "" + old_img);
                                       // premium_lawyer_models.add(new Premium_Lawyer_Model(old_img,oldurl_pass_imag));

                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    premium_lawyer_adapter.notifyItemChanged(i);
                                }
                            }
                        }else {
                            recycler_media_images.setVisibility(View.GONE);
                            progressBar_images.setVisibility(View.GONE);
                            tv_media_img_not_found.setVisibility(View.VISIBLE);
                            tv_media_img_not_found.setText(context.getResources().getString(R.string.data_not_found));

                        }



                        /////////////////////////////////////////////////////

                        String user_media_status = data_obj.getString("user_media_status");
                        Log.e("user_media_status_med", "" + user_media_status);

                        String article_media_url = data_obj.getString("arr_user_media_content_url");
                        Log.e("media_image", "" + article_media_url);

                        JSONObject image_obj = data_obj.getJSONObject("arr_user_media_content_url");
                        Log.e("imgg", "" + image_obj);

                        String image = image_obj.getString("image");
                        Log.e("imageeee", "" + image);

                        JSONArray imag_array = image_obj.getJSONArray("image");

                        if (imag_array != null && imag_array.isNull(0) != true) {
                            Log.e("imag_array", "" + imag_array);

                            for (int j = 0; j < imag_array.length(); j++) {
                                try {
                                    JSONObject object = imag_array.getJSONObject(j);

                                    Log.e("imag_name", "" + object.getString("name"));
                                    premium_lawyer_models.add(new Media_images_model(object.getString("url"),object.getString("name")));



                                    // String imagepass = imag_array.getString(j);
                                    // Log.e("image_prem", "" + imagepass);

                                   /* if (imagepass == "" || imagepass == null || imagepass == "null" || imagepass.equalsIgnoreCase(null)
                                            || imagepass.equalsIgnoreCase("null")) {
                                        Toast.makeText(context, "data problem", Toast.LENGTH_SHORT).show();
                                    } else {

                                        premium_lawyer_models.add(new Media_images_model(imagepass,old_img));

                                    }*/

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    premium_lawyer_adapter.notifyItemChanged(j);
                                }
                            }
                        }else {
                            recycler_media_images.setVisibility(View.GONE);
                            progressBar_images.setVisibility(View.GONE);
                            tv_media_img_not_found.setVisibility(View.VISIBLE);
                            tv_media_img_not_found.setText(context.getResources().getString(R.string.data_not_found));

                        }




                    } else if (status.equalsIgnoreCase("error")) {

                        recycler_media_images.setVisibility(View.GONE);
                        progressBar_images.setVisibility(View.GONE);
                        tv_media_img_not_found.setVisibility(View.VISIBLE);
                        tv_media_img_not_found.setText(context.getResources().getString(R.string.data_not_found));

                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

    private void AllocateMemory(View v) {
        recycler_media_images = (RecyclerView) v.findViewById(R.id.recycler_media_images);
        progressBar_images = (ProgressBar) v.findViewById(R.id.progressBar_images);
        tv_media_img_not_found = (TextView) v.findViewById(R.id.tv_media_img_not_found);
    }

}
