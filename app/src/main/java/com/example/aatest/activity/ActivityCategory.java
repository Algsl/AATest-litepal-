package com.example.aatest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterCategory;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.Category;
import com.example.aatest.entity.CategoryTotal;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ActivityCategory extends ActivityBase implements SlideMenuView.SlideMenuListener{
    private ExpandableListView categoryList;
    private Category category;
    private AdapterCategory adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category);
        categoryList=(ExpandableListView)findViewById(R.id.ExpandableListViewCategory);
        bindData();
        registerForContextMenu(categoryList);
        createSlideMenu(R.array.SlideMenuCategory);
    }

    private void bindData() {
        adapter=new AdapterCategory(this);
        categoryList.setAdapter(adapter);
        setCategoryTitle();
    }

    private void setCategoryTitle() {
        int count= DataSupport.where("state=1").find(Category.class).size();
        String title=getString(R.string.ActivityTitleCategory,new Object[]{count});
        setTopBarTitle(title);
    }

    @Override
    public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
        SlideMenuToggle();
        if(slideMenuItem.getId()==0){
            Intent intent=new Intent(this,ActivityCategoryAddOrEdit.class);
            startActivityForResult(intent,1);
        }else if(slideMenuItem.getId()==1){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bindData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo ecMenuinfo= (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        long position=ecMenuinfo.packedPosition;
        int type=ExpandableListView.getPackedPositionType(position);
        int groupPosition=ExpandableListView.getPackedPositionGroup(position);
        int childPosition=ExpandableListView.getPackedPositionChild(position);
        switch (type){
            case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
                category= (Category) adapter.getGroup(groupPosition);
                break;
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
                category= (Category) adapter.getChild(groupPosition,childPosition);
                break;
            default:
                break;
        }
        menu.setHeaderIcon(R.drawable.category_small_icon);
        if(category!=null){
            menu.setHeaderTitle(category.getCategoryName());
        }
        createMenu(menu);
        if(adapter.getChildrenCount(groupPosition)!=0 && category.getParentId()==0){
            menu.findItem(2).setEnabled(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case 1:
                intent=new Intent(this,ActivityCategoryAddOrEdit.class);
                intent.putExtra("category",category);
                startActivityForResult(intent,1);
                break;
            case 2:
                deleteCategory(category);
                break;
            case 3:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(final Category category) {
        String message=getString(R.string.DialogMessageCategoryDelete,new Object[]{category.getCategoryName()});
        showAlertDialog(R.string.DialogTitleDelete, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category.setState(0);
                category.save();
                bindData();
            }
        });
    }
}
