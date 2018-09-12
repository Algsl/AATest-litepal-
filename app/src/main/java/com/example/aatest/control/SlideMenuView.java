package com.example.aatest.control;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.adapter.AdapterSlideMenu;

import java.util.ArrayList;
import java.util.List;

public class SlideMenuView {
    private int h;
    private List list;
    private Activity activity;
    private boolean isClose;
    private RelativeLayout layout;
    private SlideMenuListener slideMenuListener;

    public interface SlideMenuListener{
        public void onSlideMenuItemClick(View view,SlideMenuItem slideMenuItem);
    }
   public SlideMenuView(final Activity activity){
        this.activity=activity;
        layout=(RelativeLayout) activity.findViewById(R.id.IncludeBottom);
        if(activity instanceof SlideMenuListener){
            slideMenuListener=(SlideMenuListener)activity;
            list=new ArrayList();
            isClose=true;
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toggle();
            }
        });
   }

    public void Toggle() {
        if(isClose){
            //Toast.makeText(activity,"开启菜单",Toast.LENGTH_SHORT).show();
            openMenu();
        }else{
            //Toast.makeText(activity,"关闭菜单",Toast.LENGTH_SHORT).show();
            closeMenu();
        }
    }

    private void openMenu() {
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.IncludeTitle);
        layout.setLayoutParams(layoutParams);
        isClose=false;
    }

    private void closeMenu() {
        RelativeLayout rlayout=(RelativeLayout)activity.findViewById(R.id.layBottomBar);
        h=rlayout.getLayoutParams().height;
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.setLayoutParams(layoutParams);
        isClose=true;
    }

    public void addMenuItem(SlideMenuItem slideMenuItem){
        list.add(slideMenuItem);
    }
    public void bindList(){
        ListView lv=(ListView)activity.findViewById(R.id.lvSlideList);
        AdapterSlideMenu adapter=new AdapterSlideMenu(activity,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               SlideMenuItem item= (SlideMenuItem) adapterView.getAdapter().getItem(i);
               slideMenuListener.onSlideMenuItemClick(view,item);
            }
        });
    }
    public void removeBottomBox(){
        RelativeLayout layout=activity.findViewById(R.id.layMainLayout);
        layout.removeView(this.layout);
        this.layout=null;
    }
}
