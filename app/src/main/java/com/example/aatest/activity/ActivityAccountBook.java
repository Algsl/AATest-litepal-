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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterAccountBook;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.Account;
import com.example.aatest.utility.RegexTools;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ActivityAccountBook extends ActivityBase implements SlideMenuView.SlideMenuListener{
    private Account account;
    private ListView accountList;
    private AdapterAccountBook adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.account_book);
        accountList=(ListView)findViewById(R.id.lvAccountBookList);
        bindData();
        registerForContextMenu(accountList);
        createSlideMenu(R.array.SlideMenuAccountBook);
    }

    private void bindData() {
        adapter=new AdapterAccountBook(this);
        accountList.setAdapter(adapter);
        setAccountBookTitle();
    }

    private void setAccountBookTitle() {
        int count=adapter.getCount();
        String title=getString(R.string.ActivityTitleAccountBook,new Object[]{count});
        setTopBarTitle(title);
    }

    @Override
    public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
        SlideMenuToggle();
        if(slideMenuItem.getId()==0){
            showAddOrEditDialog(null);
        }
    }

    private void showAddOrEditDialog(Account pAccount) {
        View view=getLayoutInflater().inflate(R.layout.account_book_add_or_edit,null);
        EditText etAccountName=(EditText)view.findViewById(R.id.etAccountBookName);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.chkIsDefault);
        String title;
        if(pAccount==null){
            title=getString(R.string.DialogTitleAccountBook,new Object[]{"新建"});
        }else{
            etAccountName.setText(pAccount.getAccountBookName());
            title=getString(R.string.DialogTitleAccountBook,new Object[]{"修改"});
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setPositiveButton("保存",new onAddOrEditClickListener(pAccount,etAccountName,checkBox,true))
                .setNegativeButton("取消",new onAddOrEditClickListener(pAccount,etAccountName,checkBox,false))
                .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo= (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter=accountList.getAdapter();
        account= (Account) listAdapter.getItem(adapterContextMenuInfo.position);
        menu.setHeaderTitle(account.getAccountBookName());
        menu.setHeaderIcon(R.drawable.account_book_small_icon);
        createMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                showAddOrEditDialog(account);
                break;
            case 2:
                deleteAccount();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteAccount() {
        String message=getString(R.string.DialogMessageAccountBookDelete,new Object[]{account.getAccountBookName()});
        showAlertDialog(R.string.DialogTitleDelete, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataSupport.deleteAll(Account.class,"accountBookName=?",account.getAccountBookName());
                bindData();
            }
        });
    }

    private class onAddOrEditClickListener implements DialogInterface.OnClickListener {
        private Account account;
        private EditText eAccountName;
        private CheckBox isDefault;
        private boolean isSave;
        public onAddOrEditClickListener(Account pAccount, EditText etAccountName, CheckBox checkBox, boolean b) {
            account=pAccount;
            eAccountName=etAccountName;
            isDefault=checkBox;
            isSave=b;
        }
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String accountName=eAccountName.getText().toString().trim();
            if(!isSave){
                setAlertDialogClose(dialogInterface,true);
            }else{
                if(account==null){
                    account=new Account();
                }
                if(isDefault.isChecked()){
                    List<Account> accounts= DataSupport.where("isDefault=?","1").find(Account.class);
                    for(Account account:accounts){
                        account.setIsDefault(0);
                        account.save();
                    }
                    account.setIsDefault(1);
                    account.save();
                }
                if(checkAccountName(accountName,dialogInterface)){
                    account.setAccountBookName(accountName);
                    if(account.getAccountId()==0){
                        account.save();
                    }else {
                        account.update(account.getAccountId());
                    }
                }
                bindData();
            }
        }

        private boolean checkAccountName(String accountName, DialogInterface dialogInterface) {
            boolean result=true;
            List<Account> accounts= DataSupport.findAll(Account.class);
            for(Account a:accounts){
                if(accountName.equals(a.getAccountBookName())){
                    Toast.makeText(getApplicationContext(),"该账本已存在/已设置为默认账本",Toast.LENGTH_SHORT).show();
                    setAlertDialogClose(dialogInterface,false);
                    result=false;
                    break;
                }
            }
            if(!RegexTools.IsChineseEnglishNum(accountName)){
                Toast.makeText(getApplicationContext(),getString(
                        R.string.CheckDataTextChineseEnglishNum,new Object[]{eAccountName.getHint()}),
                        Toast.LENGTH_SHORT).show();
                setAlertDialogClose(dialogInterface,false);
                result=false;
            }
            return result;
        }
    }
}
