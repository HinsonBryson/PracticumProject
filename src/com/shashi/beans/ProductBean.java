package com.shashi.beans;

import java.io.InputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductBean implements Serializable {

    private String prodId;
    private String prodName;
    private String prodType;
    private String prodInfo;
    private double prodPrice;
    private int prodQuantity;
    private double pdiscount;   // product-only discount
    private InputStream prodImage;

    public ProductBean() {}

    public ProductBean(String prodId, String prodName, String prodType, String prodInfo,
                       double prodPrice, int prodQuantity, double pdiscount, InputStream prodImage) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodType = prodType;
        this.prodInfo = prodInfo;
        this.prodPrice = prodPrice;
        this.prodQuantity = prodQuantity;
        this.pdiscount = pdiscount;
        this.prodImage = prodImage;
    }

    public String getProdId() { return prodId; }
    public void setProdId(String prodId) { this.prodId = prodId; }

    public String getProdName() { return prodName; }
    public void setProdName(String prodName) { this.prodName = prodName; }

    public String getProdType() { return prodType; }
    public void setProdType(String prodType) { this.prodType = prodType; }

    public String getProdInfo() { return prodInfo; }
    public void setProdInfo(String prodInfo) { this.prodInfo = prodInfo; }

    public double getProdPrice() { return prodPrice; }
    public void setProdPrice(double prodPrice) { this.prodPrice = prodPrice; }

    public int getProdQuantity() { return prodQuantity; }
    public void setProdQuantity(int prodQuantity) { this.prodQuantity = prodQuantity; }

    public double getPdiscount() { return pdiscount; }
    public void setPdiscount(double pdiscount) { this.pdiscount = pdiscount; }

    public InputStream getProdImage() { return prodImage; }
    public void setProdImage(InputStream prodImage) { this.prodImage = prodImage; }
}
