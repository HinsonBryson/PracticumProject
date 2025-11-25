package com.shashi.service.impl;

import com.shashi.beans.ProductBean;
import com.shashi.service.DiscountStrategy;

public class ProductDiscountStrategyImpl implements DiscountStrategy {

    @Override
    public double apply(ProductBean product, int quantity) {

        double pct = product.getPdiscount();   // percent (0–100)
        double price = product.getProdPrice();

        return (price * quantity) * (pct / 100.0);
    }
}