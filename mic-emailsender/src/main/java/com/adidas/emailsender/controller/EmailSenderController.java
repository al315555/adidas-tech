package com.adidas.emailsender.controller;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailSenderController{

    private static Logger logger = LoggerFactory.getLogger(EmailSenderController.class);

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping("/send_email")
    @ResponseStatus(HttpStatus.OK)
    public String sendEmail(@RequestParam(value = "email", required = true) final String email) throws IllegalArgumentException{
        if(!validate(email))
            throw new IllegalArgumentException("Invalid email");
        logger.info("Email sent");
        logger.debug("Email sent to " + email);
        return  "Email sent to " + email ;
    }
    
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<String> errorhandleException(final Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private static boolean validate(String emailStr) { 
        return VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr).find();
    }

}