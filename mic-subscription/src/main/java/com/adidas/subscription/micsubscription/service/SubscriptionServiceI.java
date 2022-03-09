package com.adidas.subscription.micsubscription.service;

import java.util.List;

import com.adidas.subscription.api.rest.model.Subscription;

public interface SubscriptionServiceI {

    Subscription save(final Subscription subscription);

    List<Subscription> getSubscriptions();

    Subscription getOneSubscription(final Long newslettterId, final String email);

    Subscription cancelSubscription(final Long newslettterId, final String email);

}
