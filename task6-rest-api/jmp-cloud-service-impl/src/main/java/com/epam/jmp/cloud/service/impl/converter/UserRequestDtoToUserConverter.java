package com.epam.jmp.cloud.service.impl.converter;

import com.epam.jmp.domain.User;
import com.epam.jmp.dto.UserRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserRequestDtoToUserConverter implements Converter<UserRequestDto, User> {

    @Override
    public User convert(UserRequestDto source) {
        if (source == null) {
            return null;
        }

        User user = new User();
        user.setId(source.getId());
        user.setName(source.getName());
        user.setSurname(source.getSurname());

        if (source.getBirthday() != null && !source.getBirthday().isEmpty()) {
            LocalDate date = LocalDate.parse(source.getBirthday());
            user.setBirthday(date);
        }

        return user;
    }
}
