package com.adidas.publicback.controller;
 
import java.util.List;
import java.util.Objects;

import com.adidas.back.api.rest.SubscriptionApiDelegate;
import com.adidas.back.api.rest.model.RequestOneSubscription;
import com.adidas.back.api.rest.model.Subscription;
import com.adidas.publicback.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;

@Component
public class SubscriptionsController implements SubscriptionApiDelegate {
    
    @Autowired
    SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<List<Subscription>> getsubscriptions(){
        return getResponseListEntityWithHeader(subscriptionService.getSubscriptions());
    }

    @Override
    public ResponseEntity<Subscription> getOneSubscription(RequestOneSubscription body){
        if(Objects.nonNull(body) && Objects.nonNull(body.getEmail()) && Objects.nonNull(body.getNewslettterId()))
            return getResponseEntityWithHeader(subscriptionService.getOneSubscription(body));
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Subscription> newSubscription(Subscription body){
        if(Objects.nonNull(body))
            return getResponseEntityWithHeader(subscriptionService.save(body));
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Subscription> cancelSubscription(RequestOneSubscription body) {
        if(Objects.nonNull(body))
            return getResponseEntityWithHeader(subscriptionService.cancelSubscription(body));
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<String> errorhandleException(final Exception exception) {
        return ResponseEntity.unprocessableEntity().body(exception.getMessage());
    }

    private ResponseEntity<List<Subscription>> getResponseListEntityWithHeader(final List<Subscription> subscriptions ){
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        if(Objects.nonNull(subscriptions))
            return new ResponseEntity<>(subscriptions, headers, HttpStatus.OK);
        return new ResponseEntity<>(subscriptions, headers, HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<Subscription> getResponseEntityWithHeader(final Subscription subscription ){
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        if(Objects.nonNull(subscription))
            return new ResponseEntity<>(subscription, headers, HttpStatus.OK);
        return new ResponseEntity<>(subscription, headers, HttpStatus.NO_CONTENT);
    }

}
