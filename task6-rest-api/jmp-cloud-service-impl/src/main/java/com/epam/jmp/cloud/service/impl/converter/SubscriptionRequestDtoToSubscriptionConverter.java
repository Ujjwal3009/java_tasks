package com.epam.jmp.cloud.service.impl.converter;

import com.epam.jmp.cloud.service.impl.UserRepository;
import com.epam.jmp.domain.Subscription;
import com.epam.jmp.domain.User;
import com.epam.jmp.dto.SubscriptionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class SubscriptionRequestDtoToSubscriptionConverter implements Converter<SubscriptionRequestDto, Subscription> {

    @Autowired
    private UserRepository userRepository;

    public SubscriptionRequestDtoToSubscriptionConverter() {
    }

    public SubscriptionRequestDtoToSubscriptionConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Subscription convert(SubscriptionRequestDto source) {
        if (source == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(source.getId());

        if (source.getUserId() != null) {
            Optional<User> optionalUser = userRepository.findById(source.getUserId());
            if (optionalUser.isPresent()) {
                subscription.setUser(optionalUser.get());
            } else {
                throw new RuntimeException("User not found with id: " + source.getUserId());
            }
        }

        subscription.setStartDate(LocalDate.now());

        return subscription;
    }
}
