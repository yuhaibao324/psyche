package com.liangliang.psyche;

import java.io.Serializable;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/07
 */
public class Record implements Serializable {
    String department;
    String designation;
    String costToCompany;
    String state;

    public Record(String department, String designation, String costToCompany, String state) {
        this.department = department;
        this.designation = designation;
        this.costToCompany = costToCompany;
        this.state = state;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCostToCompany() {
        return costToCompany;
    }

    public void setCostToCompany(String costToCompany) {
        this.costToCompany = costToCompany;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "Record{" +
                "department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", costToCompany='" + costToCompany + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}