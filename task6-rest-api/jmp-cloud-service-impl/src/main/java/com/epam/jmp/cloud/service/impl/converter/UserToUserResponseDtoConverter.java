package com.epam.jmp.cloud.service.impl.converter;

import com.epam.jmp.domain.User;
import com.epam.jmp.dto.UserResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseDtoConverter implements Converter<User, UserResponseDto> {

    @Override
    public UserResponseDto convert(User source) {
        if (source == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(source.getId());
        dto.setName(source.getName());
        dto.setSurname(source.getSurname());

        if (source.getBirthday() != null) {
            dto.setBirthday(source.getBirthday().toString());
        }

        return dto;
    }
}
