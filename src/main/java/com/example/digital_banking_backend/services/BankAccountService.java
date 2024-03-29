package com.example.digital_banking_backend.services;

import com.example.digital_banking_backend.dtos.BankAccountDTO;
import com.example.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.example.digital_banking_backend.dtos.CustomerDTO;
import com.example.digital_banking_backend.dtos.SavingBankAccountDTO;
import com.example.digital_banking_backend.entities.BankAccount;
import com.example.digital_banking_backend.exception.BalanceAccountInsufficentException;
import com.example.digital_banking_backend.exception.BankAccountException;
import com.example.digital_banking_backend.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    public CustomerDTO saveCustomer(CustomerDTO customerDTO);
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    public List<CustomerDTO> listCustomer();
    public List<BankAccount> listBankAccount();
    public BankAccountDTO getAccount(String accountId) throws BankAccountException;
    public void debit(String accountId,double amount,String description) throws BankAccountException, BalanceAccountInsufficentException;
    public void credit(String accountId,double amount,String description) throws BankAccountException;
    public void transfert(String accountIdSource,String accountIdDestination,double amount) throws BankAccountException, BalanceAccountInsufficentException;

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    public void deleteCustomer(Long accountId);
    public CustomerDTO updateCustomer(CustomerDTO customerDTO);
}
