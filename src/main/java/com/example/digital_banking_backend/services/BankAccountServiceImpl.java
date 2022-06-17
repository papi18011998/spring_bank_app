package com.example.digital_banking_backend.services;

import com.example.digital_banking_backend.dtos.CustomerDTO;
import com.example.digital_banking_backend.entities.*;
import com.example.digital_banking_backend.enums.OperationType;
import com.example.digital_banking_backend.exception.BalanceAccountInsufficentException;
import com.example.digital_banking_backend.exception.BankAccountException;
import com.example.digital_banking_backend.exception.CustomerNotFoundException;
import com.example.digital_banking_backend.mappers.BankAccountMapperImpl;
import com.example.digital_banking_backend.repositories.AccountOperationRepository;
import com.example.digital_banking_backend.repositories.BankAccountRepository;
import com.example.digital_banking_backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountMapperImpl bankAccountMapper;
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
//    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer ...");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        log.info("Saving new bank account ...");
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }
        CurrentAccount bankAccount = new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setOverdraft(overDraft);
        bankAccount.setCustomer(customer);
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        log.info("Saving new bank account ...");
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null){
            throw  new CustomerNotFoundException("Customer not found with id: " + customerId);
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        bankAccountRepository.save(savingAccount);
        return savingAccount;
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
//        List<CustomerDTO> customerDTO = new ArrayList<>();
//        customers.forEach(customer -> {
//            customerDTO.add(bankAccountMapper.fromCustomer(customer));
//        });
        List<CustomerDTO> customerDTO = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer))
                                                            .collect(Collectors.toList());
        return customerDTO;
    }

    @Override
    public List<BankAccount> listBankAccount() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount getAccount(String accountId) throws BankAccountException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountException("This bank is not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountException, BalanceAccountInsufficentException {
        BankAccount bankAccount = getAccount(accountId);
        if(bankAccount.getBalance()<amount){
            throw new BalanceAccountInsufficentException("Balance is not sufficient");
        }
            AccountOperation accountOperation =new AccountOperation();
            accountOperation.setBankAccount(bankAccount);
            accountOperation.setOperationType(OperationType.DEBIT);
            accountOperation.setOperationDate(new Date());
            accountOperation.setDescription(description);
            accountOperation.setAmount(amount);
            bankAccount.setBalance(bankAccount.getBalance()-amount);
            accountOperationRepository.save(accountOperation);
            bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountException {
        BankAccount bankAccount = getAccount(accountId);

            AccountOperation accountOperation =new AccountOperation();
            accountOperation.setBankAccount(bankAccount);
            accountOperation.setOperationType(OperationType.CREDIT);
            accountOperation.setOperationDate(new Date());
            accountOperation.setDescription(description);
            accountOperation.setAmount(amount);
            bankAccount.setBalance(bankAccount.getBalance()+amount);
            accountOperationRepository.save(accountOperation);
            bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountException, BalanceAccountInsufficentException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from"+ accountIdSource);
    }
}
