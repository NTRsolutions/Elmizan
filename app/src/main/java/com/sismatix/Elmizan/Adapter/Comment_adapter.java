package com.sismatix.Elmizan.Adapter;

import android.content.Context;
import android.os.Build;
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
import com.sismatix.Elmizan.Model.Comment_Model;
import com.sismatix.Elmizan.R;

import java.util.List;


public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.MyViewHolder> {

    private List<Comment_Model> model;
    private Context context;
    LayoutInflater inflater;
    public static String name, image;
    int selectedposition = 0;
    int pageno = 1;
    private int selectedItem;


    public Comment_adapter(Context context, List<Comment_Model> model) {
        this.context = context;
        this.model = model;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Comment_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.comment_row, parent, false);

        return new Comment_adapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(Comment_adapter.MyViewHolder holder, final int position) {

        final Comment_Model comment_model = model.get(position);

        holder.tv_username_comment.setTypeface(Navigation_activity.typeface);
        holder.tv_comment.setTypeface(Navigation_activity.typeface);
        holder.tv_date_comment.setTypeface(Navigation_activity.typeface);

        Navigation_activity.Check_String_NULL_Value(holder.tv_username_comment, comment_model.getComment());
        Navigation_activity.Check_String_NULL_Value(holder.tv_date_comment, comment_model.getTime());
        Navigation_activity.Check_String_NULL_Value(holder.tv_comment, comment_model.getName());




    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_username_comment,tv_date_comment,tv_comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_username_comment = (TextView) itemView.findViewById(R.id.tv_username_comment);
            tv_date_comment = (TextView) itemView.findViewById(R.id.tv_date_comment);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);

        }
    }
}






