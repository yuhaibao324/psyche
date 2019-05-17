package com.liangliang.fastbase.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Builder
@Data
public class Company {
    @JsonProperty(value = "ts_code")
    private String code;
    private String symbol;
    private String name;
    private String area;
    private String industry;
    @JsonProperty(value = "list_date")
    private String listDate;


}
