package com.example.aatest.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class CategoryTotal extends DataSupport implements Serializable{
    public String count;
    public String sumAmount;
    public String categoryName;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
