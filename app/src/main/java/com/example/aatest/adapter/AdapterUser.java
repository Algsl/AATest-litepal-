package com.example.aatest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.adapter.base.AdapterBase;
import com.example.aatest.entity.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AdapterUser extends AdapterBase{
    public AdapterUser(Context context) {
        super(context, null);
        List list=DataSupport.findAll(User.class);
        setList(list);
    }
    private class Holder{
        TextView userName;
        ImageView userIcon;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.user_list_item,null);
            holder=new Holder();
            holder.userIcon=(ImageView)view.findViewById(R.id.ivUserIcon);
            holder.userName=(TextView)view.findViewById(R.id.tvUserName);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        User user= (User) getList().get(i);
        holder.userIcon.setImageResource(R.drawable.user_small_icon);
        holder.userName.setText(user.getUserName());
        return view;
    }
}
