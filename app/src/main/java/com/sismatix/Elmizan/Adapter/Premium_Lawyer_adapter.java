package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.MediaImageSlider;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Fregment.MediaImages;
import com.sismatix.Elmizan.Model.Media_images_model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Premium_Lawyer_adapter extends RecyclerView.Adapter<Premium_Lawyer_adapter.MyViewHolder> {
    private Context context;
    public static List<Media_images_model> models;
    String  old_image,screen;

    public Premium_Lawyer_adapter(Context context, List<Media_images_model> models,String screen) {
        this.context = context;
        this.models = models;
        this.screen = screen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.premium_lawyer_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Media_images_model product_model = models.get(position);

        Glide.with(context).load(product_model.getImages()).into(holder.imageview);
        holder.tv_img.setTypeface(Navigation_activity.typeface);



        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id=Login_preference.getuser_id(context);

                old_image=product_model.getOld_img();
                Log.e("old_img_72",""+old_image);
              //  Toast.makeText(context, old_image+"old_imag=>>"+old_image, Toast.LENGTH_SHORT).show();
                Call_Delete_images_api(old_image,user_id);

                }
        });

        holder.tv_img.setText(product_model.getOld_img());


        holder.lv_prem_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position_pass= String.valueOf(position);
               // Toast.makeText(context, position_pass+" =>>"+product_model.getImages(), Toast.LENGTH_SHORT).show();
                //AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(context, MediaImageSlider.class);
                intent.putExtra("pos", position);
                context.startActivity(intent);
                //activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        /*String old_img=product_model.getOld_img();
        Log.e("image_67old",""+old_img);
*/

        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {

            if (My_Preference.get_premium_lawyer(context).equals("premium") == true) {

                if(screen.equalsIgnoreCase("media_images")==true) {
                    holder.delete_img.setVisibility(View.VISIBLE);
                }else {
                    holder.delete_img.setVisibility(View.GONE);

                }
            }else {
                holder.delete_img.setVisibility(View.GONE);
            }
        } else {
            holder.delete_img.setVisibility(View.GONE);
        }



    }

    private void Call_Delete_images_api(String old_img, final String user_id) {
        Log.e("old_img_pass", "" + old_img);
       // Toast.makeText(context, "oldimage_pass"+old_img, Toast.LENGTH_SHORT).show();

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> register = apii.Call_delete_api(user_id,old_img,"");
        Log.e("old_img_pass", "" + old_img);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("response_del_img", "" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    Log.e("jb_delete_173", "" + jsonObject);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {

                        MediaImages.CALL_GET_MEDIAIMAGES_API(user_id);
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();


                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView tv_category_name ;
        LinearLayout lv_prem_images;
        ImageView imageview,delete_img;
        TextView tv_img;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView)view.findViewById(R.id.imageview);
            delete_img = (ImageView)view.findViewById(R.id.delete_img);
            lv_prem_images = (LinearLayout) view.findViewById(R.id.lv_prem_images);
            tv_img = (TextView) view.findViewById(R.id.tv_img);
        }
    }
}