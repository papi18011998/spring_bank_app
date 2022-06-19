package com.example.digital_banking_backend.dtos;

import com.example.digital_banking_backend.entities.AccountOperation;
import com.example.digital_banking_backend.entities.Customer;
import com.example.digital_banking_backend.enums.AccountStatus;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccount {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;

}
