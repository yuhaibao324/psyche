package com.tedu.psyche.entity;

import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/22
 */

public class IndustryModel {
    private String name;
    private Double count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "IndustryModel{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
