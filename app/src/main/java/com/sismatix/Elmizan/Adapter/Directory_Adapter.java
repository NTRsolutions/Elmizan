package com.sismatix.Elmizan.Adapter;

import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.Elmizan.Activity.Chat_activity;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Fregment.Article_freg;
import com.sismatix.Elmizan.Fregment.Directory_of_lawyers_freg;
import com.sismatix.Elmizan.Fregment.Login_freg;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Fregment.Premimum_Lawyer_freg;
import com.sismatix.Elmizan.Model.Directory_Model;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Directory_Adapter extends RecyclerView.Adapter<Directory_Adapter.MyViewHolder> {
    private Context context;
    private List<Directory_Model> models;
    String login_flag;

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
        lang_arbi();
        holder.call.setTypeface(Navigation_activity.typeface);
        holder.chat.setTypeface(Navigation_activity.typeface);
        holder.article.setTypeface(Navigation_activity.typeface);
        holder.msg.setTypeface(Navigation_activity.typeface);
        holder.more.setTypeface(Navigation_activity.typeface);
        holder.tv_directory_user_name.setTypeface(Navigation_activity.typeface);
        holder.tv_directory_appeal.setTypeface(Navigation_activity.typeface);
        holder.tv_directory_user_name.setText(Navigation_activity.Convert_String_First_Letter(directory_model.getUser_firstname()));

        Navigation_activity.Check_String_NULL_Value(holder.tv_directory_appeal, directory_model.getUser_description());
        login_flag = Login_preference.getLogin_flag(context);
       // Log.e("login_flagg", "" + login_flag);
        Log.e("userid_82", "" + directory_model.getUser_id());
        // My_Preference.set_premium_lawyer(context, directory_model.getBasic_premium());
        // holder.tv_directory_offline_onnline.setText(directory_model.getNews_detail());
        //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.my_profile);
        requestOptions.error(R.drawable.my_profile);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(directory_model.getUser_avatar_url()).into(holder.iv_directory_profile_image);

      //  Log.e("from_user_id", "" + Login_preference.getuser_id(context));

        holder.lv_directory_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {
                    Intent i = new Intent(context, Chat_activity.class);
                    Log.e("user_99", "" + models.get(position).getUser_id());
                    i.putExtra("user_id", models.get(position).getUser_id());
                    context.startActivity(i);
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out);
                    Fragment newFragment = new Login_freg();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack(null).commit();
                }
                /*Intent intent = new Intent(context, Chat_activity.class);
                context.startActivity(intent);*/

            }
        });

        ///       Glide.with(context).load(directory_model.getUser_avatar_url()).into(holder.iv_directory_profile_image);

        holder.lv_directory_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Uri uri = Uri.parse("smsto:" + "");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                context.startActivity(intent);*/
                /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent i = new Intent(context, Chat_activity.class);
                Log.e("user_99", "" + models.get(position).getUser_id());
                i.putExtra("user_id", models.get(position).getUser_id());
                activity.startActivity(i);*/
                if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {
                    Intent i = new Intent(context, Chat_activity.class);
                    Log.e("user_99", "" + models.get(position).getUser_id());
                    i.putExtra("user_id", models.get(position).getUser_id());
                    context.startActivity(i);
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out);
                    Fragment newFragment = new Login_freg();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack(null).commit();
                }
                /*Intent i = new Intent(context, Chat_activity.class);
                Log.e("user_99", "" + models.get(position).getUser_id());
                i.putExtra("user_id", models.get(position).getUser_id());
                context.startActivity(i);*/

            }
        });

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {
                    Intent i = new Intent(context, Chat_activity.class);
                    Log.e("user_99", "" + models.get(position).getUser_id());
                    i.putExtra("user_id", models.get(position).getUser_id());
                    context.startActivity(i);
                } else {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out);
                    Fragment newFragment = new Login_freg();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack(null).commit();
                }

               /* Intent i = new Intent(context, Chat_activity.class);
                Log.e("user_99", "" + models.get(position).getUser_id());
                i.putExtra("user_id", models.get(position).getUser_id());
                context.startActivity(i);*/
               /* Uri uri = Uri.parse("smsto:" + "");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                context.startActivity(intent);*/
            }
        });

        if (directory_model.getBasic_premium().equalsIgnoreCase("premium") == true) {

            if (directory_model.getIs_online().equalsIgnoreCase("0") == true) {
                holder.lv_directory_chat.setVisibility(View.GONE);
                holder.lv_directory_send_msg.setVisibility(View.VISIBLE);
                holder.tv_directory_offline_onnline.setText(context.getResources().getString(R.string.offline));
                holder.tv_directory_offline_onnline.setTextColor(context.getResources().getColor(R.color.red));
                holder.tv_directory_circle.setTextColor(context.getResources().getColor(R.color.red));

            } else if (directory_model.getIs_online().equalsIgnoreCase("1") == true) {

                holder.tv_directory_offline_onnline.setText(context.getResources().getString(R.string.online));
                holder.tv_directory_offline_onnline.setTextColor(context.getResources().getColor(R.color.green));
                holder.tv_directory_circle.setTextColor(context.getResources().getColor(R.color.green));
                holder.lv_directory_chat.setVisibility(View.VISIBLE);
                holder.lv_directory_send_msg.setVisibility(View.GONE);

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_directory_premimum_logo.setImageDrawable(context.getDrawable(R.drawable.menu_img));
                holder.lv_directory_more.setVisibility(View.VISIBLE);
                holder.lv_directory_article.setVisibility(View.VISIBLE);
                holder.lv_directory_call.setVisibility(View.VISIBLE);
                //  holder.lv_directory_chat.setVisibility(View.VISIBLE);
                // holder.lv_directory_send_msg.setVisibility(View.VISIBLE);

            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_directory_premimum_logo.setImageDrawable(context.getDrawable(R.drawable.grey_perimimum));
                holder.lv_directory_more.setVisibility(View.VISIBLE);
                holder.lv_directory_article.setVisibility(View.GONE);
                holder.lv_directory_call.setVisibility(View.GONE);
                holder.lv_directory_chat.setVisibility(View.GONE);
                holder.lv_directory_send_msg.setVisibility(View.GONE);
                holder.tv_directory_offline_onnline.setVisibility(View.GONE);
                holder.tv_directory_circle.setVisibility(View.GONE);

            }
        }

        holder.lv_directory_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Log.e("phone", "" + directory_model.getUser_phone());

                        if (directory_model.getUser_phone() == "" || directory_model.getUser_phone() == null || directory_model.getUser_phone() == "null" || directory_model.getUser_phone().equalsIgnoreCase(null)
                                || directory_model.getUser_phone().equalsIgnoreCase("null")) {
                            Log.e("phone", "" + directory_model.getUser_phone());
                        } else {
                            Log.e("phone_104", "" + directory_model.getUser_phone());
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            i.setData(Uri.parse("tel:" + directory_model.getUser_phone()));
                            activity.startActivity(i);
                        }

                    }
                }, 50);
            }
        });

        holder.lv_directory_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (directory_model.getBasic_premium().equalsIgnoreCase("basic") == true) {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Directory_of_lawyers_freg();
                            Bundle b = new Bundle();
                            b.putString("user_id", directory_model.getUser_id());
                            // b.putString("name",product_model.getCategory_name());
                            Log.e("user_id_131", "" + directory_model.getUser_id());
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().
                                    replace(R.id.main_fram_layout, myFragment).addToBackStack("directorylawyer").commit();
                        } else {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Premimum_Lawyer_freg();
                            Bundle b = new Bundle();
                            b.putString("user_id", directory_model.getUser_id());
                            Log.e("user_id_140", "" + directory_model.getUser_id());
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        }
                    }
                }, 1000);

            }
        });

        holder.lv_directory_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Log.e("phone", "" + directory_model.getUser_phone());

                        Fragment myFragment = new Article_freg();
                        Bundle b = new Bundle();
                        b.putString("user_id", directory_model.getUser_id());
                        // b.putString("name",product_model.getCategory_name());
                        Log.e("user_id_131", "" + directory_model.getUser_id());
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        // getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    }
                }, 50);
            }
        });
        holder.lv_directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (directory_model.getBasic_premium().equalsIgnoreCase("basic") == true) { /*Bundle b=new Bundle();
                        b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        *///Log.e("categotyidd",""+product_model.getValue());
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Directory_of_lawyers_freg();
                            Bundle b = new Bundle();
                            b.putString("user_id", directory_model.getUser_id());
                            // b.putString("name",product_model.getCategory_name());
                            Log.e("to_user_id", "" + directory_model.getUser_id());
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack("directorylawyer").commit();

                        } else {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            Fragment myFragment = new Premimum_Lawyer_freg();
                            Bundle b = new Bundle();
                            b.putString("user_id", directory_model.getUser_id());
                            // b.putString("name",product_model.getCategory_name());
                            Log.e("user_id_140", "" + directory_model.getUser_id());
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        }
                    }
                }, 1000);
            }
        });

    }

    private void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    /*public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        models.clear();
        if (charText.length() == 0) {
            models.addAll(arraylist);
        } else {
            for (Directory_Model wp : arraylist) {
                if (wp.getUser_firstname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    models.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView call, chat, article, msg, more;
        TextView tv_directory_user_name, tv_directory_appeal, tv_directory_offline_onnline, tv_directory_circle;
        ImageView iv_directory_premimum_logo;
        CircleImageView iv_directory_profile_image;
        LinearLayout lv_directory, lv_directory_more, lv_directory_send_msg, lv_directory_article, lv_directory_chat, lv_directory_call;

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

            call = (TextView) view.findViewById(R.id.tv_directory_call);
            chat = (TextView) view.findViewById(R.id.tv_directory_chat);
            article = (TextView) view.findViewById(R.id.tv_directory_article);
            msg = (TextView) view.findViewById(R.id.tv_directory_send_msg);
            more = (TextView) view.findViewById(R.id.tv_directory_more);

            /*lv_directory_send_msg.setEnabled(false);
            lv_directory_chat.setEnabled(false);*/
        }
    }


}


