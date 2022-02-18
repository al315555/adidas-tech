package com.adidas.subscription.micsubscription.service;

import java.util.List; 

import com.adidas.subscription.api.rest.model.RequestOneSubscription;
import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.micsubscription.repository.RepositoryMongoDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
     
    private static Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    RepositoryMongoDB repositoryMongoDB;

    public Subscription save(final Subscription subscription){
        logger.info("Saving subscription");
        logger.debug("Saving subscription: ", subscription );
        final Subscription subscriptionSaved = repositoryMongoDB.insert(subscription);
        logger.info("Saved subscription");
        logger.debug("Saved subscription: ", subscription);
        return subscriptionSaved;
    }

    public List<Subscription> getSubscriptions(){
        return repositoryMongoDB.findAll();
    }

    public Subscription getOneSubscription(final RequestOneSubscription body){
        return repositoryMongoDB.findByEmail(body.getEmail()).stream().filter(subscription -> body.getNewslettterId().equals(subscription.getNewslettterId())).findFirst().get();
    }

    public Subscription cancelSubscription(final RequestOneSubscription body){
        final Subscription subscriptionRetrieved = repositoryMongoDB.findByEmail(body.getEmail()).stream().filter(subscription -> body.getNewslettterId().equals(subscription.getNewslettterId())).findFirst().get();
        logger.info("Removing/cancelling subscription");
        logger.debug("Removing/cancelling: ", subscriptionRetrieved );
        repositoryMongoDB.delete(subscriptionRetrieved);
        logger.info("Removed/Cancelled subscription");
        logger.debug("Removed/Cancelled subscription: ", subscriptionRetrieved);
        return subscriptionRetrieved;
    }
   
}
