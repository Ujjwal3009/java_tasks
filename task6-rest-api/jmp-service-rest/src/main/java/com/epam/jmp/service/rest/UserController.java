package com.epam.jmp.service.rest;

import com.epam.jmp.domain.User;
import com.epam.jmp.dto.UserRequestDto;
import com.epam.jmp.dto.UserResponseDto;
import com.epam.jmp.service.api.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "REST API for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "created")
    public EntityModel<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        User user = conversionService.convert(userRequestDto, User.class);
        User createdUser = userService.createUser(user);

        UserResponseDto responseDto = conversionService.convert(createdUser, UserResponseDto.class);

        EntityModel<UserResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(UserController.class).getUser(responseDto.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUser()).withRel("users"));

        return model;
    }

    @PutMapping
    @Operation(summary = "Update existing user")
    @ApiResponse(responseCode = "200", description = "updated ok")
    public EntityModel<UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto) {
        User user = conversionService.convert(userRequestDto, User.class);
        User updatedUser = userService.updateUser(user);

        UserResponseDto responseDto = conversionService.convert(updatedUser, UserResponseDto.class);

        EntityModel<UserResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(UserController.class).getUser(responseDto.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUser()).withRel("users"));

        return model;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "204", description = "deleted")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "found"),
        @ApiResponse(responseCode = "404", description = "not found")
    })
    public EntityModel<UserResponseDto> getUser(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.getUser(id);

        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        User user = optionalUser.get();
        UserResponseDto responseDto = conversionService.convert(user, UserResponseDto.class);

        EntityModel<UserResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUser()).withRel("users"));

        return model;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "returns list of users")
    public List<EntityModel<UserResponseDto>> getAllUser() {
        List<User> userList = userService.getAllUsers();
        List<EntityModel<UserResponseDto>> resultList = new ArrayList<>();

        for (User u : userList) {
            UserResponseDto dto = conversionService.convert(u, UserResponseDto.class);
            EntityModel<UserResponseDto> model = EntityModel.of(dto);
            model.add(linkTo(methodOn(UserController.class).getUser(dto.getId())).withSelfRel());
            resultList.add(model);
        }

        return resultList;
    }
}
