package com.leniolabs.challenge.service;

import com.leniolabs.challenge.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountServiceImplTest {

    private AccountServiceImpl accountServiceImpl;

    @BeforeEach
    public void init() {
        accountServiceImpl = new AccountServiceImpl();
    }

    @Test
    void shouldCreatePersonalAccount() {
        //Arrange
        Account account = new Account("1","pepe","flores", "personal", 100.0);
        //Act
        String id = accountServiceImpl.createAccount(account);

        //Assert
        assertEquals("1", id);
        assertTrue(accountServiceImpl.findById("1").isPresent());

    }

    @Test
    void shouldCreateCorporateAccount() {
        //Arrange
        Account account = new Account("2","pepe","flores", "corporate", 100.0);
        //Act
        String id = accountServiceImpl.createAccount(account);

        //Assert
        assertEquals("2", id);
        assertTrue(accountServiceImpl.findById("2").isPresent());
    }

    @Test
    void shouldFindAccount(){
        // Arrange
        Account account = new Account("3","pepe","flores", "personal", 100.0);
        accountServiceImpl.createAccount(account);

        // Act
        Optional<Account> result = accountServiceImpl.findById("3");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("3", result.get().getId());
        assertEquals("pepe", result.get().getName());
        assertEquals("flores", result.get().getLastName());
        assertEquals("personal", result.get().getAccountType());
        assertEquals(100.0, result.get().getBalance());
    }

    @Test
    void shouldNotFindAccount(){
        // Act
        Optional<Account> result = accountServiceImpl.findById("3");

        // Assert
        assertFalse(result.isPresent());
    }




}
