package com.jmp.application;

import com.jmp.bank.api.Bank;
import com.jmp.cloud.service.impl.SubscriptionNotFoundException;
import com.jmp.dto.BankCardType;
import com.jmp.dto.User;
import com.jmp.service.api.Service;

import java.time.LocalDate;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        var bankLoader = ServiceLoader.load(Bank.class);
        var bank = bankLoader.findFirst().orElseThrow(() -> new IllegalStateException("No Bank service found"));

        var serviceLoader = ServiceLoader.load(Service.class);
        var service = serviceLoader.findFirst().orElseThrow(() -> new IllegalStateException("No Service provider found"));

        var user1 = new User("John", "Doe", LocalDate.of(1995, 5, 20));
        var user2 = new User("Alice", "Smith", LocalDate.of(2010, 8, 15));

        System.out.println("User 1 payable? " + Service.isPayableUser(user1));
        System.out.println("User 2 payable? " + Service.isPayableUser(user2));

        var creditCard = bank.createBankCard(user1, BankCardType.CREDIT);
        var debitCard = bank.createBankCard(user2, BankCardType.DEBIT);

        System.out.println("Created credit card: " + creditCard);
        System.out.println("Created debit card: " + debitCard);

        service.subscribe(creditCard);
        service.subscribe(debitCard);

        var sub = service.getSubscriptionByBankCardNumber(creditCard.getNumber())
                .orElseThrow(() -> new SubscriptionNotFoundException("Card not found: " + creditCard.getNumber()));
        System.out.println("Found subscription: " + sub);

        System.out.println("All users count: " + service.getAllUsers().size());
        System.out.println("Average user age: " + service.getAverageUsersAge());

        var recentSubs = service.getAllSubscriptionsByCondition(s ->
                s.getStartDate().isAfter(LocalDate.now().minusDays(1)));
        System.out.println("Active recent subs: " + recentSubs.size());

        try {
            service.getSubscriptionByBankCardNumber("INVALID-9999")
                    .orElseThrow(() -> new SubscriptionNotFoundException("Not found test"));
        } catch (SubscriptionNotFoundException ex) {
            System.out.println("Expected exception caught: " + ex.getMessage());
        }
    }
}
