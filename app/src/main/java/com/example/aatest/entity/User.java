package com.example.aatest.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class User extends DataSupport{
    private int id;
    private String userName;
    private Date createDate=new Date();
    private int state=1;


    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
