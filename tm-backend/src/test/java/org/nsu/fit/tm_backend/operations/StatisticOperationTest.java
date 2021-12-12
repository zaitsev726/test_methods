package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StatisticOperationTest {
    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.
    @Test
    void testStatisticOperationInitWithNullCustomerManager() {
        CustomerManager customerManager = null;
        SubscriptionManager subscriptionManager = mock(SubscriptionManager.class);
        List<UUID> customersIds = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, customersIds));
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void testStatisticOperationInitWithNullSubscriptionManager() {
        CustomerManager customerManager = mock(CustomerManager.class);
        SubscriptionManager subscriptionManager = null;
        List<UUID> customersIds = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, customersIds));
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void testStatisticOperationInitWithNullCustomerIds() {
        CustomerManager customerManager = mock(CustomerManager.class);
        SubscriptionManager subscriptionManager = mock(SubscriptionManager.class);
        List<UUID> customersIds = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, customersIds));
        assertEquals("customerIds", exception.getMessage());
    }

    private CustomerManager customerManager;
    private SubscriptionManager subscriptionManager;

    private StatisticOperation statisticOperation;

    private List<UUID> customerIds;
    private List<CustomerPojo> customers;
    private List<SubscriptionPojo> subscriptions;

    @BeforeEach
    void init() {
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);

        customers = new ArrayList<>();
        customerIds = new ArrayList<>();
        subscriptions = new ArrayList<>();

        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "Baba_Jaga";
        customer.balance = 500;

        SubscriptionPojo subscriptionPojo = new SubscriptionPojo();
        subscriptionPojo.id = UUID.randomUUID();
        subscriptionPojo.customerId = customer.id;
        subscriptionPojo.planId = UUID.randomUUID();
        subscriptionPojo.planName = "plan_name";
        subscriptionPojo.planDetails = "plan_details";
        subscriptionPojo.planFee = 501;

        customers.add(customer);
        customerIds.add(customer.id);
        subscriptions.add(subscriptionPojo);

        customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Cena";
        customer.login = "john_cena@google.com";
        customer.pass = "password";
        customer.balance = 1000;

        subscriptionPojo = new SubscriptionPojo();
        subscriptionPojo.id = UUID.randomUUID();
        subscriptionPojo.customerId = customer.id;
        subscriptionPojo.planId = UUID.randomUUID();
        subscriptionPojo.planName = "plan_name_2";
        subscriptionPojo.planDetails = "plan_details_2";
        subscriptionPojo.planFee = 1001;

        customers.add(customer);
        customerIds.add(customer.id);
        subscriptions.add(subscriptionPojo);

        statisticOperation = new StatisticOperation(customerManager, subscriptionManager, customerIds);
    }

    @Test
    void testStatisticOperationResultWithCustomersAndWithSubscriptions() {
        when(customerManager.getCustomer(customers.get(0).id)).thenReturn(customers.get(0));
        when(customerManager.getCustomer(customers.get(1).id)).thenReturn(customers.get(1));

        when(subscriptionManager.getSubscriptions(customerIds.get(0))).thenReturn(Collections.singletonList(subscriptions.get(0)));
        when(subscriptionManager.getSubscriptions(customerIds.get(1))).thenReturn(Collections.singletonList(subscriptions.get(1)));

        StatisticOperation.StatisticOperationResult result = statisticOperation.Execute();

        assertEquals(1500, result.overallBalance);
        assertEquals(1502, result.overallFee);

        verify(customerManager, times(2)).getCustomer(any(UUID.class));
        verify(subscriptionManager, times(2)).getSubscriptions(any(UUID.class));
        verifyNoMoreInteractions(customerManager, subscriptionManager);
    }
}
