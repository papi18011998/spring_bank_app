package com.example.digital_banking_backend;

import com.example.digital_banking_backend.entities.*;
import com.example.digital_banking_backend.enums.AccountStatus;
import com.example.digital_banking_backend.enums.OperationType;
import com.example.digital_banking_backend.exception.BalanceAccountInsufficentException;
import com.example.digital_banking_backend.exception.BankAccountException;
import com.example.digital_banking_backend.exception.CustomerNotFoundException;
import com.example.digital_banking_backend.repositories.AccountOperationRepository;
import com.example.digital_banking_backend.repositories.BankAccountRepository;
import com.example.digital_banking_backend.repositories.CustomerRepository;
import com.example.digital_banking_backend.services.BankAccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackendApplication.class, args);
    }

//    @Bean
//    CommandLineRunner start(CustomerRepository customerRepository,
//                            BankAccountRepository bankAccountRepository,
//                            AccountOperationRepository accountOperationRepository){
//        return args -> {
//            Stream.of("Papi","Cheikh","Jacques").forEach(name->{
//                Customer customer = new Customer();
//                customer.setName(name);
//                customer.setEmail(name.toLowerCase()+"@gmail.com");
//                customerRepository.save(customer);
//            });
//            customerRepository.findAll().forEach(customer -> {
//                    CurrentAccount currentAccount =new CurrentAccount();
//                    currentAccount.setId(UUID.randomUUID().toString());
//                    currentAccount.setCustomer(customer);
//                    currentAccount.setBalance(Math.random()*10000);
//                    currentAccount.setCreatedAt(new Date());
//                    currentAccount.setStatus(AccountStatus.CREATED);
//                    currentAccount.setOverdraft(1000000);
//                    bankAccountRepository.save(currentAccount);
//
//                    SavingAccount savingAccount =new SavingAccount();
//                    savingAccount.setId(UUID.randomUUID().toString());
//                    savingAccount.setCustomer(customer);
//                    savingAccount.setBalance(Math.random()*10000);
//                    savingAccount.setCreatedAt(new Date());
//                    savingAccount.setStatus(AccountStatus.CREATED);
//                    savingAccount.setInterestRate(9.8);
//                    bankAccountRepository.save(savingAccount);
//            });
//            bankAccountRepository.findAll().forEach(bankAccount -> {
//                AccountOperation debit = new AccountOperation();
//                for (int i=0;i<7;i++){
//                    AccountOperation accountOperation = new AccountOperation();
//                    accountOperation.setAmount(Math.random()*10000);
//                    accountOperation.setOperationDate(new Date());
//                    accountOperation.setOperationType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
//                    accountOperation.setBankAccount(bankAccount);
//                    accountOperationRepository.save(accountOperation);
//                }
//            });
//        };
//    }
//    @Bean
    CommandLineRunner start(BankAccountServiceImpl bankAccountService){
        return args -> {
            Stream.of("Mame Thierno","Miraj","Abdoul Karim").forEach(name->{
                Customer customer = new Customer();
                customer.setEmail(name.toLowerCase()+"@gmail.com");
                customer.setName(name);
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000,Math.random()*9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*9000,7.3, customer.getId());
                    bankAccountService.listBankAccount().forEach(bankAccount -> {
                        try {
                            for (int i=0;i<10;i++){
                                bankAccountService.credit(bankAccount.getId(), Math.random()*100000,"credit");
                                bankAccountService.debit(bankAccount.getId(), Math.random()*1000,"debit");
                            }
                        } catch (BankAccountException | BalanceAccountInsufficentException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        };
    }

}
