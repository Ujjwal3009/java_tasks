package com.epam.jmp.service.rest;

import com.epam.jmp.domain.Subscription;
import com.epam.jmp.dto.SubscriptionRequestDto;
import com.epam.jmp.dto.SubscriptionResponseDto;
import com.epam.jmp.service.api.SubscriptionService;
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
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription Controller", description = "REST API for managing subscriptions")
public class ServiceController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new subscription")
    @ApiResponse(responseCode = "201", description = "subscription created")
    public EntityModel<SubscriptionResponseDto> createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        Subscription subscription = conversionService.convert(subscriptionRequestDto, Subscription.class);
        Subscription createdSub = subscriptionService.createSubscription(subscription);

        SubscriptionResponseDto responseDto = conversionService.convert(createdSub, SubscriptionResponseDto.class);

        EntityModel<SubscriptionResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(ServiceController.class).getSubscription(responseDto.getId())).withSelfRel());
        model.add(linkTo(methodOn(ServiceController.class).getAllSubscription()).withRel("subscriptions"));
        if (responseDto.getUserId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUser(responseDto.getUserId())).withRel("user"));
        }

        return model;
    }

    @PutMapping
    @Operation(summary = "Update a subscription")
    @ApiResponse(responseCode = "200", description = "ok")
    public EntityModel<SubscriptionResponseDto> updateSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        Subscription subscription = conversionService.convert(subscriptionRequestDto, Subscription.class);
        Subscription updatedSub = subscriptionService.updateSubscription(subscription);

        SubscriptionResponseDto responseDto = conversionService.convert(updatedSub, SubscriptionResponseDto.class);

        EntityModel<SubscriptionResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(ServiceController.class).getSubscription(responseDto.getId())).withSelfRel());
        model.add(linkTo(methodOn(ServiceController.class).getAllSubscription()).withRel("subscriptions"));
        if (responseDto.getUserId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUser(responseDto.getUserId())).withRel("user"));
        }

        return model;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete subscription")
    @ApiResponse(responseCode = "204", description = "deleted")
    public void deleteSubscription(@PathVariable("id") Long id) {
        subscriptionService.deleteSubscription(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by its id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "found"),
        @ApiResponse(responseCode = "404", description = "subscription not found")
    })
    public EntityModel<SubscriptionResponseDto> getSubscription(@PathVariable("id") Long id) {
        Optional<Subscription> optionalSubscription = subscriptionService.getSubscription(id);

        if (!optionalSubscription.isPresent()) {
            throw new RuntimeException("Subscription not found with id: " + id);
        }

        Subscription sub = optionalSubscription.get();
        SubscriptionResponseDto responseDto = conversionService.convert(sub, SubscriptionResponseDto.class);

        EntityModel<SubscriptionResponseDto> model = EntityModel.of(responseDto);
        model.add(linkTo(methodOn(ServiceController.class).getSubscription(id)).withSelfRel());
        model.add(linkTo(methodOn(ServiceController.class).getAllSubscription()).withRel("subscriptions"));
        if (responseDto.getUserId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUser(responseDto.getUserId())).withRel("user"));
        }

        return model;
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions")
    @ApiResponse(responseCode = "200", description = "list of subscriptions")
    public List<EntityModel<SubscriptionResponseDto>> getAllSubscription() {
        List<Subscription> subList = subscriptionService.getAllSubscriptions();
        List<EntityModel<SubscriptionResponseDto>> resultList = new ArrayList<>();

        for (Subscription s : subList) {
            SubscriptionResponseDto dto = conversionService.convert(s, SubscriptionResponseDto.class);
            EntityModel<SubscriptionResponseDto> model = EntityModel.of(dto);
            model.add(linkTo(methodOn(ServiceController.class).getSubscription(dto.getId())).withSelfRel());
            if (dto.getUserId() != null) {
                model.add(linkTo(methodOn(UserController.class).getUser(dto.getUserId())).withRel("user"));
            }
            resultList.add(model);
        }

        return resultList;
    }
}
