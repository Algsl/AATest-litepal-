package com.example.aatest.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

public class Category extends DataSupport implements Serializable{
    private int id;
    private String categoryName;
    private String typeFlag;
    private int parentId=0;
    private String path;
    private Date createDate=new Date();
    private int state=1;

    public int getCategoryId() {
        return id;
    }

    public void setCategoryId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @Override
    public String toString() {
        return categoryName;
    }
}
