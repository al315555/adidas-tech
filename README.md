# Adidas newsletter subscription

There are 3 microservices with a mongoDB inside the cluster to be run by docker-compose.

```bash
docker-compose up 
```

After launch the previous command inside a terminal where docker-compose was installed previously four containers will be run:

 - adidas-mic-subscription-1   http://localhost:8082/subcriptions-api/
 - adidas-mic-emailsender-1   http://localhost:8083/send_email?email=<sample>
 - adidas-mic-publicback-1    http://localhost:8081/publicback/
 - adidas-mongodb-1



Swagger is available in the following link: 

 - adidas-mic-subscription-1  http://localhost:8082/swagger-ui/index.html 
 - adidas-mic-publicback-1    http://localhost:8081/swagger-ui/index.html 

To access to the correct version the following sentences should be searched with the search top bar: /v2/api-docs



## Request samples

##### Get all Subscriptions

```
curl --location --request GET 'http://localhost:8081/publicback/subscription/all'
```

##### Create new Subscription

```
curl --location --request POST 'http://localhost:8081/publicback/subscription' \

--header 'Content-Type: application/json' \

--data-raw '{ "gender" : "masculine", "dateOfBirth" : "2000-01-23", "newslettterId" : 0, "consent" : false, "email" : "email@eew.es" }'
```

##### Retrieve One Subscription

```
curl --location --request GET 'http://localhost:8081/publicback/subscription' \

--header 'Content-Type: application/json' \

--data-raw '{ "newslettterId" : 0, "email" : "email@eew.es" }'
```

##### Cancel One Subscription

```
curl --location --request PUT 'http://localhost:8081/publicback/subscription/cancel' \
--header 'Content-Type: application/json' \
--data-raw '{ "newslettterId" : 0, "email" : "email@eew.es" }'
```



By Rubén H