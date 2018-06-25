package com.huiyi.dao;

import com.dto.huiyi.meeting.entity.register.ComparisonResultDto;

import java.io.Serializable;

public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String GSMC;
    private String NSR;
    private String DZ;
    private String DH;
    private String KHH;
    private String ZH;
    private ComparisonResultDto comparisonResultDto;

    public ComparisonResultDto getComparisonResultDto() {
        return comparisonResultDto;
    }

    public void setComparisonResultDto(ComparisonResultDto comparisonResultDto) {
        this.comparisonResultDto = comparisonResultDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGSMC() {
        return GSMC;
    }

    public void setGSMC(String GSMC) {
        this.GSMC = GSMC;
    }

    public String getNSR() {
        return NSR;
    }

    public void setNSR(String NSR) {
        this.NSR = NSR;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public String getDH() {
        return DH;
    }

    public void setDH(String DH) {
        this.DH = DH;
    }

    public String getKHH() {
        return KHH;
    }

    public void setKHH(String KHH) {
        this.KHH = KHH;
    }

    public String getZH() {
        return ZH;
    }

    public void setZH(String ZH) {
        this.ZH = ZH;
    }
}
