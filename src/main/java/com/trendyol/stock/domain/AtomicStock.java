package com.trendyol.stock.domain;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class AtomicStock {
    private AtomicInteger quantity;

    public AtomicStock(){

    }

    public int incrementQuantity(){
        return quantity.incrementAndGet();
    }
}
