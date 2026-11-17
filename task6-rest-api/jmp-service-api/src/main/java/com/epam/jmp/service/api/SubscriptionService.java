package com.epam.jmp.service.api;

import com.epam.jmp.domain.Subscription;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription);
    Subscription updateSubscription(Subscription subscription);
    void deleteSubscription(Long id);
    Optional<Subscription> getSubscription(Long id);
    List<Subscription> getAllSubscriptions();
}
