package com.example.aatest.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterAccountBook;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.Account;
import com.example.aatest.entity.Payout;
import com.example.aatest.entity.User;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.List;

public class ActivityStatistics extends ActivityBase implements SlideMenuView.SlideMenuListener{
    private TextView tvStatistics;
    private Account account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.statistics);
        tvStatistics=(TextView)findViewById(R.id.tvStatisticsResult);
        account= (Account) DataSupport.where("isDefault=1").find(Account.class).get(0);
        setStatisticsTitle();
        bindData();
        createSlideMenu(R.array.SlideMenuStatistics);
    }

    private void bindData() {
        new BindDataThread().start();
    }

    private void setStatisticsTitle() {
        String title=getString(R.string.ActivityTitleStatistics, new Object[]{account.getAccountBookName()});
        setTopBarTitle(title);
    }
    public String getResult(int id){
        String result="";
        List<Payout> payouts= DataSupport.where("accountBookId=?",id+"").find(Payout.class);
        for(int i=0;i<payouts.size();i++){
            Payout payout=payouts.get(i);
            if(payout.getPayoutType().equals("个人")){
                result+=DataSupport.find(User.class, Long.parseLong(payout.getPayUserId().split(",")[0])).getUserName()+"：个人消费 "+payout.getAmount()+"元\n";
            }else if(payout.getPayoutType().equals("均分")){

            }
        }
        return result;
    }

    @Override
    public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
        SlideMenuToggle();
        if (slideMenuItem.getId() == 0) {
            showAccountBookSelectDialog();
        }
    }

    private void showAccountBookSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        ListView listView = (ListView)view.findViewById(R.id.ListViewSelect);
        AdapterAccountBook adapter = new AdapterAccountBook(this);
        listView.setAdapter(adapter);

        builder.setTitle(R.string.ButtonTextSelectAccountBook);
        builder.setNegativeButton(R.string.ButtonTextBack, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        listView.setOnItemClickListener(new ActivityStatistics.OnAccountBookItemClickListener(dialog));
    }

    private class BindDataThread extends Thread{
        @Override
        public void run() {
            String result=getResult(1);
            Message msg=new Message();
            msg.obj=result;
            msg.what=1;
            handler.sendMessage(msg);
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String result=(String)msg.obj;
                    tvStatistics.setText(result);
                    break;
                default:
                    break;
            }
        }
    };

    private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dialog;
        public OnAccountBookItemClickListener(AlertDialog alertDialog) {
            dialog=alertDialog;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            account=(Account)adapterView.getAdapter().getItem(i);
            bindData();
            dialog.dismiss();
        }
    }
}
