package com.shashi.service;
import com.shashi.beans.ProductBean;
public interface DiscountFactory {

    /**
     * Returns the appropriate discount strategy based on a product.
     */
    DiscountStrategy getStrategy(ProductBean product);
}
