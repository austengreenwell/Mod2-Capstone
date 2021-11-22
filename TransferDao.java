package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransferById(long id);

    List<Transfer> listAllTransfers(long accountId);

//    void updateStatus(long statusId);

    boolean transferTransaction(int transactionType, int transactionStatus,
                                long accountFrom, long accountTo, BigDecimal amount);

}
