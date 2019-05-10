package com.tedu.psyche.entity;

/**
 * Describe:
 * 统计结果
 * @Author liang
 * @Since 2019/05/08
 */

public class StatisticsRecord {
    private int id;
    private int type;
    private String name;
    private Double value;
    public StatisticsRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "StatisticsRecord{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
