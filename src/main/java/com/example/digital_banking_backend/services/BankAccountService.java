package com.example.digital_banking_backend.services;

import com.example.digital_banking_backend.entities.BankAccount;
import com.example.digital_banking_backend.entities.CurrentAccount;
import com.example.digital_banking_backend.entities.Customer;
import com.example.digital_banking_backend.entities.SavingAccount;
import com.example.digital_banking_backend.exception.BalanceAccountInsufficentException;
import com.example.digital_banking_backend.exception.BankAccountException;
import com.example.digital_banking_backend.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    public Customer saveCustomer(Customer customer);
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    public List<Customer> listCustomer();
    public List<BankAccount> listBankAccount();
    public BankAccount getAccount(String accountId) throws BankAccountException;
    public void debit(String accountId,double amount,String description) throws BankAccountException, BalanceAccountInsufficentException;
    public void credit(String accountId,double amount,String description) throws BankAccountException;
    public void transfert(String accountIdSource,String accountIdDestination,double amount) throws BankAccountException, BalanceAccountInsufficentException;
}
