package com.example.aatest.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class AdapterBase extends BaseAdapter{
    private List list;
    private Context context;
    private LayoutInflater layoutInflater;
    public AdapterBase(Context context,List list){
        this.context=context;
        this.list=list;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public List getList() {

        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
