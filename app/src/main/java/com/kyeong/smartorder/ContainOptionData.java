package com.kyeong.smartorder;

public class ContainOptionData {

    String opName, opPrice, opCount;

    public ContainOptionData(String opName, String opPrice, String opCount) {
        this.opName = opName;
        this.opPrice = opPrice;
        this.opCount = opCount;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpPrice() {
        return opPrice;
    }

    public void setOpPrice(String opPrice) {
        this.opPrice = opPrice;
    }

    public String getOpCount() {
        return opCount;
    }

    public void setOpCount(String opCount) {
        this.opCount = opCount;
    }
}
