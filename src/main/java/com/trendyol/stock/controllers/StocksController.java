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
@RequestMapping("/carts")
public class StocksController {

    private final StockService stockService;
    private final RestService restService;

    public StocksController(StockService stockService, RestService restService) {
        this.restService = restService;
        this.stockService = stockService;
    }


}
