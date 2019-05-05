package com.tedu.psyche.dto;


/**
 * describe:
 *
 * @author liang
 * @date 2019/05/05
 */
public class StockArea {
    private String area;
    private int count;

    public StockArea(String area, int count) {
        this.area = area;
        this.count = count;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
