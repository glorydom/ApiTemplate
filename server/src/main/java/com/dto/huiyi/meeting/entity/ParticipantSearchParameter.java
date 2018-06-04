package com.dto.huiyi.meeting.entity;

import java.io.Serializable;

public class ParticipantSearchParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String telephone;
    private String company;

    private int pageIndex;
    private int amount;  //分页

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
