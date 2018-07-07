package com.huiyi.dao;

import java.io.Serializable;
import java.util.Date;

public class JCI_ORDER_HOTEL implements Serializable {

    private static final long serialVersionUID = 1L;

    private int ID;
    private String YY;
    private String NO;
    private String FX;
    private int NUM;
    private Date RZSJ;
    private Date LKSJ;
    private String ST;
    private Date RE_DATE;
    private String JDNO;
    private String TZXMA;
    private String TZXMB;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getYY() {
        return YY;
    }

    public void setYY(String YY) {
        this.YY = YY;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getFX() {
        return FX;
    }

    public void setFX(String FX) {
        this.FX = FX;
    }

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public Date getRZSJ() {
        return RZSJ;
    }

    public void setRZSJ(Date RZSJ) {
        this.RZSJ = RZSJ;
    }

    public Date getLKSJ() {
        return LKSJ;
    }

    public void setLKSJ(Date LKSJ) {
        this.LKSJ = LKSJ;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public Date getRE_DATE() {
        return RE_DATE;
    }

    public void setRE_DATE(Date RE_DATE) {
        this.RE_DATE = RE_DATE;
    }

    public String getJDNO() {
        return JDNO;
    }

    public void setJDNO(String JDNO) {
        this.JDNO = JDNO;
    }

    public String getTZXMA() {
        return TZXMA;
    }

    public void setTZXMA(String TZXMA) {
        this.TZXMA = TZXMA;
    }

    public String getTZXMB() {
        return TZXMB;
    }

    public void setTZXMB(String TZXMB) {
        this.TZXMB = TZXMB;
    }
}
