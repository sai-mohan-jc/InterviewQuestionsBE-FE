package com.banking.customer.javacore.collections;

public class Transaction implements Comparable<Transaction> {

    private String transactionId;
    private String customerName;
    private double amount;

    public Transaction(String transactionId, String customerName, double amount) {
        this.transactionId = transactionId;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    // Natural ordering
    // Transaction ID: TXN101, TXN102, TXN103...
    @Override
    public int compareTo(Transaction other) {
        return Double.compare(other.amount,this.amount);
    }

    @Override
    public String toString() {
        return transactionId + " | "
                + customerName + " | "
                + amount;
    }
}