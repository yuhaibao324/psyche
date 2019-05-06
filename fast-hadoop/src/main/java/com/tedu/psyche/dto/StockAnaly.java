package com.tedu.psyche.dto;


/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/05
 */
public class StockAnaly {
    private String name;
    private Double value;

    public StockAnaly(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
