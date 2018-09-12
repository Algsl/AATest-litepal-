package com.example.aatest.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.entity.Category;
import com.example.aatest.utility.RegexTools;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ActivityCategoryAddOrEdit extends ActivityBase implements View.OnClickListener{
    private Button btnSave,btnCancel;
    private Category category;
    private Spinner parentId;
    private EditText etCategoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category_add_or_edit);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        etCategoryName=(EditText)findViewById(R.id.etCategoryName);
        parentId=(Spinner)findViewById(R.id.SpinnerParentID);
        category= (Category) getIntent().getSerializableExtra("category");
        bindData();
        setCategoryAddOrEditTitle();

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    private void bindData() {
        List list= DataSupport.where("parentId=0 and state=1").find(Category.class);
        list.add(0,"--请选择--");
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<Category> categoryAdapter=arrayAdapter;
        parentId.setAdapter(categoryAdapter);
    }

    private void setCategoryAddOrEditTitle() {
        String title;
        if(category==null){
            title=getString(R.string.ActivityTitleCategoryAddOrEdit,new Object[]{"新建"});
        }else{
            title=getString(R.string.ActivityTitleCategoryAddOrEdit,new Object[]{"修改"});
            initData(category);
        }
        setTopBarTitle(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                addOrEditCategory();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void addOrEditCategory() {
        String categoryName=etCategoryName.getText().toString().trim();
        boolean checkResult= RegexTools.IsChineseEnglishNum(categoryName);
        if(!checkResult) {
            Toast.makeText(this, getString(R.string.CheckDataTextChineseEnglishNum,
                    new Object[]{R.string.TextViewTextCategoryName}), Toast.LENGTH_SHORT).show();
            return;
        }
        if(category==null){
            category=new Category();
            category.setTypeFlag(getString(R.string.PayoutTypeFlag));
            category.setPath("");
        }
        category.setCategoryName(categoryName);
        if(!parentId.getSelectedItem().toString().equals("--请选择--")){
            Category category= (Category) parentId.getSelectedItem();
            if(category!=null){
                this.category.setParentId(category.getCategoryId());
                category.save();
            }
        }else{
            category.setParentId(0);
        }

        if(category.getCategoryId()==0){
            category.save();
        }else{
            category.update(category.getCategoryId());
        }
        finish();
    }
    private void initData(Category pCategory){
        etCategoryName.setText(pCategory.getCategoryName());
        ArrayAdapter adapter= (ArrayAdapter) parentId.getAdapter();
        if(category.getParentId()!=0){
            int position=0;
            for(int i=1;i<adapter.getCount();i++){
                Category category= (Category) adapter.getItem(i);
                if(category.getCategoryId()==pCategory.getParentId()){
                    position=adapter.getPosition(category);
                }
            }
            parentId.setSelection(position);
        }else{
            int count=DataSupport.where("parentId=? and state=1",
                    pCategory.getCategoryId()+"").find(Category.class).size();
            if(count!=0){
                parentId.setEnabled(false);
            }
        }
    }
}
