package com.tedu.psyche.data.es.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Describe:
 *
 * @Author liang
 * @Since 2019/05/23
 */
@Data
@Builder
@Document(indexName = "stock")
public class Stock {
    @Id
    private String ts_code;
    private int symbol;
    private String name;
    private String area;
    private String industry;
    private String list_date;
}
