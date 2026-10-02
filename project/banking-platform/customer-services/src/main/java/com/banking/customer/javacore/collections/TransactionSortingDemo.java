package com.banking.customer.javacore.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionSortingDemo {

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction("TXN103", "Mohan", 5000));
        transactions.add(new Transaction("TXN101", "Ravi", 1500));
        transactions.add(new Transaction("TXN102", "Kumar", 8000));
        transactions.add(new Transaction("TXN104", "Anil", 2500));

        System.out.println("Original Transactions:");
        transactions.forEach(System.out::println);

        // ----------------------------------
        // 1. Comparable
        // ----------------------------------

        transactions.sort(null);

        System.out.println("\nComparable - Transaction ID:");
        transactions.forEach(System.out::println);

        // ----------------------------------
        // 2. Comparator - Amount ascending
        // ----------------------------------

        transactions.sort(
                Comparator.comparing(Transaction::getAmount)
        );

        System.out.println("\nComparator - Amount Ascending:");
        transactions.forEach(System.out::println);

        // ----------------------------------
        // 3. Comparator - Amount descending
        // ----------------------------------

        transactions.sort(
                Comparator.comparing(Transaction::getAmount).reversed()
        );

        System.out.println("\nComparator - Amount Descending:");
        transactions.forEach(System.out::println);

        // ----------------------------------
        // 4. Comparator - Customer Name
        // ----------------------------------

        transactions.sort(
                Comparator.comparing(Transaction::getCustomerName)
        );

        System.out.println("\nComparator - Customer Name:");
        transactions.forEach(System.out::println);
        
        
        //Get only names in object
        List<String> names=transactions.stream().map(t->t.getCustomerName()).collect(Collectors.toList());
        names.sort(String::compareTo);
        
        System.out.println(names);
        
    }
}