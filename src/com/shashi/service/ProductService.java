package com.shashi.service;

import java.io.InputStream;
import java.util.List;

import com.shashi.beans.ProductBean;

public interface ProductService {

    // Add product using fields
    public String addProduct(String prodName, String prodType, String prodInfo,
                             double prodPrice, int prodQuantity, double pdiscount,
                             InputStream prodImage);

    // Add product using ProductBean
    public String addProduct(ProductBean product);

    // Remove a product
    public String removeProduct(String prodId);

    // Update product (with image)
    public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct);

    // Update product (without image)
    public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct);

    // Update only price
    public String updateProductPrice(String prodId, double updatedPrice);

    // Fetch all products
    public List<ProductBean> getAllProducts();

    // Fetch all products by type
    public List<ProductBean> getAllProductsByType(String type);

    // Search products by keyword
    public List<ProductBean> searchAllProducts(String search);

    // Get product image
    public byte[] getImage(String prodId);

    // Get full product details
    public ProductBean getProductDetails(String prodId);

    // Sell N quantity of a product
    public boolean sellNProduct(String prodId, int n);

    // Get price of a product
    public double getProductPrice(String prodId);

    // Get quantity of a product
    public int getProductQuantity(String prodId);
}
