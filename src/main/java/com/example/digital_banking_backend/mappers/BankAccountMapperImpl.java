package com.example.digital_banking_backend.mappers;

import com.example.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.example.digital_banking_backend.dtos.CustomerDTO;
import com.example.digital_banking_backend.dtos.SavingBankAccountDTO;
import com.example.digital_banking_backend.entities.CurrentAccount;
import com.example.digital_banking_backend.entities.Customer;
import com.example.digital_banking_backend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
// La methode manuelle
//        customerDTO.setId(customerDTO.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public  Customer formCustomeDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }
    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        return savingBankAccountDTO;
    }
    public  SavingAccount fromSavingAccoutDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(formCustomeDTO(savingBankAccountDTO.getCustomerDTO()));
        return  savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        return currentBankAccountDTO;
    }
    public CurrentAccount formCurrentAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(formCustomeDTO(currentBankAccountDTO.getCustomerDTO()));
        return  currentAccount;
    }
}
