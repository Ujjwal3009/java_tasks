package com.epam.jmp.cloud.service.impl.converter;

import com.epam.jmp.domain.Subscription;
import com.epam.jmp.dto.SubscriptionResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionToSubscriptionResponseDtoConverter implements Converter<Subscription, SubscriptionResponseDto> {

    @Override
    public SubscriptionResponseDto convert(Subscription source) {
        if (source == null) {
            return null;
        }

        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(source.getId());

        if (source.getUser() != null) {
            dto.setUserId(source.getUser().getId());
        }

        if (source.getStartDate() != null) {
            dto.setStartDate(source.getStartDate().toString());
        }

        return dto;
    }
}
