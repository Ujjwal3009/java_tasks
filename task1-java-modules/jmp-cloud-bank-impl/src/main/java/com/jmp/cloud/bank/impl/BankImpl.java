package com.jmp.cloud.bank.impl;

import com.jmp.bank.api.Bank;
import com.jmp.dto.BankCard;
import com.jmp.dto.BankCardType;
import com.jmp.dto.CreditBankCard;
import com.jmp.dto.DebitBankCard;
import com.jmp.dto.User;

import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

public class BankImpl implements Bank {

    private static final Random RANDOM = new Random();

    private final Map<BankCardType, BiFunction<String, User, BankCard>> creators = Map.of(
            BankCardType.CREDIT, CreditBankCard::new,
            BankCardType.DEBIT, DebitBankCard::new
    );

    @Override
    public BankCard createBankCard(User user, BankCardType bankCardType) {
        var cardNumber = generateCardNumber();
        var creator = creators.get(bankCardType);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown card type: " + bankCardType);
        }
        return creator.apply(cardNumber, user);
    }

    protected String generateCardNumber() {
        var sb = new StringBuilder();
        for (var i = 0; i < 4; i++) {
            var segment = 1000 + RANDOM.nextInt(9000);
            sb.append(segment);
            if (i < 3) sb.append("-");
        }
        return sb.toString();
    }
}
