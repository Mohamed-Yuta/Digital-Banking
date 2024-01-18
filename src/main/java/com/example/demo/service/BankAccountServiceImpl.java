package com.example.demo.service;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mappers.BankAccountMapperImpl;
import com.example.demo.repositories.AccountOperationRepository;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankAccountServiceImpl implements  BankAccountService {
    @Autowired
    private CustomerRepository customerRepository ;
    @Autowired
    private BankAccountRepository bankAccountRepository ;
    @Autowired
    private AccountOperationRepository accountOperationRepository ;
    @Autowired
    private BankAccountMapperImpl dtoMapper;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        log.info("Saving new Customer");

        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer1 = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer1) ;
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null ) throw new CustomerNotFoundException("Customer not found ");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount) ;
    }
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null ) throw new CustomerNotFoundException("Customer not found ");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedAccount);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS =customers.stream().map(cust-> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS ;
    };

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found "));
        if(bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;

            return (BankAccountDTO) dtoMapper.fromSavingBankAccount(savingAccount);
        }
        else return (BankAccountDTO) dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found "));

        if ( amount > bankAccount.getBalance()) {
            throw  new BalanceNotSufficientException("balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found "));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);

        bankAccount.setBalance(bankAccount.getBalance()+amount);

        accountOperationRepository.save(accountOperation);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource,amount,"Virement");
        credit(accountIdDestination,amount,"virement");

    }
    @Override
    public List<BankAccountDTO> bankAccountDTOList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOList = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }
            else {
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOList ;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

        log.info("Saving new Customer");

        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer1 = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer1) ;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
        Customer customer = new Customer() ;
            }

    @Override
    public List<AccountOperationDTO> historique(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    }
    @Override
    public AccountHistoryDTO getAccountHistory(String accountId , int page , int size ) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("account not found");
        Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDto = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS= accountOperations.getContent().stream().map(op-> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

        accountHistoryDto.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto ;


    }
}
