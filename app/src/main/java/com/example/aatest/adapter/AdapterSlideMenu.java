package com.example.aatest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.adapter.base.AdapterBase;
import com.example.aatest.control.SlideMenuItem;

import java.util.List;

public class AdapterSlideMenu extends AdapterBase{

    public AdapterSlideMenu(Context context, List list) {
        super(context, list);
    }
    private class Holder{
        TextView title;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.slidemenu_list_item,null);
            holder=new Holder();
            holder.title=(TextView)view.findViewById(R.id.tvMenuName);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        SlideMenuItem item= (SlideMenuItem) getList().get(i);
        holder.title.setText(item.getTitle());
        return view;
    }
}
