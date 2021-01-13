package com.kyeong.smartorder;

public class ContainData {

    String value_n , value_p;
    int value_i;

    String oriName , oriPrice, oriCount;

    public ContainData(String value_n, String value_p, int value_i, String oriName, String oriPrice, String oriCount) {
        this.value_n = value_n;
        this.value_p = value_p;
        this.value_i = value_i;
        this.oriName = oriName;
        this.oriPrice = oriPrice;
        this.oriCount = oriCount;
    }

    public String getOriName() {
        return oriName;
    }

    public void setOriName(String oriName) {
        this.oriName = oriName;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getOriCount() {
        return oriCount;
    }

    public void setOriCount(String oriCount) {
        this.oriCount = oriCount;
    }

    public String getValue_n() {
        return value_n;
    }

    public String getValue_p() {
        return value_p;
    }

    public int getValue_i() {
        return value_i;
    }

    public void setValue_n(String value_n) {
        this.value_n = value_n;
    }

    public void setValue_p(String value_p) {
        this.value_p = value_p;
    }

    public void setValue_i(int value_i) {
        this.value_i = value_i;
    }
}
