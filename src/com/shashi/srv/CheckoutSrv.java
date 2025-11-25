package com.shashi.srv;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.shashi.beans.ProductBean;
import com.shashi.service.DiscountStrategy;
import com.shashi.service.DiscountFactory;
import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.service.impl.DiscountFactoryImpl;  // <-- IMPORTANT
import com.shashi.utility.DBUtil;

@WebServlet("/CheckoutSrv")
public class CheckoutSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp?message=Please login to checkout");
            return;
        }

        List<CartItem> cart = new ArrayList<>();
        double originalTotal = 0;
        double totalDiscount = 0;
        double finalTotal = 0;

        // USE IMPLEMENTATION, NOT INTERFACE
        DiscountFactory discountFactory = new DiscountFactoryImpl();

        try (Connection con = DBUtil.provideConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT uc.prodid, uc.quantity, p.pname, p.pprice, p.pdiscount " +
                "FROM usercart uc JOIN product p ON uc.prodid = p.pid WHERE uc.username = ?");

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setProdId(rs.getString("prodid"));
                product.setProdName(rs.getString("pname"));
                product.setProdPrice(rs.getDouble("pprice"));
                product.setPdiscount(rs.getDouble("pdiscount"));

                int qty = rs.getInt("quantity");

                // Compute prices
                double original = product.getProdPrice() * qty;

                DiscountStrategy strategy = discountFactory.getStrategy(product);
                double discount = strategy.apply(product, qty);
                double finalAmt = original - discount;

                cart.add(new CartItem(
                    product.getProdId(),
                    product.getProdName(),
                    product.getProdPrice(),
                    qty,
                    product.getPdiscount(),
                    original,
                    discount,
                    finalAmt
                ));

                originalTotal += original;
                totalDiscount += discount;
                finalTotal += finalAmt;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("cart", cart);
        request.setAttribute("originalTotal", originalTotal);
        request.setAttribute("totalDiscount", totalDiscount);
        request.setAttribute("finalTotal", finalTotal);

        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }


    // POST — Process checkout
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        ProductServiceImpl productService = new ProductServiceImpl();
        boolean success = true;

        try (Connection con = DBUtil.provideConnection()) {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "SELECT prodid, quantity FROM usercart WHERE username=? FOR UPDATE");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String prodId = rs.getString("prodid");
                int qty = rs.getInt("quantity");

                if (!productService.sellNProduct(prodId, qty)) {
                    success = false;
                    break;
                }
            }

            if (success) {
                PreparedStatement clear = con.prepareStatement(
                    "DELETE FROM usercart WHERE username=?");
                clear.setString(1, username);
                clear.executeUpdate();
                con.commit();
            } else {
                con.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        if (success)
            response.sendRedirect("checkoutSuccess.jsp");
        else
            response.sendRedirect("checkout.jsp?error=Checkout Failed");
    }


    // ---------------------------------------------------------
    // Inner Class representing an item in the cart
    // ---------------------------------------------------------
    public static class CartItem {
        public String prodId, name;
        public double price, pdiscount, original, discount, finalAmt;
        public int qty;

        public CartItem(String prodId, String name, double price, int qty,
                        double pdiscount, double original, double discount, double finalAmt) {
            this.prodId = prodId;
            this.name = name;
            this.price = price;
            this.qty = qty;
            this.pdiscount = pdiscount;
            this.original = original;
            this.discount = discount;
            this.finalAmt = finalAmt;
        }
    }
}
