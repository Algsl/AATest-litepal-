package com.example.aatest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aatest.R;

public class AdapterMain extends BaseAdapter{
    private Context mContext;

    Integer[] img={R.drawable.grid_payout, R.drawable.grid_bill, R.drawable.grid_report,
            R.drawable.grid_account_book, R.drawable.grid_category, R.drawable.grid_user};
    String[] name=new String[6];

    private class Holder{
        ImageView img;
        TextView name;
    }

    public AdapterMain(Context pContext){
        mContext=pContext;
        name[0] = pContext.getString(R.string.appGridTextPayoutAdd);
        name[1] = pContext.getString(R.string.appGridTextPayoutManage);
        name[2] = pContext.getString(R.string.appGridTextStatisticsManage);
        name[3] = pContext.getString(R.string.appGridTextAccountBookManage);
        name[4] = pContext.getString(R.string.appGridTextCategoryManage);
        name[5] = pContext.getString(R.string.appGridTextUserManage);
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return name[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.main_body_item,null);
            holder=new Holder();
            holder.img=(ImageView)view.findViewById(R.id.ivIcon);
            holder.name=(TextView)view.findViewById(R.id.tvName);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(80,80);
        holder.img.setLayoutParams(layoutParams);
        holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.img.setImageResource(img[i]);
        holder.name.setText(name[i]);
        return view;
    }
}
