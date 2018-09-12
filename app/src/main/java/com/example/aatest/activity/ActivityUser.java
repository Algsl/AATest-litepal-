package com.example.aatest.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterUser;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.User;
import com.example.aatest.utility.RegexTools;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ActivityUser extends ActivityBase implements SlideMenuView.SlideMenuListener{
    private AdapterUser adapter;
    private ListView userList;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.user);
        userList=(ListView)findViewById(R.id.lvUserList);
        bindData();
        registerForContextMenu(userList);
        createSlideMenu(R.array.SlideMenuUser);
    }
    private void bindData() {
        adapter=new AdapterUser(this);
        userList.setAdapter(adapter);
        setUserTitle();
    }

    private void setUserTitle() {
        int count=adapter.getCount();
        String title=getString(R.string.ActivityTitleUser,new Object[]{count});
        setTopBarTitle(title);
    }

    @Override
    public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
        SlideMenuToggle();
        if(slideMenuItem.getId()==0){
            showUserAddOrEditDialog(null);
        }
    }

    private void showUserAddOrEditDialog(User pUser) {
        View view=getLayoutInflater().inflate(R.layout.user_add_or_edit,null);
        EditText etUserName=(EditText)view.findViewById(R.id.etUserName);
        if(pUser!=null){
            etUserName.setText(pUser.getUserName());
        }
        String title;
        if(pUser==null){
            title=getString(R.string.DialogTitleUser,new Object[]{getString(R.string.TitleAdd)});
        }else{
            title=getString(R.string.DialogTitleUser,new Object[]{getString(R.string.TitleEdit)});
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.drawable.user_small_icon)
                .setNeutralButton("保存",new onAddOrEdit(pUser,etUserName,true))
                .setNegativeButton("取消",new onAddOrEdit(pUser,etUserName,false))
                .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo= (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter=userList.getAdapter();
        user=(User)listAdapter.getItem(adapterContextMenuInfo.position);
        menu.setHeaderIcon(R.drawable.user_small_icon);
        menu.setHeaderTitle(user.getUserName());
        createMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                showUserAddOrEditDialog(user);
                break;
            case 2:
                delete();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void delete(){
        String message=getString(R.string.DialogMessageUserDelete,new Object[]{user.getUserName()});
        showAlertDialog(R.string.DialogTitleDelete, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataSupport.deleteAll(User.class,"userName=?",user.getUserName());
                bindData();
            }
        });
    }
    private class onAddOrEdit implements DialogInterface.OnClickListener {
        private User user;
        private EditText eUserName;
        private boolean isSave;
        public onAddOrEdit(User pUser, EditText userName, boolean b) {
            user=pUser;
            eUserName=userName;
            isSave=b;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String userName=eUserName.getText().toString().trim();
            if(!isSave){
                setAlertDialogClose(dialogInterface,true);
            }else{
                if(user==null){
                    user=new User();
                }
                if(checkUserName(dialogInterface,userName)){
                    user.setUserName(userName);
                    if(user.getUserId()==0){
                        user.save();
                    }else{
                        user.update(user.getUserId());
                    }
                    bindData();
                }
            }
        }

        private boolean checkUserName(DialogInterface dialogInterface,String userName) {
            Boolean result=true;
            List<User> users=DataSupport.findAll(User.class);
            for(User u:users){
                if(userName.equals(u.getUserName())){
                    Toast.makeText(getApplicationContext(),getString(
                            R.string.CheckDataTextUserExist),Toast.LENGTH_SHORT).show();
                    setAlertDialogClose(dialogInterface,false);
                    result=false;
                    break;
                }
            }
            if(!RegexTools.IsChineseEnglishNum(userName)){
                Toast.makeText(getApplicationContext(),getString(
                        R.string.CheckDataTextChineseEnglishNum,new Object[]{eUserName.getHint()}),
                        Toast.LENGTH_SHORT).show();
                setAlertDialogClose(dialogInterface,false);
                result=false;
            }
            return result;
        }
    }
}
