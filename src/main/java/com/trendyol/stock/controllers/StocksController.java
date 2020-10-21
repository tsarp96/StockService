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
    public ResponseEntity<Void> addStock(@RequestBody Stock stock, @PathVariable String productId){
        stockService.createStock(stock);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("http://localhost:8082/products/"+productId+"/stocks{id}")
                .buildAndExpand(stock.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{productId}/stocks")
    public ResponseEntity<List<Stock>> getStockByProductId(@PathVariable String productId){
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @PatchMapping("/{productId}/stocks")
    public ResponseEntity<List<Stock>> incrementStockByProductId(@PathVariable String productId, @RequestParam(name = "quantity") int quantity){
        return ResponseEntity.ok(stockService.changeQuantityByProductId(productId, quantity));
    }
    @DeleteMapping ("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable ("id") String id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

}
