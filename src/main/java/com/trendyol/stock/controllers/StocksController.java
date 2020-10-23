package com.trendyol.stock.controllers;

import com.trendyol.stock.domain.Stock;
import com.trendyol.stock.services.StockService;
import com.trendyol.stock.services.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products/")
public class StocksController {

    private final StockService stockService;
    private final RestService restService;

    public StocksController(StockService stockService, RestService restService) {
        this.restService = restService;
        this.stockService = stockService;
    }

    @PostMapping("/{productId}/stocks")
    public ResponseEntity<Void> createStock(@RequestBody Stock stock, @PathVariable String productId){
        stockService.createStock(stock);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(stock.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{productId}/stocks")
    public ResponseEntity<List<Stock>> getStockByProductId(@PathVariable String productId){
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @PatchMapping("/{productId}/stocks")
    public ResponseEntity<List<Stock>> changeStockByProductId(@PathVariable String productId, @RequestParam(name = "quantity") int quantity){
        stockService.changeQuantityByProductId(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/stocks/{stockId}")
    public ResponseEntity<List<Stock>> changeStockByProductId(@RequestParam(name = "quantity") int quantity,
                                                              @PathVariable String stockId){
        stockService.changeQuantityById(stockId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping ("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable ("id") String id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

}
