package com.example.demo.service;

import com.example.demo.dtos.*;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance , double interestRate , Long customerId) throws CustomerNotFoundException ;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId , double amount , String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId , double amount , String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource , String accountIdDestination , double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountDTOList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> historique(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
