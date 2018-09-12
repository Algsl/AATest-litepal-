package com.example.aatest.activity.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;

import java.lang.reflect.Field;

public class ActivityBase extends Activity{
    private SlideMenuView slideMenuView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView btn=(ImageView)findViewById(R.id.ivAppBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
    public void openActivity(Class<?> pClass){
        Intent i=new Intent(this,pClass);
        startActivity(i);
    }
    public void appendMainBody(int resId){
        LinearLayout layout=(LinearLayout)findViewById(R.id.layMainBody);
        View view= LayoutInflater.from(this).inflate(resId,null);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view,layoutParams);
    }
    public void createSlideMenu(int resId){
        slideMenuView=new SlideMenuView(this);
        String[] menus=getResources().getStringArray(resId);
        for(int i=0;i<menus.length;i++){
            SlideMenuItem item=new SlideMenuItem(i,menus[i]);
            slideMenuView.addMenuItem(item);
        }
        slideMenuView.bindList();
    }
    public void setTopBarTitle(String str){
        TextView title=(TextView)findViewById(R.id.tvTopTitle);
        title.setText(str);
    }
    protected void SlideMenuToggle(){
        slideMenuView.Toggle();
    }
    protected void setAlertDialogClose(DialogInterface pDialog,Boolean isClose){
        try{
            Field field=pDialog.getClass().getSuperclass().getField("mShowing");
            field.setAccessible(true);
            field.set(pDialog,isClose);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void createMenu(Menu menu){
        int item[]={1,2};
        int groupId=0;
        int order=0;
        for(int i=0;i<item.length;i++){
            switch (item[i]){
                case 1:
                    menu.add(groupId,item[i],order,"修改");
                    break;
                case 2:
                    menu.add(groupId,item[i],order,"删除");
                    break;
                default:
                    break;
            }
        }
    }
    protected AlertDialog showAlertDialog(int titleResId,String message,DialogInterface.OnClickListener clickListener) {
        String title = getResources().getString(titleResId);
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("是", clickListener)
                .setNegativeButton("否", null)
                .show();
    }
    public void removeBottomBox(){
        slideMenuView=new SlideMenuView(this);
        slideMenuView.removeBottomBox();
    }
}
