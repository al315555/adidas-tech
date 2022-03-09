package com.adidas.publicback.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import com.adidas.back.api.rest.model.RequestOneSubscription;
import com.adidas.back.api.rest.model.Subscription;
import com.adidas.back.api.rest.model.Subscription.GenderEnum;
import com.adidas.publicback.service.SubscriptionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SubscriptionsControllerTest {

    @InjectMocks
    SubscriptionsController controller;

    @Mock
    SubscriptionService subscriptionService;

    Subscription subscription;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.subscription = new Subscription()
                .newslettterId(0L)
                .email("email@eew.es")
                .gender(GenderEnum.MASCULINE)
                .dateOfBirth(LocalDate.now())
                .consent(false);
    }

    @Test
    void testCancelSubscription() {
        final MultiValueMap<String, String> headers = retrieveHeaders();
        final ResponseEntity<Subscription> expectedReponse = new ResponseEntity<>(this.subscription, headers,
                HttpStatus.OK);
        final RequestOneSubscription request = new RequestOneSubscription()
                .email("email@eew.es")
                .newslettterId(0L);
        when(subscriptionService.cancelSubscription(request)).thenReturn(subscription);
        ResponseEntity<Subscription> response = controller.cancelSubscription(request);
        verify(subscriptionService, times(1)).cancelSubscription(request);
        assertEquals(expectedReponse, response);
    }

    @Test
    void testGetOneSubscription() {
        final MultiValueMap<String, String> headers = retrieveHeaders();
        final ResponseEntity<Subscription> expectedReponse = new ResponseEntity<>(this.subscription, headers,
                HttpStatus.OK);
        final RequestOneSubscription request = new RequestOneSubscription()
                .email("email@eew.es")
                .newslettterId(0L);

        when(subscriptionService.getOneSubscription(request)).thenReturn(subscription);
        ResponseEntity<Subscription> response = controller.getOneSubscription(request);
        verify(subscriptionService, times(1)).getOneSubscription(request);
        assertEquals(expectedReponse, response);
    }

    @Test
    void testGetsubscriptions() {
        final MultiValueMap<String, String> headers = retrieveHeaders();
        final ResponseEntity<List<Subscription>> expectedReponse = new ResponseEntity<>(List.of(this.subscription),
                headers, HttpStatus.OK);

        when(subscriptionService.getSubscriptions()).thenReturn(List.of(subscription));
        ResponseEntity<List<Subscription>> response = controller.getsubscriptions();
        verify(subscriptionService, times(1)).getSubscriptions();
        assertEquals(expectedReponse, response);
    }

    @Test
    void testNewSubscription() {
        final MultiValueMap<String, String> headers = retrieveHeaders();
        final ResponseEntity<Subscription> expectedReponse = new ResponseEntity<>(this.subscription, headers,
                HttpStatus.OK);

        when(subscriptionService.save(this.subscription)).thenReturn(subscription);
        ResponseEntity<Subscription> response = controller.newSubscription(this.subscription);
        verify(subscriptionService, times(1)).save(this.subscription);
        assertEquals(expectedReponse, response);
    }

    private MultiValueMap<String, String> retrieveHeaders() {
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        return headers;
    }

}
