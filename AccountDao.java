package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccountByUserId (long id);

    long getUserId (long id);

    List<Account> findAll();

    BigDecimal viewBalance (long id);

}
