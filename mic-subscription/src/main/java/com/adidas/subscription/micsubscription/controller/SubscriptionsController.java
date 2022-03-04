package com.adidas.subscription.micsubscription.controller;
 
import java.util.List;
import java.util.Objects;

import com.adidas.subscription.api.rest.SubscriptionApiDelegate;
import com.adidas.subscription.api.rest.model.RequestOneSubscription;
import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.micsubscription.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<Subscription> getOneSubscription(Long newslettterId, String email){
        if(Objects.nonNull(newslettterId) && Objects.nonNull(email) )
            return getResponseEntityWithHeader(subscriptionService.getOneSubscription(newslettterId, email));
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

    private ResponseEntity<List<Subscription>> getResponseListEntityWithHeader(final List<Subscription> subscriptions ){
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        ResponseEntity<List<Subscription>> response = new ResponseEntity<>(subscriptions, headers, HttpStatus.OK);
        return response;
    }

    private ResponseEntity<Subscription> getResponseEntityWithHeader(final Subscription subscription ){
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        ResponseEntity<Subscription> response = new ResponseEntity<>(subscription, headers, HttpStatus.OK);
        return response;
    }

}
