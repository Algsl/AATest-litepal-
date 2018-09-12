package com.example.aatest.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Account extends DataSupport{
    private int id;
    private String accountBookName;
    private Date createDate=new Date();
    private int state=1;
    private int isDefault;

    public int getAccountId() {
        return id;
    }

    public void setAccountId(int id) {
        this.id = id;
    }

    public String getAccountBookName() {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
