package com.adidas.subscription.micsubscription.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.adidas.subscription.api.rest.SubscriptionApiDelegate;
import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.micsubscription.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;

@Component
public class SubscriptionsController implements SubscriptionApiDelegate {

    @Autowired
    SubscriptionService subscriptionService;

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @Override
    public ResponseEntity<List<Subscription>> getsubscriptions() {
        return getResponseListEntityWithHeader(subscriptionService.getSubscriptions());
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @Override
    public ResponseEntity<Subscription> getOneSubscription(Long newslettterId, String email) {
        if (Objects.nonNull(newslettterId) && Objects.nonNull(email))
            try {
                return getResponseEntityWithHeader(subscriptionService.getOneSubscription(newslettterId, email));
            } catch (

            NoSuchElementException noSuchElementException) {
                MultiValueMap<String, String> headers = retrieveFilledHeaderWithJsonMediaType();
                headers.add("status", "Subscription element to be selected was not found");
                return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
            }
        return ResponseEntity.badRequest().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @Override
    public ResponseEntity<Subscription> newSubscription(Subscription body) {
        if (Objects.nonNull(body))
            return getResponseEntityWithHeader(subscriptionService.save(body));
        return ResponseEntity.badRequest().build();
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @Override
    public ResponseEntity<Subscription> cancelSubscription(Long newslettterId, String email) {
        if (Objects.nonNull(newslettterId) && Objects.nonNull(email))
            try {
                return getResponseEntityWithHeader(subscriptionService.cancelSubscription(newslettterId, email));
            } catch (NoSuchElementException noSuchElementException) {
                MultiValueMap<String, String> headers = retrieveFilledHeaderWithJsonMediaType();
                headers.add("status", "Subscription element to be canceled was not found");
                return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
            }
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<String> errorhandleException(final Exception exception) {
        return ResponseEntity.unprocessableEntity().body(exception.getMessage());
    }

    private ResponseEntity<List<Subscription>> getResponseListEntityWithHeader(final List<Subscription> subscriptions) {
        MultiValueMap<String, String> headers = retrieveFilledHeaderWithJsonMediaType();
        ResponseEntity<List<Subscription>> response = new ResponseEntity<>(subscriptions, headers, HttpStatus.OK);
        return response;
    }

    private ResponseEntity<Subscription> getResponseEntityWithHeader(final Subscription subscription) {
        MultiValueMap<String, String> headers = retrieveFilledHeaderWithJsonMediaType();
        ResponseEntity<Subscription> response = new ResponseEntity<>(subscription, headers, HttpStatus.OK);
        return response;
    }

    private MultiValueMap<String, String> retrieveFilledHeaderWithJsonMediaType() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        return headers;
    }

}
