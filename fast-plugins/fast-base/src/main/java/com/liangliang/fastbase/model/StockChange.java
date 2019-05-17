package com.liangliang.fastbase.model;

import lombok.Builder;
import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/16
 */
@Data
@Builder
public class StockChange {
    private String code;
    private String name;
    private double avgChange;
    private double maxChange;
    private double minChange;
    private double midChange;
}
