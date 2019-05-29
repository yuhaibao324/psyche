package com.tedu.psyche.data.es.repository;


import com.tedu.psyche.data.es.model.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/23
 */
@Repository
public interface StockEsRepository extends ElasticsearchRepository<Stock,String> {
}
