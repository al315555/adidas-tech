package com.adidas.subscription.micsubscription.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.micsubscription.repository.RepositoryMongoDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service 
public class SubscriptionService implements SubscriptionServiceI {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    RepositoryMongoDB repositoryMongoDB;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${baseurl.emailsender}")
    private String emailSenderBaseURL = "http://localhost:8083";

    @Override
    public Subscription save(final Subscription subscription) {
        log.info("Saving subscription");
        log.debug("Saving subscription: {}", subscription);
        final Subscription subscriptionSaved = repositoryMongoDB.insert(subscription);
        log.info("Saved subscription");
        log.debug("Saved subscription: {}", subscription);
        sendEmailTo(subscription.getEmail());
        return subscriptionSaved;
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return repositoryMongoDB.findAll();
    }

    @Override
    public Subscription getOneSubscription(final Long newslettterId, final String email) throws NoSuchElementException {
        return repositoryMongoDB.findByEmail(email).stream()
                .filter(subscription -> newslettterId.equals(subscription.getNewslettterId())).findFirst().get();
    }

    @Override
    public Subscription cancelSubscription(final Long newslettterId, final String email) throws NoSuchElementException {
        final Subscription subscriptionRetrieved = getOneSubscription(newslettterId, email);
        log.info("Removing/cancelling subscription");
        log.debug("Removing/cancelling subscription: {}", subscriptionRetrieved);
        repositoryMongoDB.delete(subscriptionRetrieved);
        log.info("Removed/Cancelled subscription");
        log.debug("Removed/Cancelled subscription: {}", subscriptionRetrieved);
        sendEmailTo(email);
        return subscriptionRetrieved;
    }

    private void sendEmailTo(final String email) {
        try {
            final String uriEmailSender = emailSenderBaseURL + "/send_email?email=" + email;
            final String result = this.restTemplate.postForObject(uriEmailSender, null, String.class);
            log.info("Email sent");
            log.debug("Email sent to: {}", email);
            log.debug("Return msg:  {}", result);
        } catch (Exception e) {
            log.error("Email was not sent due to an Error: {}", e);
        }
    }
}
