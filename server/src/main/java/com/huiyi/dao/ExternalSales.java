package com.huiyi.dao;

import java.io.Serializable;

public class ExternalSales implements Serializable {

    private static final long serialVersionUID = 1L;

    private String COMPANY;
    private String SALES;

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getSALES() {
        return SALES;
    }

    public void setSALES(String SALES) {
        this.SALES = SALES;
    }
}
