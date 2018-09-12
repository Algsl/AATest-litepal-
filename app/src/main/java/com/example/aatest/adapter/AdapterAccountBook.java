package com.example.aatest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.adapter.base.AdapterBase;
import com.example.aatest.entity.Account;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.List;

public class AdapterAccountBook extends AdapterBase{
    public AdapterAccountBook(Context context) {
        super(context,null);
        List list= DataSupport.findAll(Account.class);
        setList(list);
    }
    private class Holder{
        TextView accountName;
        ImageView accountImage;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.account_book_list_item,null);
            holder=new Holder();
            holder.accountName=(TextView)view.findViewById(R.id.tvAccountBookName);
            holder.accountImage=(ImageView)view.findViewById(R.id.ivAccountBookIcon);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        Account account= (Account) getList().get(i);
        holder.accountName.setText(account.getAccountBookName());
        if(account.getIsDefault()==1){
            holder.accountImage.setImageResource(R.drawable.account_book_default);
        }else{
            holder.accountImage.setImageResource(R.drawable.account_book_small_icon);
        }
        return view;
    }
}
