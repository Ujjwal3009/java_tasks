package com.jmp.cloud.service.impl;

import com.jmp.dto.BankCard;
import com.jmp.dto.Subscription;
import com.jmp.dto.User;
import com.jmp.service.api.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServiceImpl implements Service {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
    private final List<BankCard> bankCards = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void subscribe(BankCard bankCard) {
        var subscription = new Subscription(bankCard.getNumber(), LocalDate.now());
        subscriptions.put(bankCard.getNumber(), subscription);
        bankCards.add(bankCard);
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String bankcardNumber) {
        return Optional.ofNullable(subscriptions.get(bankcardNumber));
    }

    @Override
    public List<User> getAllUsers() {
        return bankCards.stream()
                .map(BankCard::getUser)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate) {
        return subscriptions.values().stream()
                .filter(predicate)
                .collect(Collectors.toUnmodifiableList());
    }
}
