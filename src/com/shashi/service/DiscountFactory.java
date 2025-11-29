package com.shashi.service;

import com.shashi.beans.ProductBean;

public interface DiscountFactory {

    /**
     * Returns the discount strategy based on product and optional coupon code.
     */
    DiscountStrategy getStrategy(ProductBean product);
}