package com.shashi.service.impl;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.shashi.beans.ProductBean;
import com.shashi.service.ProductService;
import com.shashi.utility.DBUtil;
import com.shashi.utility.IDUtil;

public class ProductServiceImpl implements ProductService {

    @Override
    public String addProduct(String prodName, String prodType, String prodInfo,
                             double prodPrice, int prodQuantity, double pdiscount,
                             InputStream prodImage) {

        String prodId = IDUtil.generateId();
        ProductBean product = new ProductBean(
                prodId, prodName, prodType, prodInfo,
                prodPrice, prodQuantity, pdiscount, prodImage
        );

        return addProduct(product);
    }

    @Override
    public String addProduct(ProductBean product) {
        String status = "Product Registration Failed!";

        if (product.getProdId() == null)
            product.setProdId(IDUtil.generateId());

        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO product(pid, pname, ptype, pinfo, pprice, pquantity, pdiscount, image) " +
                             "VALUES(?,?,?,?,?,?,?,?)")) {

            ps.setString(1, product.getProdId());
            ps.setString(2, product.getProdName());
            ps.setString(3, product.getProdType());
            ps.setString(4, product.getProdInfo());
            ps.setDouble(5, product.getProdPrice());
            ps.setInt(6, product.getProdQuantity());
            ps.setDouble(7, product.getPdiscount());
            ps.setBlob(8, product.getProdImage());

            int k = ps.executeUpdate();
            if (k > 0)
                status = "Product Added Successfully!";

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        }

        return status;
    }

    // --------------------------------------------------------------------
    // REMOVE PRODUCT
    // --------------------------------------------------------------------
    @Override
    public String removeProduct(String prodId) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM product WHERE pid=?")) {

            ps.setString(1, prodId);
            int k = ps.executeUpdate();

            if (k > 0)
                return "Product Removed Successfully!";
            else
                return "Product Not Found!";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // --------------------------------------------------------------------
    // UPDATE PRODUCT (WITH IMAGE)
    // --------------------------------------------------------------------
    @Override
    public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE product SET pname=?, ptype=?, pinfo=?, pprice=?, pquantity=?, pdiscount=?, image=? " +
                             "WHERE pid=?")) {

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setDouble(6, updatedProduct.getPdiscount());
            ps.setBlob(7, updatedProduct.getProdImage());
            ps.setString(8, prevProduct.getProdId());

            if (ps.executeUpdate() > 0)
                return "Product Updated Successfully!";
            else
                return "Product Update Failed!";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // --------------------------------------------------------------------
    // UPDATE PRODUCT (WITHOUT IMAGE)
    // --------------------------------------------------------------------
    @Override
    public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE product SET pname=?, ptype=?, pinfo=?, pprice=?, pquantity=?, pdiscount=? " +
                             "WHERE pid=?")) {

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setDouble(6, updatedProduct.getPdiscount());
            ps.setString(7, prevProductId);

            if (ps.executeUpdate() > 0)
                return "Product Updated Successfully!";
            else
                return "Product Update Failed!";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // --------------------------------------------------------------------
    // UPDATE PRICE ONLY
    // --------------------------------------------------------------------
    @Override
    public String updateProductPrice(String prodId, double updatedPrice) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE product SET pprice=? WHERE pid=?")) {

            ps.setDouble(1, updatedPrice);
            ps.setString(2, prodId);

            return ps.executeUpdate() > 0 ? "Price Updated!" : "Product Not Found!";

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // --------------------------------------------------------------------
    // GET ALL PRODUCTS
    // --------------------------------------------------------------------
    @Override
    public List<ProductBean> getAllProducts() {
        List<ProductBean> list = new ArrayList<>();

        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM product");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setProdId(rs.getString("pid"));
                p.setProdName(rs.getString("pname"));
                p.setProdType(rs.getString("ptype"));
                p.setProdInfo(rs.getString("pinfo"));
                p.setProdPrice(rs.getDouble("pprice"));
                p.setProdQuantity(rs.getInt("pquantity"));
                p.setPdiscount(rs.getDouble("pdiscount"));
                p.setProdImage(rs.getBinaryStream("image"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // --------------------------------------------------------------------
    // GET PRODUCTS BY TYPE
    // --------------------------------------------------------------------
    @Override
    public List<ProductBean> getAllProductsByType(String type) {
        List<ProductBean> list = new ArrayList<>();

        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM product WHERE ptype=?")) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setProdId(rs.getString("pid"));
                p.setProdName(rs.getString("pname"));
                p.setProdType(rs.getString("ptype"));
                p.setProdInfo(rs.getString("pinfo"));
                p.setProdPrice(rs.getDouble("pprice"));
                p.setProdQuantity(rs.getInt("pquantity"));
                p.setPdiscount(rs.getDouble("pdiscount"));
                p.setProdImage(rs.getBinaryStream("image"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --------------------------------------------------------------------
    // SEARCH PRODUCTS
    // --------------------------------------------------------------------
    @Override
    public List<ProductBean> searchAllProducts(String search) {
        List<ProductBean> list = new ArrayList<>();

        String query = "SELECT * FROM product WHERE pname LIKE ? OR ptype LIKE ? OR pinfo LIKE ?";

        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            String like = "%" + search + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setProdId(rs.getString("pid"));
                p.setProdName(rs.getString("pname"));
                p.setProdType(rs.getString("ptype"));
                p.setProdInfo(rs.getString("pinfo"));
                p.setProdPrice(rs.getDouble("pprice"));
                p.setProdQuantity(rs.getInt("pquantity"));
                p.setPdiscount(rs.getDouble("pdiscount"));
                p.setProdImage(rs.getBinaryStream("image"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --------------------------------------------------------------------
    // GET IMAGE
    // --------------------------------------------------------------------
    @Override
    public byte[] getImage(String prodId) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT image FROM product WHERE pid=?")) {

            ps.setString(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Blob blob = rs.getBlob("image");
                return blob != null ? blob.getBytes(1, (int) blob.length()) : null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------------------------------------------------------------------
    // GET PRODUCT DETAILS BY ID
    // --------------------------------------------------------------------
    @Override
    public ProductBean getProductDetails(String prodId) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM product WHERE pid=?")) {

            ps.setString(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductBean p = new ProductBean();
                p.setProdId(rs.getString("pid"));
                p.setProdName(rs.getString("pname"));
                p.setProdType(rs.getString("ptype"));
                p.setProdInfo(rs.getString("pinfo"));
                p.setProdPrice(rs.getDouble("pprice"));
                p.setProdQuantity(rs.getInt("pquantity"));
                p.setPdiscount(rs.getDouble("pdiscount"));
                p.setProdImage(rs.getBinaryStream("image"));
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------------------------------------------------------------------
    // SELL N PRODUCT
    // --------------------------------------------------------------------
    @Override
    public boolean sellNProduct(String prodId, int n) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE product SET pquantity = pquantity - ? WHERE pid=? AND pquantity >= ?")) {

            ps.setInt(1, n);
            ps.setString(2, prodId);
            ps.setInt(3, n);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // --------------------------------------------------------------------
    // GET PRODUCT PRICE
    // --------------------------------------------------------------------
    @Override
    public double getProductPrice(String prodId) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT pprice FROM product WHERE pid=?")) {

            ps.setString(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble("pprice");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --------------------------------------------------------------------
    // GET PRODUCT QUANTITY
    // --------------------------------------------------------------------
    @Override
    public int getProductQuantity(String prodId) {
        try (Connection con = DBUtil.provideConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT pquantity FROM product WHERE pid=?")) {

            ps.setString(1, prodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt("pquantity");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
