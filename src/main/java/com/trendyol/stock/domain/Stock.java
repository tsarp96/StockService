package com.trendyol.stock.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stock {
    private String id;
    private String item_id;
    private int quantity;
    private String Address;
}
