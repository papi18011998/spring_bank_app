package com.example.digital_banking_backend.web;

import com.example.digital_banking_backend.dtos.CustomerDTO;
import com.example.digital_banking_backend.entities.Customer;
import com.example.digital_banking_backend.exception.CustomerNotFoundException;
import com.example.digital_banking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomer();
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }
    @PostMapping("/customers")
    public CustomerDTO save(@RequestBody CustomerDTO customerDTORequest){
        return bankAccountService.saveCustomer(customerDTORequest);
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(id);
        return  bankAccountService.updateCustomer(customerDTO);
    }
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
