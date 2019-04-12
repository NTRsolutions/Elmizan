package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sismatix.Elmizan.Activity.Navigation_activity;

import com.sismatix.Elmizan.Fregment.Final_Library_fregment;
import com.sismatix.Elmizan.Model.Category_Model;
import com.sismatix.Elmizan.R;

import java.util.List;


public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.MyViewHolder> {

    private List<Category_Model> model;
    private Context context;
    LayoutInflater inflater;
    public static String name, image;
    int selectedposition = -1;
    int pageno = 1;
    private int selectedItem;

    public static  String category_id;


    public Category_Adapter(Context context, List<Category_Model> model) {
        this.context = context;
        this.model = model;
        selectedItem = 0;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.category_row, parent, false);
        return new Category_Adapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Category_Model category_model = model.get(position);

        holder.tv_category_title.setTypeface(Navigation_activity.typeface);
        Log.e("cate_id_78", "" + category_model.getCategory_id());
        category_id=model.get(0).getCategory_id();

        Navigation_activity.Check_String_NULL_Value(holder.tv_category_title, category_model.getCategory_title());

        /*if (selectedItem == position) {
            Log.e("cate_id_888", "" + category_model.getCategory_id());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_category_title.setTextColor(context.getColor(R.color.colorPrimary));
            }
            Final_Library_fregment.CALL_LIBRARY_LIST_API(category_model.getCategory_id(), pageno);
            holder.category_view_line.setVisibility(View.VISIBLE);
        }*/

        //  holder.tv_country.setText(rmd.getCountry_name());

        holder.lv_category_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cate_id_61", "" + category_model.getCategory_id());
                // Toast.makeText(context, "category="+category_model.getCategory_id(), Toast.LENGTH_SHORT).show();

                Final_Library_fregment.CALL_LIBRARY_LIST_API(category_model.getCategory_id(), pageno);

                selectedposition = position;
                notifyDataSetChanged();

            }
        });

        if (selectedposition == position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_category_title.setTextColor(context.getColor(R.color.colorPrimary));
            }

            holder.category_view_line.setVisibility(View.VISIBLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_category_title.setTextColor(context.getColor(R.color.bottom_icon));
            }
            holder.category_view_line.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_category_title;
        public ImageView ivflag;
        LinearLayout lv_category_click;
        View category_view_line;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_category_title = (TextView) itemView.findViewById(R.id.tv_category_title);
            ivflag = (ImageView) itemView.findViewById(R.id.iv_flag);
            lv_category_click = (LinearLayout) itemView.findViewById(R.id.lv_category_click);
            category_view_line = (View) itemView.findViewById(R.id.category_view_line);

        }
    }
}




