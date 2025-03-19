package com.leniolabs.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leniolabs.challenge.calculator.FeeCalculatorIF;
import com.leniolabs.challenge.calculator.factory.FeeCalculatorFactory;
import com.leniolabs.challenge.model.Account;
import com.leniolabs.challenge.service.AccounServiceIF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AccounServiceIF accountControllerService;

    @Mock
    private FeeCalculatorFactory feeCalculatorFactory;

    @InjectMocks
    private AccountController accountController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        // Arrange
        Account account = new Account("1", "John", "Doe", "personal", 1000.0);

        when(accountControllerService.createAccount(any(Account.class))).thenReturn("1");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/lenio-challenge/account/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andReturn();

        verify(accountControllerService, times(1)).createAccount(any(Account.class));
    }

    @Test
    void shouldCalculateCorporateFee() throws Exception {
        // Arrange
        Account account = new Account("2", "Jane", "Smith", "corporate", 2000.0);

        FeeCalculatorIF feeCalculator = mock(FeeCalculatorIF.class);
        when(accountControllerService.findById("2")).thenReturn(Optional.of(account));
        when(feeCalculatorFactory.getFeeCalculator("corporate")).thenReturn(feeCalculator);
        when(feeCalculator.calculateFee()).thenReturn(0.05);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/lenio-challenge/account/v1/calculate-fee/2"))
                .andExpect(status().isOk())
                .andReturn();

        verify(accountControllerService, times(1)).findById("2");
        verify(feeCalculatorFactory, times(1)).getFeeCalculator("corporate");
        verify(feeCalculator, times(1)).calculateFee();
    }

    @Test
    void shouldCalculatePersonalFee() throws Exception {
        // Arrange
        Account account = new Account("3", "Cristal", "Smith", "personal", 2000.0);

        FeeCalculatorIF feeCalculator = mock(FeeCalculatorIF.class);
        when(accountControllerService.findById("3")).thenReturn(Optional.of(account));
        when(feeCalculatorFactory.getFeeCalculator("personal")).thenReturn(feeCalculator);
        when(feeCalculator.calculateFee()).thenReturn(0.01);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/lenio-challenge/account/v1/calculate-fee/3"))
                .andExpect(status().isOk())
                .andReturn();

        verify(accountControllerService, times(1)).findById("3");
        verify(feeCalculatorFactory, times(1)).getFeeCalculator("personal");
        verify(feeCalculator, times(1)).calculateFee();
    }

    @Test
    void testCalculateFeeAccountNotFound() throws Exception {
        // Arrange
        when(accountControllerService.findById("4")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/lenio-challenge/account/v1/calculate-fee/4"))
                .andExpect(status().isNotFound());

        verify(accountControllerService, times(1)).findById("4"); // Use the correct ID here
    }


}
