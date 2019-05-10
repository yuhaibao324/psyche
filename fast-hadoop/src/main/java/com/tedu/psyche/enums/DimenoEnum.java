package com.tedu.psyche.enums;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonCreator;

import java.util.Map;

/**
 * Describe:
 *  统计维度
 * @Author liang
 * @Since 2019/05/08
 */
public enum DimenoEnum {
    INDUSTRY(1,"行业"),LOCATION(2,"位置"),PRICE_AMOUNT(3,"价格位置");
    private int type;
    private String name;

    DimenoEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
    private static Map<Integer,DimenoEnum> map = Maps.newHashMapWithExpectedSize(4);

    static {
        for(DimenoEnum statisticsType:DimenoEnum.values()){
            map.put(statisticsType.type,statisticsType);
        }
    }
    @JsonCreator
    public static DimenoEnum valueoOf(int value){
        DimenoEnum statisticsType = map.get(value);
        return statisticsType;
    }

}
