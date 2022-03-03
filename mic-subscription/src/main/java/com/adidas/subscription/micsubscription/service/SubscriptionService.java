package com.adidas.subscription.micsubscription.service;

import java.util.List; 

import com.adidas.subscription.api.rest.model.RequestOneSubscription;
import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.micsubscription.repository.RepositoryMongoDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriptionService {
     
    private static Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    RepositoryMongoDB repositoryMongoDB;

    @Value("${baseurl.emailsender}")
    private String emailSenderBaseURL = "http://localhost:8083";
    
    public Subscription save(final Subscription subscription){
        logger.info("Saving subscription");
        logger.debug("Saving subscription: ", subscription );
        final Subscription subscriptionSaved = repositoryMongoDB.insert(subscription);
        logger.info("Saved subscription");
        logger.debug("Saved subscription: ", subscription);
        sendEmailTo(subscription.getEmail());
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
        sendEmailTo(body.getEmail());
        return subscriptionRetrieved;
    }

    private void sendEmailTo(final String email) {
        try{
            final String uriEmailSender = emailSenderBaseURL+"/send_email?email="+ email;
            final RestTemplate restTemplate = new RestTemplate();
            final Subscription result = restTemplate.postForObject(uriEmailSender, null, null);
            logger.info("Email sent");
            logger.debug("Email sent to: {}", email);
            logger.debug("Return msg:  {}", result);
        }catch(Exception e){
            logger.error("Email was not sent due to an Error: {}", e);
        }   
    }
}
