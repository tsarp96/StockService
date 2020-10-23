package com.trendyol.stock.repositories;

import com.couchbase.client.core.error.CasMismatchException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutateInResult;
import com.couchbase.client.java.kv.MutateInSpec;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;
import com.trendyol.stock.domain.AtomicStock;
import com.trendyol.stock.domain.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.couchbase.client.java.kv.ReplaceOptions.replaceOptions;

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
        /*String statement = String.format("Delete from StockDB where id = %s",stockId);
        QueryResult query = couchbaseCluster.query(statement);*/
        stocksCollection.remove(stockId);
    }

    public List<Stock> getStockByItemID(String id) {
        String statement = String.format("Select id, itemId, quantity from StockDB where itemId = \"%s\"", id);
        QueryResult query = couchbaseCluster.query(statement);
        return query.rowsAs(Stock.class);
    }

    public void changeQuantityById(String stockId, int quantity){
        // Get the current document contents
        GetResult getResult = stocksCollection.get(stockId);
        // Change the count on the stock
        JsonObject content = getResult.contentAsObject();
        content.put("quantity", quantity);
        try {
            // Attempt to replace the document with cas
            MutationResult result = stocksCollection.replace(stockId, content, replaceOptions().cas(getResult.cas()));
        } catch (CasMismatchException ex) {
            // continue the loop on cas mismatch to try again
            // note that any other exception will be raised and break the loop as well
        }
    }

    public Stock changeQuantityByProductId(String productId, int quantity){
        String statement = String.format("UPDATE StockDB SET quantity = %d WHERE itemID = %s",quantity,productId);
        QueryResult query = couchbaseCluster.query(statement);
        return query.rowsAs(Stock.class).get(0);
    }

}
