package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
public class AccountController {


    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }



    @RequestMapping(path = "/user",method = RequestMethod.GET)
    public Account getAccountByUserId(Principal principal) {
        long currentUserId = userDao.findIdByUsername(principal.getName());
        return accountDao.getAccountByUserId(currentUserId);
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        long currentUserId = userDao.findIdByUsername(principal.getName());
        return accountDao.viewBalance(currentUserId);
    }

    @RequestMapping(path = "/other_users", method = RequestMethod.GET)
    public Account getAccountByUsername(User userName){
        long currentUserId = userDao.findIdByUsername(userName.getUsername());
        return accountDao.getAccountByUserId(currentUserId);
    }
}
