package com.trendyol.stock.repositories;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.query.QueryResult;
import com.trendyol.stock.domain.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StocksRepository {

    private final Cluster couchbaseCluster;
    private final Collection stocksCollection;

    public StocksRepository(Cluster couchbaseCluster, Collection stocksCollection) {
        this.couchbaseCluster = couchbaseCluster;
        this.stocksCollection = stocksCollection;
    }

    public void insert(Stock stock) {
        stocksCollection.insert(stock.getId(), stock);
    }

    public void update(Stock stock) {
        stocksCollection.replace(stock.getId(), stock);
    }

    public Stock findById(String id) {
        GetResult getResult = stocksCollection.get(id);
        Stock stock = getResult.contentAs(Stock.class);
        return stock;
    }

    public void deleteById(String stockId){
        String statement = String.format("Delete from StockDB where id = \"%s\"",stockId);
        QueryResult query = couchbaseCluster.query(statement);
    }

    public List<Stock> getStockByItemID(String id) {
        String statement = String.format("Select * from StockDB where itemId = '%s'", id);
        QueryResult query = couchbaseCluster.query(statement);
        return query.rowsAs(Stock.class);
    }
}
