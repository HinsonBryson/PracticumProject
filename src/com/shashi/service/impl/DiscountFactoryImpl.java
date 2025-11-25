package com.shashi.service.impl;

import com.shashi.beans.ProductBean;
import com.shashi.service.DiscountFactory;
import com.shashi.service.DiscountStrategy;

public class DiscountFactoryImpl implements DiscountFactory {

    @Override
    public DiscountStrategy getStrategy(ProductBean product) {

        // If product discount > 0, use product-level discount strategy
        if (product.getPdiscount() > 0) {
            return new ProductDiscountStrategyImpl();
        }

        // If no discount, use no-discount strategy
        return new NoDiscountStrategyImpl();
    }
}