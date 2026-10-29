package com.jmp.application;

import com.jmp.bank.api.Bank;
import com.jmp.cloud.bank.impl.BankImpl;
import com.jmp.cloud.service.impl.ServiceImpl;
import com.jmp.cloud.service.impl.SubscriptionNotFoundException;
import com.jmp.dto.BankCardType;
import com.jmp.dto.CreditBankCard;
import com.jmp.dto.DebitBankCard;
import com.jmp.dto.User;
import com.jmp.service.api.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ServiceLoader;

public class ApplicationTest {

    @Test
    public void testBankCardCreation() {
        var bank = new BankImpl();
        var user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1));

        var creditCard = bank.createBankCard(user, BankCardType.CREDIT);
        Assertions.assertInstanceOf(CreditBankCard.class, creditCard);
        Assertions.assertEquals("Jane", creditCard.getUser().getName());

        var debitCard = bank.createBankCard(user, BankCardType.DEBIT);
        Assertions.assertInstanceOf(DebitBankCard.class, debitCard);
        Assertions.assertEquals("Jane", debitCard.getUser().getName());
    }

    @Test
    public void testServiceSubscriptionAndAgeMethods() {
        var service = new ServiceImpl();
        var userOld = new User("Old", "User", LocalDate.of(1980, 1, 1));
        var userYoung = new User("Young", "User", LocalDate.now().minusYears(10));

        Assertions.assertTrue(Service.isPayableUser(userOld));
        Assertions.assertFalse(Service.isPayableUser(userYoung));

        var bank = new BankImpl();
        var card1 = bank.createBankCard(userOld, BankCardType.CREDIT);
        var card2 = bank.createBankCard(userYoung, BankCardType.DEBIT);

        service.subscribe(card1);
        service.subscribe(card2);

        var subOpt = service.getSubscriptionByBankCardNumber(card1.getNumber());
        Assertions.assertTrue(subOpt.isPresent());
        Assertions.assertEquals(card1.getNumber(), subOpt.get().getBankcardNumber());

        Assertions.assertThrows(SubscriptionNotFoundException.class, () ->
                service.getSubscriptionByBankCardNumber("nonexistent")
                        .orElseThrow(() -> new SubscriptionNotFoundException("Not found"))
        );

        Assertions.assertEquals(2, service.getAllUsers().size());
        Assertions.assertTrue(service.getAverageUsersAge() > 0);

        var allSubs = service.getAllSubscriptionsByCondition(s -> s.getStartDate() != null);
        Assertions.assertEquals(2, allSubs.size());
    }

    @Test
    public void testServiceLoader() {
        var bankLoader = ServiceLoader.load(Bank.class);
        Assertions.assertTrue(bankLoader.findFirst().isPresent());

        var serviceLoader = ServiceLoader.load(Service.class);
        Assertions.assertTrue(serviceLoader.findFirst().isPresent());
    }
}
