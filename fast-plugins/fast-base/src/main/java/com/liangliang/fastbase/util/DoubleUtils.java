package com.liangliang.fastbase.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/21
 */
public class DoubleUtils {

    public static Double format(Double d,int scale){
        BigDecimal bg = new BigDecimal(d).setScale(scale, RoundingMode.CEILING);
        return bg.doubleValue();
    }

}
