package com.example.digital_banking_backend.services;

import com.example.digital_banking_backend.dtos.BankAccountDTO;
import com.example.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.example.digital_banking_backend.dtos.CustomerDTO;
import com.example.digital_banking_backend.dtos.SavingBankAccountDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer ...");
        Customer savedCustomer = bankAccountMapper.formCustomeDTO(customerDTO);
        customerRepository.save(savedCustomer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
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
        return bankAccountMapper.fromCurrentCurrentBankAccount(bankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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
        return bankAccountMapper.fromSavingAccount(savingAccount);
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
    public BankAccountDTO getAccount(String accountId) throws BankAccountException {
//        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountException("This bank is not found"));
//        if(bankAccount instanceof  SavingAccount savingAccount){
//            return bankAccountMapper.fromSavingAccount(savingAccount);
//        }else {
//            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
//            return bankAccountMapper.fromCurrentCurrentBankAccount(currentAccount);
//        }
        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountException, BalanceAccountInsufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountException("This bank is not found"));
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
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountException("This bank is not found"));

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
    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public void deleteCustomer(Long accountId) {
        customerRepository.deleteById(accountId);
    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating new customer ...");
        Customer savedCustomer = bankAccountMapper.formCustomeDTO(customerDTO);
        customerRepository.save(savedCustomer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

}
