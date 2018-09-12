package com.example.aatest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterMain;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.User;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;


public class ActivityMain extends ActivityBase implements SlideMenuView.SlideMenuListener{
    Class[] classes={ActivityRecord.class,ActivityQuery.class,ActivityStatistics.class,
            ActivityAccountBook.class,ActivityCategory.class,ActivityUser.class};
    String[] names={"记录消费","查询消费","统计管理","账本管理","类别管理","人员管理"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.main_body);
        GridView gv=(GridView)findViewById(R.id.gvAppGrid);
        AdapterMain adapterMain=new AdapterMain(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String name=(String)adapterView.getItemAtPosition(i);
                String name=(String)adapterView.getAdapter().getItem(i);
                for(int index=0;index<names.length;index++){
                    if(names[index].equals(name)){
                        openActivity(classes[index]);
                    }
                }
            }
        });
        gv.setAdapter(adapterMain);
        createSlideMenu(R.array.SlideMenuActivityMain);
    }

    @Override
    public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
        Toast.makeText(this,slideMenuItem.getTitle()+" "+slideMenuItem.getId(),
                Toast.LENGTH_SHORT).show();
    }
}
