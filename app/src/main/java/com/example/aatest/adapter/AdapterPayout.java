package com.example.aatest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aatest.R;
import com.example.aatest.adapter.base.AdapterBase;
import com.example.aatest.entity.Payout;
import com.example.aatest.entity.User;
import com.example.aatest.utility.DateTools;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AdapterPayout extends AdapterBase{
    private class Holder{
        ImageView icon;
        TextView name,total,payoutUserAndType;
        View relativeLayoutDate;
    }
    private int accountBookId;
    public AdapterPayout(Context context, List list) {
        super(context, list);
    }
    public AdapterPayout(Context context,int accountBookId){
        this(context,null);
        this.accountBookId=accountBookId;
        List<Payout> payouts=DataSupport.where("accountBookId=?",accountBookId+"").find(Payout.class);
        setList(payouts);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=getLayoutInflater().inflate(R.layout.payout_list_item,null);
            holder=new Holder();
            holder.icon=(ImageView)view.findViewById(R.id.PayoutIcon);
            holder.name=(TextView)view.findViewById(R.id.PayoutName);
            holder.total=(TextView)view.findViewById(R.id.Total);
            holder.payoutUserAndType=(TextView)view.findViewById(R.id.PayoutUserAndPayoutType);
            holder.relativeLayoutDate=(View)view.findViewById(R.id.RelativeLayoutDate);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        holder.relativeLayoutDate.setVisibility(View.GONE);
        Payout payout=(Payout)getItem(i);
        String payoutDate= DateTools.getFormatShortTime(payout.getPayoutDate());
        boolean isShow =false;
        if(i>0){
            Payout payout1=(Payout)getItem(i);
            String payoutDateLast=DateTools.getFormatShortTime(payout1.getPayoutDate());
            isShow=!payoutDate.equals(payoutDateLast);
        }
        if(isShow || i==0){
            holder.relativeLayoutDate.setVisibility(View.VISIBLE);
            ((TextView)holder.relativeLayoutDate.findViewById(R.id.tvPayoutDate)).setText(payoutDate);
        }
        holder.icon.setImageResource(R.drawable.payout_small_icon);
        holder.total.setText( payout.getAmount()+"");
        holder.name.setText(payout.getCategoryName());
        String[] users=payout.getPayUserId().split(",");
        String userNameString="";
        for(int j=0;j<users.length;j++){
            userNameString+=DataSupport.select("userName").where("id="+users[j]).find(User.class).get(0).getUserName()+",";
        }
        holder.payoutUserAndType.setText(userNameString+" "+payout.getPayoutType());
        return view;
    }
}
