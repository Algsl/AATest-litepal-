package com.example.aatest.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;
import com.example.aatest.adapter.AdapterAccountBook;
import com.example.aatest.adapter.AdapterCategory;
import com.example.aatest.adapter.AdapterUser;
import com.example.aatest.entity.Account;
import com.example.aatest.entity.Category;
import com.example.aatest.entity.Payout;
import com.example.aatest.entity.User;
import com.example.aatest.utility.DateTools;
import com.example.aatest.utility.RegexTools;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityRecord extends ActivityBase implements View.OnClickListener{
    private Payout payout;
    private Integer accountBookId,categoryId;
    private String payoutTypeArr[],payoutUserId;

    private AutoCompleteTextView actvCategoryName;
    private EditText etAccountBookName,etAmount,etPayoutDate,etPayoutType,etPayoutUser,etComment;
    private Button btnSave,btnCancel,btnSelectCategory,btnSelectUser,btnSelectAccountBook,
            btnSelectAmount,btnSelectPayoutDate,btnSelectPayoutType;

    private Account account;
    private List<RelativeLayout> itemColor;
    private List<User> userSelectList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.payout_add_or_edit);
        removeBottomBox();
        initView();
        initVariable();
        bindData();
        setRecordTitle();
        initListeners();
    }

    private void initView() {
        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnSelectAccountBook = (Button)findViewById(R.id.btnSelectAccountBook);
        btnSelectAmount = (Button)findViewById(R.id.btnSelectAmount);
        btnSelectCategory = (Button)findViewById(R.id.btnSelectCategory);
        btnSelectPayoutDate = (Button)findViewById(R.id.btnSelectPayoutDate);
        btnSelectPayoutType = (Button)findViewById(R.id.btnSelectPayoutType);
        btnSelectUser = (Button)findViewById(R.id.btnSelectUser);
        etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
        etPayoutDate = (EditText) findViewById(R.id.etPayoutDate);
        etPayoutType = (EditText) findViewById(R.id.etPayoutType);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
        actvCategoryName = (AutoCompleteTextView) findViewById(R.id.actvCategoryName);
        etPayoutUser = (EditText) findViewById(R.id.etPayoutUser);
        etComment = (EditText) findViewById(R.id.etComment);
    }

    private void initVariable() {
        account=new Account();
        payout= (Payout) getIntent().getSerializableExtra("payout");
        //account= DataSupport.findFirst(Account.class);
    }

    private void bindData() {
        accountBookId=account.getAccountId();
        etAccountBookName.setText(account.getAccountBookName());
        actvCategoryName.setAdapter(null);
        etPayoutDate.setText(DateTools.getFormatDateTime(new Date(),"yyyy-MM-dd"));
        payoutTypeArr=getResources().getStringArray(R.array.PayoutType);
        etPayoutType.setText(payoutTypeArr[0]);
    }

    private void setRecordTitle() {
        String title;
        if(payout==null){
            title=getString(R.string.ActivityTitlePayoutAddOrEdit,new Object[]{"新建"});
        }else{
            title=getString(R.string.ActivityTitlePayoutAddOrEdit,new Object[]{"修改"});
        }
        setTopBarTitle(title);
    }

    private void initListeners() {
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSelectAmount.setOnClickListener(this);
        btnSelectCategory.setOnClickListener(this);
        btnSelectPayoutDate.setOnClickListener(this);
        btnSelectPayoutType.setOnClickListener(this);
        btnSelectUser.setOnClickListener(this);
        actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
        btnSelectAccountBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectAccountBook:
                showAccountBookSelectDialog();
                break;
            case R.id.btnSelectAmount:
                break;
            case R.id.btnSelectCategory:
                showCategorySelectDialog();
                break;
            case R.id.btnSelectPayoutDate:
                Calendar calendar = Calendar.getInstance();
                showDateSelectDialog(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                break;
            case R.id.btnSelectPayoutType:
                showPayoutTypeSelectDialog();
                break;
            case R.id.btnSelectUser:
                showUserSelectDialog(etPayoutType.getText().toString());
                break;
            case R.id.btnSave:
                addOrEditPayout();
                break;
            case R.id.btnCancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void addOrEditPayout() {
        Boolean result=checkData();
        if(!result){
            return;
        }
        if(payout==null){
            payout=new Payout();
        }
        payout.setAccountBookId(accountBookId);
        payout.setCategoryId(categoryId);
        payout.setAmount(Float.parseFloat(etAmount.getText().toString().trim()));
        payout.setPayoutDate(DateTools.getDate(etPayoutDate.getText().toString().trim(),"yyyy-MM-dd"));
        payout.setPayoutType(etPayoutType.getText().toString().trim());
        payout.setPayUserId(payoutUserId);
        payout.setComment(etComment.getText().toString().trim());
        if(payout.getPayoutId()==0){
            payout.save();
        }else{
            payout.update(payout.getPayoutId());
        }
        finish();
    }

    private Boolean checkData() {
        boolean result= RegexTools.IsMoney(etAmount.getText().toString().trim());
        if(!result){
            etAmount.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextMoney), Toast.LENGTH_SHORT).show();
            return result;
        }
        result=RegexTools.IsNull(categoryId);
        if(!result){
            btnSelectCategory.setFocusable(true);
            btnSelectCategory.setFocusableInTouchMode(true);
            btnSelectCategory.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextCategoryIsNull), Toast.LENGTH_SHORT).show();
            return result;
        }
        if(payoutUserId==null){
            btnSelectUser.setFocusable(true);
            btnSelectUser.setFocusableInTouchMode(true);
            btnSelectUser.requestFocus();
            Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUserIsNull),Toast.LENGTH_SHORT).show();
            return false;
        }
        String payType=etPayoutType.getText().toString();
        if(payType.equals(payoutTypeArr[0]) || payType.equals(payoutTypeArr[1])){
            if(payoutUserId.split(",").length <= 1){
                btnSelectUser.setFocusable(true);
                btnSelectUser.setFocusableInTouchMode(true);
                btnSelectUser.requestFocus();
                Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser), Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            if(payoutUserId.equals("")){
                btnSelectUser.setFocusable(true);
                btnSelectUser.setFocusableInTouchMode(true);
                btnSelectUser.requestFocus();
                Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser2), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void showUserSelectDialog(String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=LayoutInflater.from(this).inflate(R.layout.user,null);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.LinearLayoutMain);
        linearLayout.setBackgroundResource(R.drawable.blue);
        ListView listView=(ListView)view.findViewById(R.id.lvUserList);
        AdapterUser adapterUser=new AdapterUser(this);
        listView.setAdapter(adapterUser);

        builder.setIcon(R.drawable.user_small_icon);
        builder.setTitle("选择消费人");
        builder.setPositiveButton("确定",new OnSelectUserBack());
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
        listView.setOnItemClickListener(new OnUserItemClick(dialog,s));
    }

    private void showPayoutTypeSelectDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.payout_type_select_list,null);
        ListView listView=(ListView)view.findViewById(R.id.ListViewPayoutType);

        builder.setTitle("计算方式");
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
        listView.setOnItemClickListener(new OnPayTypeItemClick(dialog));
    }

    private void showDateSelectDialog(int i, int i1, int i2) {
        (new DatePickerDialog(this, new OnDateSelectedListener(), i,i1, i2)).show();
    }

    private void showCategorySelectDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.category_select_list,null);
        ExpandableListView elistView=(ExpandableListView)view.findViewById(R.id.ExpandableListViewCategory);
        AdapterCategory adapterCategory=new AdapterCategory(this);
        elistView.setAdapter(adapterCategory);
        builder.setIcon(R.drawable.category_small_icon);
        builder.setTitle("选择类别");
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
        elistView.setOnGroupClickListener(new OnGroupClick(dialog,adapterCategory));
        elistView.setOnChildClickListener(new OnChildClick(dialog,adapterCategory));
    }

    private void showAccountBookSelectDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_list,null);
        ListView listView=(ListView)view.findViewById(R.id.ListViewSelect);
        AdapterAccountBook adapter=new AdapterAccountBook(this);
        listView.setAdapter(adapter);
        builder.setTitle("选择账本");
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
        listView.setOnItemClickListener(new OnAccountItemClick(dialog));
    }

    private class OnAccountItemClick implements AdapterView.OnItemClickListener {
        private  AlertDialog alertDialog;
        public OnAccountItemClick(AlertDialog dialog) {
            alertDialog=dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Account account=(Account)(adapterView.getAdapter().getItem(i));
            etAccountBookName.setText(account.getAccountBookName());
            accountBookId=account.getAccountId();
            alertDialog.dismiss();
        }
    }

    private class OnGroupClick implements ExpandableListView.OnGroupClickListener {
        private AlertDialog alertDialog;
        private AdapterCategory adapter;
        public OnGroupClick(AlertDialog dialog, AdapterCategory adapterCategory) {
            alertDialog=dialog;
            adapter=adapterCategory;
        }

        @Override
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            int count=adapter.getChildrenCount(i);
            if(count==0){
                Category category= (Category) adapter.getGroup(i);
                actvCategoryName.setText(category.getCategoryName());
                categoryId=category.getCategoryId();
                alertDialog.dismiss();
            }
            return false;
        }
    }

    private class OnChildClick implements ExpandableListView.OnChildClickListener {
        private AlertDialog alertDialog;
        private AdapterCategory adapter;
        public OnChildClick(AlertDialog dialog, AdapterCategory adapterCategory) {
            alertDialog=dialog;
            adapter=adapterCategory;
        }

        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
            Category category= (Category) adapter.getChild(i,i1);
            actvCategoryName.setText(category.getCategoryName());
            categoryId=category.getCategoryId();
            alertDialog.dismiss();
            return false;
        }
    }

    private class OnDateSelectedListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Date date = new Date(i-1900, i1, i2);
            etPayoutDate.setText(DateTools.getFormatDateTime(date,"yyyy-MM-dd"));
        }
    }

    private class OnSelectUserBack implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            etPayoutUser.setText("");
            String name="";
            payoutUserId="";
            if(userSelectList!=null){
                for(int j=0;j<userSelectList.size();j++){
                    name+=userSelectList.get(j).getUserName()+",";
                    payoutUserId+=userSelectList.get(j).getUserId()+",";
                }
                etPayoutUser.setText(name);
            }
            itemColor=null;
            userSelectList=null;
        }
    }

    private class OnAutoCompleteTextViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Category category= (Category) adapterView.getAdapter().getItem(i);
            categoryId=category.getCategoryId();
        }
    }

    private class OnUserItemClick implements AdapterView.OnItemClickListener {
        private AlertDialog alertDialog;
        private String payType;
        public OnUserItemClick(AlertDialog dialog, String s) {
            alertDialog=dialog;
            payType=s;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String payTypes[]=getResources().getStringArray(R.array.PayoutType);
            User user= (User) adapterView.getAdapter().getItem(i);
            if(payType.equals(payTypes[0]) || payType.equals(payTypes[1])){
                RelativeLayout main=(RelativeLayout)view.findViewById(R.id.RelativeLayoutMain);
                if(itemColor==null && userSelectList==null){
                    itemColor=new ArrayList<RelativeLayout>();
                    userSelectList=new ArrayList<User>();
                }
                if(itemColor.contains(main)){
                    main.setBackgroundResource(R.drawable.blue);
                    itemColor.remove(main);
                    userSelectList.remove(user);
                }else{
                    main.setBackgroundResource(R.drawable.red);
                    itemColor.add(main);
                    userSelectList.add(user);
                }
                return;
            }
            if(payType.equals(payTypes[2])){
                userSelectList=new ArrayList<User>();
                userSelectList.add(user);
                etPayoutUser.setText("");
                String name="";
                payoutUserId="";
                for(int j=0;j<userSelectList.size();j++){
                    name+=userSelectList.get(j).getUserName()+",";
                    payoutUserId+=userSelectList.get(j).getUserId()+",";
                }
                etPayoutUser.setText(name);
                itemColor=null;
                userSelectList=null;
                alertDialog.dismiss();
            }
        }
    }

    private class OnPayTypeItemClick implements AdapterView.OnItemClickListener {
        private AlertDialog alertDialog;
        public OnPayTypeItemClick(AlertDialog dialog) {
            alertDialog=dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String payType=(String)adapterView.getAdapter().getItem(i);
            etPayoutType.setText(payType);
            etPayoutUser.setText("");
            payoutUserId="";
            alertDialog.dismiss();
        }
    }
}
