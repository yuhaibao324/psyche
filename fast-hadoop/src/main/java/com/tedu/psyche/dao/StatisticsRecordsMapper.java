package com.tedu.psyche.dao;

import com.tedu.psyche.entity.StatisticsRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/08
 */
@Mapper
public interface StatisticsRecordsMapper {
    List<StatisticsRecord> list(int type);
    List<StatisticsRecord> listByLimit(int type);
    public void insert(StatisticsRecord record);
    public void batchInsert(List<StatisticsRecord> records);
//    public int sum(int type);
    public double total(int type);
}
