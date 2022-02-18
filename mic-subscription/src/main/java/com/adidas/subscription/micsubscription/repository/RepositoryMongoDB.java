package com.adidas.subscription.micsubscription.repository; 

import java.util.List;

import com.adidas.subscription.api.rest.model.Subscription;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositoryMongoDB extends MongoRepository<Subscription, String> {
    
    List<Subscription> findByEmail(String email);

}
