package com.shashi.service.impl;

import com.shashi.beans.ProductBean;
import com.shashi.service.DiscountFactory;
import com.shashi.service.DiscountStrategy;

public class DiscountFactoryImpl implements DiscountFactory {

    @Override
    public DiscountStrategy getStrategy(ProductBean product) {

        // If product-level discount exists
        if (product.getPdiscount() > 0) {
            return new ProductDiscountStrategyImpl();
        }

        // Otherwise fallback
        return new NoDiscountStrategyImpl();
    }
}
