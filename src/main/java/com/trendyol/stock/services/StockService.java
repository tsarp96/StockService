package com.trendyol.stock.services;

import com.trendyol.stock.domain.Stock;
import com.trendyol.stock.repositories.StocksRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StocksRepository stocksRepository;

    public StockService(StocksRepository stocksRepository) {
        this.stocksRepository = stocksRepository;
    }

    public void createStock(Stock stock) {
        stocksRepository.insert(stock);
    }

    public List<Stock> getStockByProductId(String productId) {
        return stocksRepository.getStockByItemID(productId);
    }

    public void changeQuantityByProductId(String productId, int quantity) {
        stocksRepository.changeQuantityByProductId(productId, quantity);
    }
    public void deleteStock(String id){
        stocksRepository.deleteById(id);
    }

    public void changeQuantityById(String stockId, int quantity) {
        stocksRepository.changeQuantityById(stockId,quantity);
    }
}
