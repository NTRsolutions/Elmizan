package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Fregment.Directory_of_lawyers_freg;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Fregment.Premimum_Lawyer_freg;
import com.sismatix.Elmizan.Model.Directory_Model;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Directory_Adapter extends RecyclerView.Adapter<Directory_Adapter.MyViewHolder> {
    private Context context;
    private List<Directory_Model> models;


    public Directory_Adapter(Context context, List<Directory_Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public Directory_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_row, parent, false);

        return new Directory_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Directory_Model directory_model = models.get(position);
            holder.tv_directory_user_name.setText(directory_model.getUser_firstname()+" "+directory_model.getUser_lastname());
           // holder.tv_directory_appeal.setText(directory_model.getNews_title());
           // holder.tv_directory_offline_onnline.setText(directory_model.getNews_detail());
        //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
           Glide.with(context).load(directory_model.getUser_avatar_url()).into(holder.iv_directory_profile_image);

           if(directory_model.getBasic_premium().equalsIgnoreCase("premium")==true){
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   holder.iv_directory_premimum_logo.setImageDrawable(context.getDrawable(R.drawable.menu_img));
                   holder.lv_directory_more.setVisibility(View.VISIBLE);
                   holder.lv_directory_article.setVisibility(View.VISIBLE);
                   holder.lv_directory_call.setVisibility(View.VISIBLE);
                   holder.lv_directory_chat.setVisibility(View.VISIBLE);
                   holder.lv_directory_send_msg.setVisibility(View.VISIBLE);

               }
           }else {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   holder.iv_directory_premimum_logo.setImageDrawable(context.getDrawable(R.drawable.grey_perimimum));
                   holder.lv_directory_more.setVisibility(View.VISIBLE);
                   holder.lv_directory_article.setVisibility(View.GONE);
                   holder.lv_directory_call.setVisibility(View.GONE);
                   holder.lv_directory_chat.setVisibility(View.GONE);
                   holder.lv_directory_send_msg.setVisibility(View.GONE);

               }
           }
            holder.lv_directory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                        if(directory_model.getBasic_premium().equalsIgnoreCase("basic")==true)
                        { /*Bundle b=new Bundle();
                        b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        *///Log.e("categotyidd",""+product_model.getValue());
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Directory_of_lawyers_freg();
                            //myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();


                        }else {
                             /*Bundle b=new Bundle();
                        b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        *///Log.e("categotyidd",""+product_model.getValue());
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Premimum_Lawyer_freg();
                            //myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        }
                          }
                    }, 1000);
                }
            });


    }


    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_directory_user_name, tv_directory_appeal, tv_directory_offline_onnline,tv_directory_circle;
        ImageView iv_directory_premimum_logo;
        CircleImageView iv_directory_profile_image;
        LinearLayout lv_directory,lv_directory_more,lv_directory_send_msg,lv_directory_article,lv_directory_chat,lv_directory_call;

        public MyViewHolder(View view) {
            super(view);
            lv_directory = (LinearLayout) view.findViewById(R.id.lv_directory);
            lv_directory_more = (LinearLayout) view.findViewById(R.id.lv_directory_more);
            lv_directory_send_msg = (LinearLayout) view.findViewById(R.id.lv_directory_send_msg);
            lv_directory_article = (LinearLayout) view.findViewById(R.id.lv_directory_article);
            lv_directory_chat = (LinearLayout) view.findViewById(R.id.lv_directory_chat);
            lv_directory_call = (LinearLayout) view.findViewById(R.id.lv_directory_call);

            tv_directory_user_name = (TextView) view.findViewById(R.id.tv_directory_user_name);
            tv_directory_appeal = (TextView) view.findViewById(R.id.tv_directory_appeal);
            tv_directory_offline_onnline = (TextView) view.findViewById(R.id.tv_directory_offline_onnline);
            tv_directory_circle = (TextView) view.findViewById(R.id.tv_directory_circle);
            iv_directory_premimum_logo = (ImageView) view.findViewById(R.id.iv_directory_premimum_logo);
            iv_directory_profile_image = (CircleImageView) view.findViewById(R.id.iv_directory_profile_image);


        }
    }


}


