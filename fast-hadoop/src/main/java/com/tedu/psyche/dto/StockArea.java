package com.tedu.psyche.dto;

import lombok.Builder;
import lombok.Data;

/**
 * describe:
 *
 * @author liang
 * @date 2019/05/05
 */
@Builder
@Data
public class StockArea {
    private String area;
    private int count;

}
