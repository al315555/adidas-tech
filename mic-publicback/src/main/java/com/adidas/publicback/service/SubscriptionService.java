package com.adidas.publicback.service;
 
import java.util.List;

import com.adidas.back.api.rest.model.RequestOneSubscription;
import com.adidas.back.api.rest.model.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriptionService {
     
    private static Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Value("${baseurl.subscription}")
    private String subscriptionBaseURL = "http://localhost:8082";

    public Subscription save(final Subscription subscription) {
         
        logger.info("Saving subscription");
        logger.debug("Saving subscription: ", subscription );
        final String uri =  subscriptionBaseURL+"/subcriptions-api/subscription";
        //final String uriEmailSender = emailSenderBaseURL+"/send_email?email="+subscription.getEmail();
         
        //logger.info(uri, uriEmailSender);
        final RestTemplate restTemplate = new RestTemplate();
        final Subscription result = restTemplate.postForObject(uri, subscription, Subscription.class);
        //final String resultEmailSender = restTemplate.postForObject(uriEmailSender, null, String.class);
        logger.info("Saved subscription");
        logger.debug("Saved subscription: ", subscription);
        return result;
        
    }

    public List<Subscription> getSubscriptions() {
         
        final String uri =  subscriptionBaseURL+"/subcriptions-api/subscription/all";
        logger.info(uri);
        final RestTemplate restTemplate = new RestTemplate();
        final List<Subscription> result = restTemplate.getForObject(uri, List.class);
        return result;
         
    }

    public Subscription getOneSubscription(final RequestOneSubscription body) {
         
        final String uri =  subscriptionBaseURL+"/subcriptions-api/subscription";
        logger.info(uri);
        final RestTemplate restTemplate = new RestTemplate();
        final Subscription result = restTemplate.getForObject(uri, Subscription.class, body);
        return result;
        
    }

    public Subscription cancelSubscription(final RequestOneSubscription body)   {
        logger.info("Removing/cancelling subscription");
        logger.debug("Removing/cancelling: ", body );
        final String uri = subscriptionBaseURL+"/subcriptions-api/subscription/cancel";
        //final String uriEmailSender = emailSenderBaseURL+"/send_email?email="+body.getEmail();
        //logger.info(uri, uriEmailSender);
        final RestTemplate restTemplate = new RestTemplate();
        final Subscription result = restTemplate.getForObject(uri, Subscription.class, body);
        //final String resultEmailSender = restTemplate.postForObject(uri, null, String.class);
        logger.info("Removed/Cancelled subscription");
        logger.debug("Removed/Cancelled subscription: ", body);
        return result;
    
    }
   
}
