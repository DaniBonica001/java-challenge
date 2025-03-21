package com.leniolabs.challenge.controller;

import com.leniolabs.challenge.calculator.FeeCalculatorIF;
import com.leniolabs.challenge.calculator.factory.FeeCalculatorFactory;
import com.leniolabs.challenge.model.Account;
import com.leniolabs.challenge.service.AccounServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/lenio-challenge/account/v1")
public class AccountController {

    @Autowired
    private AccounServiceIF accountControllerService;

    @Autowired
    private FeeCalculatorFactory feeCalculatorFactory;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        String accountId = accountControllerService.createAccount(account);
        return ResponseEntity.ok(accountId);
    }

    @GetMapping(value = "/calculate-fee/{accountId}")
    public ResponseEntity<Double> calculateFee(@PathVariable String accountId) throws Exception {
        Account account = accountControllerService.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        FeeCalculatorIF feeCalculator = feeCalculatorFactory.getFeeCalculator(account.getAccountType());
        Double fee = feeCalculator.calculateFee();

        return ResponseEntity.ok(fee);
    }
}
