package com.trendyol.stock.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stock {
    private String id;
    private String itemId;
    private int quantity;
}
