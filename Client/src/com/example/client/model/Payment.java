/*
Stavros Moschis icsd22232
Zacharias Kokkinakis icsd22077
 */package com.example.client.model;

import java.io.Serializable;

/**
 * Represents a payment for a booking.
 * For demo purposes only — do not store real card data in production!
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private String bookingId;
    private String cardHolder;
    private transient String cardNumber;
    private double amount;
    private String paymentDate;

    public Payment(String paymentId, String bookingId, double amount, String cardHolder, String cardNumber, String paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.paymentDate = paymentDate;
    }

    // Εναλλακτικός constructor χωρίς ημερομηνία (π.χ. ορίζεται server-side)
    public Payment(String paymentId, String bookingId, double amount, String cardHolder) {
        this(paymentId, bookingId, amount, cardHolder, "0000000000000000", null);
    }

    // === Getters & Setters ===
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", amount=" + amount +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }
}
