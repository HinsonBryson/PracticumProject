package com.shashi.service.impl;

import com.shashi.beans.ProductBean;
import com.shashi.service.DiscountStrategy;

public class NoDiscountStrategyImpl implements DiscountStrategy {

    @Override
    public double apply(ProductBean product, int quantity) {
        return 0.0;
    }
}