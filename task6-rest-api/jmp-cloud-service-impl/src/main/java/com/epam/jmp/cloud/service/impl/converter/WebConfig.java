package com.epam.jmp.cloud.service.impl.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserRequestDtoToUserConverter userRequestDtoToUserConverter;

    @Autowired
    private UserToUserResponseDtoConverter userToUserResponseDtoConverter;

    @Autowired
    private SubscriptionRequestDtoToSubscriptionConverter subscriptionRequestDtoToSubscriptionConverter;

    @Autowired
    private SubscriptionToSubscriptionResponseDtoConverter subscriptionToSubscriptionResponseDtoConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(userRequestDtoToUserConverter);
        registry.addConverter(userToUserResponseDtoConverter);
        registry.addConverter(subscriptionRequestDtoToSubscriptionConverter);
        registry.addConverter(subscriptionToSubscriptionResponseDtoConverter);
    }
}
