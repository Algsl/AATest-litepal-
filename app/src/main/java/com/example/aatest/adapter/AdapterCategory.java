package com.example.aatest.adapter;

import android.content.Context;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.entity.Category;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategory extends BaseExpandableListAdapter{
    private class GroupHolder{
        TextView name;
        TextView count;
    }
    private class ChildHolder{
        TextView name;
    }
    private Context context;
    private List groupList,childList;

    public AdapterCategory(Context pContext){
        context=pContext;
        groupList= DataSupport.where("parentId=0 and state=1")
                .find(Category.class);
        childList=new ArrayList();
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder gHolder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.category_group_list_item,null);
            gHolder=new GroupHolder();
            gHolder.name=(TextView)view.findViewById(R.id.tvCategoryName);
            gHolder.count=(TextView)view.findViewById(R.id.tvCount);
            view.setTag(gHolder);
        }else{
            gHolder= (GroupHolder) view.getTag();
        }
        Category category= (Category) groupList.get(i);
        gHolder.name.setText(category.getCategoryName());
        int count=DataSupport.where("parentId=? and state=1",
                category.getCategoryId()+"").find(Category.class).size();
        gHolder.count.setText(context.getString(R.string.TextViewTextChildrenCategory,new Object[]{count}));
        childList.add(count);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getChildrenCount(int i) {
        return (int) childList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        Category category= (Category) getGroup(i);
        List<Category> list=DataSupport.where("parentId=? and state=1",
                category.getCategoryId()+"").find(Category.class);
        return list.get(i1);
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder childHolder;
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.category_children_list_item,null);
            childHolder=new ChildHolder();
            childHolder.name=(TextView)view.findViewById(R.id.tvCategoryName);
            view.setTag(childHolder);
        }else{
            childHolder= (ChildHolder) view.getTag();
        }
        Category category= (Category) getChild(i,i1);
        childHolder.name.setText(category.getCategoryName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
