package com.liangliang.fastbase.entity;

import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/15
 */
@Data
public class Stock {
    private String date;
    private double open;
    private double high;
    private double close;
    private double volume;
    private double price_change;
    private double p_change;
    private double ma5;
    private double ma10;
    private double ma20;
    private double v_ma5;
    private double v_ma10;
    private double v_ma20;
}
