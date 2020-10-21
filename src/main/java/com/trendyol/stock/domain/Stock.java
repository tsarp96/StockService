package com.trendyol.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Stock {
    private String id;
    private String itemId;
    private int quantity;

    public Stock(){
        this.id= UUID.randomUUID().toString();
    }
}
