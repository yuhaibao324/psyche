package com.tedu.psyche.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/21
 */

public class IndustryData {
    private String name;
    private Double value;
    private Double percent;

    public IndustryData() {
    }

    public IndustryData(String name, Double value, Double percent) {
        this.name = name;
        this.value = value;
        this.percent = percent;
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

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
