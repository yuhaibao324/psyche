package com.liangliang.fastbase.model;

import lombok.Builder;
import lombok.Data;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/22
 */
@Data
@Builder
public class PriceScatter {
    private String code;
    private String price;
    private String amount;
}
