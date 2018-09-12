package com.example.aatest.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterAccountBook;
import com.example.aatest.adapter.AdapterPayout;
import com.example.aatest.control.SlideMenuItem;
import com.example.aatest.control.SlideMenuView;
import com.example.aatest.entity.Account;
import com.example.aatest.entity.Payout;

public class ActivityQuery extends ActivityBase implements SlideMenuView.SlideMenuListener{
    private ListView payoutList;
    private Payout payout;
    private AdapterPayout adapter;
    private Account account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.payout_list);
        payoutList=(ListView)findViewById(R.id.lvPayoutList);
        bindData();
        createSlideMenu(R.array.SlideMenuPayout);
    }

    private void bindData() {
        adapter=new AdapterPayout(this,1);
        payoutList.setAdapter(adapter);
    }

    @Override
    public void onSlideMenuItemClick(View v, SlideMenuItem slideMenuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        ListView listView = (ListView)view.findViewById(R.id.ListViewSelect);
        AdapterAccountBook adapter = new AdapterAccountBook(this);
        listView.setAdapter(adapter);

        builder.setTitle(R.string.ButtonTextSelectAccountBook);
        builder.setNegativeButton(R.string.ButtonTextBack, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                account=(Account)adapterView.getAdapter().getItem(i);
                bindData();
                dialog.dismiss();
            }
        });
    }
}
