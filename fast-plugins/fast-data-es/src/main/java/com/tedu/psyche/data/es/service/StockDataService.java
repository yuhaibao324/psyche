package com.tedu.psyche.data.es.service;

import com.tedu.psyche.data.es.model.Stock;
import com.tedu.psyche.data.es.repository.StockEsRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/23
 */
@Service
public class StockDataService {
    @Autowired
    private StockEsRepository repository;

    public long count() {
        return repository.count();
    }

    public Stock save(Stock stock){
        return repository.save(stock);
    }

    public List<Stock> getByCode(String ts_code){
        List<Stock> list = new ArrayList<>();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("ts_code", ts_code);
        Iterable<Stock> iterable = repository.search(matchQueryBuilder);
        iterable.forEach(e->list.add(e));
        return list;
    }

}
