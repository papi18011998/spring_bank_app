package com.example.digital_banking_backend.web;

import com.example.digital_banking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data @AllArgsConstructor
public class BankAccountRestController {
    private BankAccountService bankAccountService;

}
