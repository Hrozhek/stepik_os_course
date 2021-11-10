package com.github.hrozhek.stepikoscourse.week2.data;

public class LogicalAddressModel {

    private final int pageOne;
    private final int pageTwo;
    private final int pageThree;
    private final int pageFour;
    private final int offset;

    public LogicalAddressModel(int pageOne, int pageTwo, int pageThree, int pageFour, int offset) {
        this.pageOne = pageOne;
        this.pageTwo = pageTwo;
        this.pageThree = pageThree;
        this.pageFour = pageFour;
        this.offset = offset;
    }

    public int getPageOne() {
        return pageOne;
    }

    public int getPageTwo() {
        return pageTwo;
    }

    public int getPageThree() {
        return pageThree;
    }

    public int getPageFour() {
        return pageFour;
    }

    public int getOffset() {
        return offset;
    }
}
