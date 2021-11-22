package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(long id) {
        Transfer  transfer = null;
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?; ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        if(results.next()){
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> listAllTransfers(long accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ?  ; ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }


    @Override
    public boolean transferTransaction(int transactionType, int transactionStatus,long accountFrom, long accountTo, BigDecimal amount) {

        try {
            jdbcTemplate.execute("BEGIN TRANSACTION");

            // Transfer -- from_account
            String sql = "SELECT balance FROM accounts WHERE account_id = ?;";
            BigDecimal accountFromBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountFrom);
            if (accountFromBalance.compareTo(amount) >= 0) {
                String sq = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
                jdbcTemplate.update(sq, amount, accountFrom);

            } else {
                System.out.println("Insufficient funds for this transfer.");
                return false;
            }

            // Transfer -- to_account
            String sqlString = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";
            jdbcTemplate.update(sqlString, amount, accountTo);

        } catch (DataAccessException e) {
            System.out.println("Error:" + e.getLocalizedMessage());
            jdbcTemplate.execute("ROLLBACK");
            return false;
        }
        jdbcTemplate.execute("COMMIT");
        return true;
    }


    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getLong("transfer_id"));
        transfer.setTypeId(results.getInt("transfer_type_id"));
        transfer.setStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }


}
// TODO complete if we have time, Optional Use Case -Nick
//    @Override
//    public void updateStatus(long statusId) {
//        String sql = "UPDATE transfers SET transfer_status_id = ? " +
//                "WHERE transfer_id  = ?";
//
//    }