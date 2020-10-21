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

    public List<Stock> changeQuantityByProductId(String productId, int quantity) {
        return stocksRepository.changeQuantityByProductId(productId, quantity);
    }
    public void deleteStock(String id){
        stocksRepository.deleteById(id);
    }
}
