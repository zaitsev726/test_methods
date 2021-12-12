package org.nsu.fit.tm_backend.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполенния метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).getCustomers();
    }

    // Как не надо писать тест...
    @Test
    void testCreateCustomerWithNullArgument_Wrong() {
        try {
            customerManager.createCustomer(null);
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument 'customer' is null.", ex.getMessage());
        }
    }

    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithNullPass() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = null;
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testCreateCustomerWithShortPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "pass";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }


    @Test
    void testCreateCustomerWithLongPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "passwordpassword";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testCreateCustomerWithEasyPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testCreateCustomerWithNullLogin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = null;
        createCustomerInput.pass = "password";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.login' is null.", exception.getMessage());
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testCreateCustomerWithSameEmail() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "password";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.getCustomerByLogin(createCustomerInput.login)).thenReturn(createCustomerOutput);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Customer with this login already exists.", exception.getMessage());
        verify(dbService, times(1)).getCustomerByLogin(createCustomerInput.login);
        verify(dbService, times(0)).createCustomer(createCustomerInput);
    }

    @Test
    void testGetCustomers() {
        CustomerPojo customer1 = new CustomerPojo();
        customer1.id = UUID.randomUUID();
        customer1.firstName = "John";
        customer1.lastName = "Wick";
        customer1.login = "john_wick@example.com";
        customer1.pass = "Baba_Jaga";
        customer1.balance = 0;

        CustomerPojo customer2 = new CustomerPojo();
        customer2.id = UUID.randomUUID();
        customer2.firstName = "John_1";
        customer2.lastName = "Wick_2";
        customer2.login = "john_1_wick@example.com";
        customer2.pass = "Baba_Jaga_2";
        customer2.balance = 0;
        ArrayList<CustomerPojo> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);


        when(dbService.getCustomers()).thenReturn(customers);
        List<CustomerPojo> result = customerManager.getCustomers();

        assertEquals(customers, result);
        verify(dbService, times(1)).getCustomers();
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testGetCustomer() {
        UUID uuid = UUID.randomUUID();

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = uuid;
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.getCustomer(uuid)).thenReturn(createCustomerOutput);
        CustomerPojo result = customerManager.getCustomer(uuid);

        assertEquals(createCustomerOutput, result);
        verify(dbService, times(1)).getCustomer(uuid);
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testLookupCustomerWithExistedLogin() {
        CustomerPojo customer1 = new CustomerPojo();
        customer1.id = UUID.randomUUID();
        customer1.firstName = "John";
        customer1.lastName = "Wick";
        customer1.login = "john_wick@example.com";
        customer1.pass = "Baba_Jaga";
        customer1.balance = 0;

        CustomerPojo customer2 = new CustomerPojo();
        customer2.id = UUID.randomUUID();
        customer2.firstName = "John_1";
        customer2.lastName = "Wick_2";
        customer2.login = "john_1_wick@example.com";
        customer2.pass = "Baba_Jaga_2";
        customer2.balance = 0;
        ArrayList<CustomerPojo> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);


        when(dbService.getCustomers()).thenReturn(customers);
        CustomerPojo result = customerManager.lookupCustomer("john_wick@example.com");

        assertEquals(customer1, result);
        verify(dbService, times(1)).getCustomers();
        verify(dbService, times(0)).getCustomer(customer1.id);
        verify(dbService, times(0)).getCustomer(customer2.id);
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testLookupCustomerWithNonExistedLogin() {
        CustomerPojo customer1 = new CustomerPojo();
        customer1.id = UUID.randomUUID();
        customer1.firstName = "John";
        customer1.lastName = "Wick";
        customer1.login = "john_wick@example.com";
        customer1.pass = "Baba_Jaga";
        customer1.balance = 0;

        CustomerPojo customer2 = new CustomerPojo();
        customer2.id = UUID.randomUUID();
        customer2.firstName = "John_1";
        customer2.lastName = "Wick_2";
        customer2.login = "john_1_wick@example.com";
        customer2.pass = "Baba_Jaga_2";
        customer2.balance = 0;
        ArrayList<CustomerPojo> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);


        when(dbService.getCustomers()).thenReturn(customers);
        CustomerPojo result = customerManager.lookupCustomer("john_wick@example.com_123");

        assertNull(result);
        verify(dbService, times(1)).getCustomers();
        verify(dbService, times(0)).getCustomer(customer1.id);
        verify(dbService, times(0)).getCustomer(customer2.id);
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin(createCustomerInput.login);
    }

    @Test
    void testMeAdminLogin() {
        AuthenticatedUserDetails auth = new AuthenticatedUserDetails(
                "UUID",
                "john_wick@example.com",
                Collections.singleton("ADMIN")
        );

        ContactPojo result = customerManager.me(auth);
        assertEquals(Globals.ADMIN_LOGIN, result.login);
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(0)).getCustomerByLogin("john_wick@example.com");
    }

    @Test
    void testMeUserLogin_Wrong() {
        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.getCustomerByLogin("john_wick@example.com")).thenReturn(createCustomerOutput);

        AuthenticatedUserDetails auth = new AuthenticatedUserDetails(
                "UUID",
                "john_wick@example.com",
                Collections.singleton("USER")
        );

        ContactPojo result = customerManager.me(auth);
        assertEquals(0, result.balance);
        assertNull(result.firstName);
        assertNull(result.lastName);
        assertEquals(createCustomerOutput.login, result.login);
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(0)).createCustomer(createCustomerInput);
        verify(dbService, times(1)).getCustomerByLogin("john_wick@example.com");
    }

    @Test
    void testDeleteCustomerWithNullUUID() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.deleteCustomer(null));
        assertEquals("Customer UUID can not be null.", exception.getMessage());
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(1)).deleteCustomer(null);
    }

    @Test
    void testDeleteNotExistedCustomerWithUUID() {
        UUID uuid = UUID.randomUUID();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.deleteCustomer(uuid));
        assertEquals("Customer UUID does not exists.", exception.getMessage());
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(1)).deleteCustomer(uuid);
    }

    @Test
    void testDeleteExistedCustomerWithUUID() {
        CustomerPojo customerToDelete = new CustomerPojo();
        customerToDelete.id = UUID.randomUUID();
        customerToDelete.firstName = "John";
        customerToDelete.lastName = "Wick";
        customerToDelete.login = "john_wick@example.com";
        customerToDelete.pass = "Baba_Jaga";
        customerToDelete.balance = 0;


        customerManager.deleteCustomer(customerToDelete.id);
        verify(dbService, times(1)).deleteCustomer(customerToDelete.id);
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(0)).createCustomer(customerToDelete);
    }

    @Test
    void testTopUpBalanceNonExistedCustomer() {
        UUID uuid = UUID.randomUUID();
        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = uuid;
        topUpBalancePojo.money = 12345;

        when(dbService.getCustomer(uuid)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.topUpBalance(topUpBalancePojo));
        assertEquals("Customer UUID does not exists.", exception.getMessage());
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(1)).getCustomer(topUpBalancePojo.customerId);
    }

    @Test
    void testTopUpBalanceWithNegativeBalance() {
        UUID uuid = UUID.randomUUID();
        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = uuid;
        topUpBalancePojo.money = -12345;

        CustomerPojo customer = new CustomerPojo();
        customer.id = uuid;
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "Baba_Jaga";
        customer.balance = 132;

        when(dbService.getCustomer(uuid)).thenReturn(customer);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.topUpBalance(topUpBalancePojo));
        assertEquals("Top up balance money can not be negative.", exception.getMessage());
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(1)).getCustomer(topUpBalancePojo.customerId);
        verify(dbService, times(1)).editCustomer(customer);
    }

    @Test
    void testTopUpBalanceWithPositiveBalance() {
        UUID uuid = UUID.randomUUID();
        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = uuid;
        topUpBalancePojo.money = 12345;

        CustomerPojo customer = new CustomerPojo();
        customer.id = uuid;
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "Baba_Jaga";
        customer.balance = 132;

        when(dbService.getCustomer(uuid)).thenReturn(customer);

        CustomerPojo result = customerManager.topUpBalance(topUpBalancePojo);
        assertEquals(customer.login, result.login);
        assertEquals(customer.balance + topUpBalancePojo.money, result.balance);
        verify(dbService, times(1)).editCustomer(customer);
        verify(dbService, times(0)).getCustomers();
        verify(dbService, times(1)).getCustomer(topUpBalancePojo.customerId);
    }

}
