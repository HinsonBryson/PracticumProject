package com.shashi.service;
import com.shashi.beans.ProductBean;
public interface DiscountStrategy {
    /**
     * Applies discount logic to a product.
     * @return discount amount in dollars
     */
    double apply(ProductBean product, int quantity);
}
