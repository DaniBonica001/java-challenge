package com.leniolabs.challenge.calculator.factory;


import com.leniolabs.challenge.calculator.FeeCalculatorIF;
import com.leniolabs.challenge.custom.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FeeCalculatorFactory {

    private final Map<String, FeeCalculatorIF> feeCalculators;

    @Autowired
    public FeeCalculatorFactory(List<FeeCalculatorIF> feeCalculators) {
        this.feeCalculators = feeCalculators.stream()
                .collect(Collectors.toMap(
                        calc -> calc.getClass().getAnnotation(AccountType.class).value(),
                        Function.identity()
                ));
    }

    public FeeCalculatorIF getFeeCalculator(String accountType) {
        return Optional.ofNullable(feeCalculators.get(accountType))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account type"));
    }
}
