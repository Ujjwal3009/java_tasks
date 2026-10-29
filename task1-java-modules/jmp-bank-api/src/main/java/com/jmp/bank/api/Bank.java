package com.jmp.bank.api;

import com.jmp.dto.BankCard;
import com.jmp.dto.BankCardType;
import com.jmp.dto.User;

public interface Bank {
    BankCard createBankCard(User user, BankCardType bankCardType);
}
