package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        long userId = userDao.findByUsername(principal.getName()).getId();
        long accountId = accountDao.getAccountByUserId(userId).getId();
        return transferDao.listAllTransfers(accountId);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public Transfer getTransferDetails(Principal principal) {
        long currentUserId = userDao.findIdByUsername(principal.getName());
        return transferDao.getTransferById(currentUserId);
    }

    @RequestMapping(path = "/execute", method = RequestMethod.POST)
    public boolean transfer(Principal principal, @RequestBody Transfer transfer) {
        long accountFrom = 0;
        long accountTo = 0;
        int transactionType = transfer.getTypeId();

        // REQUEST TRANSFER
        if (transactionType == 1) {

            // account to
            long userId = userDao.findByUsername(principal.getName()).getId();
            accountTo = accountDao.getAccountByUserId(userId).getId();

            // account from
            accountFrom = transfer.getAccountFrom();

            // SEND TRANSFER
        } else if (transactionType == 2) {

            // account to
            accountTo = transfer.getAccountTo();

            // account from
            long userId = userDao.findByUsername(principal.getName()).getId();
            accountFrom = accountDao.getAccountByUserId(userId).getId();
        }

        // amount
        BigDecimal amount = transfer.getAmount();
        int transactionStatus = transfer.getStatusId();
        return transferDao.transferTransaction(transactionType, transactionStatus, accountFrom, accountTo, amount);
    }
}



