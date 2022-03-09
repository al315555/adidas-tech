package com.adidas.subscription.micsubscription.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import com.adidas.subscription.api.rest.model.Subscription;
import com.adidas.subscription.api.rest.model.Subscription.GenderEnum;
import com.adidas.subscription.micsubscription.repository.RepositoryMongoDB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations; 
import org.slf4j.LoggerFactory; 
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.springframework.web.client.RestTemplate;

public class SubscriptionServiceTest {

    @InjectMocks
    SubscriptionService subscriptionService;

    @Mock
    RepositoryMongoDB repositoryMongoDB;
 
    Logger log;

    @Mock
    RestTemplate restTemplate;

    Subscription subscription;

    List<ILoggingEvent> logsList;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.subscription = new Subscription()
                .newslettterId(1568L)
                .email("email_2@adidas.com")
                .gender(GenderEnum.FEMENINE)
                .dateOfBirth(LocalDate.now())
                .consent(false);

        this.log = (Logger) LoggerFactory.getLogger(SubscriptionService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        this.log.addAppender(listAppender);
        this.logsList = listAppender.list;

    }

    @Test
    void testCancelSubscription() {
        final String emailResult = null;
        when(repositoryMongoDB.findByEmail(eq("email_2@adidas.com"))).thenReturn(List.of(this.subscription));
        final Subscription result = subscriptionService.cancelSubscription(1568L, "email_2@adidas.com");
        verify(repositoryMongoDB, times(1)).delete(this.subscription);
        assertTrue(logsList.get(0).getFormattedMessage().contains("Removing/cancelling subscription"));
        assertTrue(logsList.get(1).getFormattedMessage().contains("Removing/cancelling subscription: "+this.subscription));
        assertTrue(logsList.get(2).getFormattedMessage().contains("Removed/Cancelled subscription"));
        assertTrue(logsList.get(3).getFormattedMessage().contains("Removed/Cancelled subscription: "+this.subscription));

        verifyEmailSenderLogs(emailResult);
        assertEquals(this.subscription, result);
    }

    @Test
    void testGetOneSubscription() {
        when(repositoryMongoDB.findByEmail(eq("email_2@adidas.com"))).thenReturn(List.of(this.subscription));
        final Subscription result = subscriptionService.getOneSubscription(1568L, "email_2@adidas.com");
        assertEquals(this.subscription, result);
    }

    @Test
    void testGetSubscriptions() {
        when(repositoryMongoDB.findAll()).thenReturn(List.of(this.subscription));
        final List<Subscription> result = subscriptionService.getSubscriptions();
        assertEquals(List.of(this.subscription), result);
    }

    @Test
    void testSave() {
        final String emailResult = null;
        when(repositoryMongoDB.insert(this.subscription)).thenReturn(this.subscription);
        final Subscription result = subscriptionService.save(this.subscription);
        verify(repositoryMongoDB, times(1)).insert(this.subscription);
        assertTrue(logsList.get(0).getFormattedMessage().contains("Saving subscription"));
        assertTrue(logsList.get(1).getFormattedMessage().contains("Saving subscription: "+this.subscription));
        assertTrue(logsList.get(2).getFormattedMessage().contains("Saved subscription"));
        assertTrue(logsList.get(3).getFormattedMessage().contains("Saved subscription: "+this.subscription));

        verifyEmailSenderLogs(emailResult);
        assertEquals(this.subscription, result);
    }

    private void verifyEmailSenderLogs(final String emailResult) { 
        assertTrue(logsList.get(logsList.size()-3).getFormattedMessage().contains("Email sent"));
        assertTrue(logsList.get(logsList.size()-2).getFormattedMessage().contains("Email sent to: email_2@adidas.com"));
        assertTrue(logsList.get(logsList.size()-1).getFormattedMessage().contains("Return msg:  null"));
    }
}
