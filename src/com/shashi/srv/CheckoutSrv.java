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
import com.shashi.service.impl.DiscountFactoryImpl;
import com.shashi.service.impl.CouponDiscountStrategyImpl; // coupon strategy
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

        String couponCode = request.getParameter("coupon"); // <-- NEW

        List<CartItem> cart = new ArrayList<>();
        double originalTotal = 0;
        double totalDiscount = 0;
        double couponDiscount = 0;
        double finalTotal = 0;

        DiscountFactory factory = new DiscountFactoryImpl();

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

                double original = product.getProdPrice() * qty;

                // product-level discount
                DiscountStrategy strat = factory.getStrategy(product);
                double itemDiscount = strat.apply(product, qty);

                // coupon discount (applied AFTER product discount)
                double itemCoupon = 0;
                if (couponCode != null && !couponCode.trim().isEmpty()) {
                    CouponDiscountStrategyImpl couponStrat = new CouponDiscountStrategyImpl(couponCode);
                    itemCoupon = couponStrat.apply(product, qty);
                }

                double finalAmt = original - itemDiscount - itemCoupon;

                cart.add(new CartItem(
                    product.getProdId(),
                    product.getProdName(),
                    product.getProdPrice(),
                    qty,
                    product.getPdiscount(),
                    original,
                    itemDiscount,
                    itemCoupon,
                    finalAmt
                ));

                originalTotal += original;
                totalDiscount += itemDiscount;
                couponDiscount += itemCoupon;
                finalTotal += finalAmt;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // send totals
        request.setAttribute("cart", cart);
        request.setAttribute("originalTotal", originalTotal);
        request.setAttribute("productDiscountTotal", totalDiscount);
        request.setAttribute("couponDiscountTotal", couponDiscount);
        request.setAttribute("finalTotal", finalTotal);
        request.setAttribute("couponCode", couponCode);

        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

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
                if (!productService.sellNProduct(rs.getString("prodid"), rs.getInt("quantity"))) {
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


    public static class CartItem {
        public String prodId, name;
        public double price, pdiscount, original, productDiscount, couponDiscount, finalAmt;
        public int qty;

        public CartItem(String prodId, String name, double price, int qty,
                        double pdiscount, double original, double productDiscount,
                        double couponDiscount, double finalAmt) {

            this.prodId = prodId;
            this.name = name;
            this.price = price;
            this.qty = qty;
            this.pdiscount = pdiscount;
            this.original = original;
            this.productDiscount = productDiscount;
            this.couponDiscount = couponDiscount;
            this.finalAmt = finalAmt;
        }
    }
}


