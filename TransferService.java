package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();


    public Transfer[] listTransfers() {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.getForObject(API_BASE_URL + "transfer/all", Transfer[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getLocalizedMessage());
        }
        return transfers;
    }

    public BigDecimal viewBalance() {
        BigDecimal balance = null;
        try {
            balance = restTemplate.getForObject(API_BASE_URL + "account/balance", BigDecimal.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getLocalizedMessage());
        }
        return balance;
    }

    public boolean executeTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;

        try {
            restTemplate.postForObject(API_BASE_URL + "transfer/execute", entity, Boolean.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getLocalizedMessage());
        }
        return success;
    }

    public Account accountFromUsername (User userName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<User>(userName, headers);
        Account account = null;
        try{
            account = restTemplate.getForObject(API_BASE_URL + "account/other_users", Account.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getLocalizedMessage());
        }
        return account;
    }


}
