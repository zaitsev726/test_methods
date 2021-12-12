package org.nsu.fit.tests.api.customer;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test(priority = 6)
public class CreateCustomerTest {
    private RestClient restClient;
    private Faker faker;

    private AccountTokenPojo adminToken;
    private CustomerPojo customerPojo;

    @BeforeClass
    public void beforeClass() {
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");

        faker = new Faker();
    }

    @Test(description = "Create customer.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature.")
    public void createCustomerTest() {
        CustomerPojo newCustomerPojo = new CustomerPojo();
        newCustomerPojo.firstName = faker.name().firstName();
        newCustomerPojo.lastName = faker.name().lastName();
        newCustomerPojo.login = faker.internet().emailAddress();
        newCustomerPojo.balance = faker.number().numberBetween(0, 1000);
        newCustomerPojo.pass = faker.internet().password(7, 11);

        customerPojo = restClient.createCustomer(newCustomerPojo, adminToken);
        assertNotNull(customerPojo);
        assertEquals(customerPojo.firstName, newCustomerPojo.firstName);
        assertEquals(customerPojo.lastName, newCustomerPojo.lastName);
        assertEquals(customerPojo.login, newCustomerPojo.login);
        assertEquals(customerPojo.balance, newCustomerPojo.balance);
    }

    @AfterClass
    public void afterClass() {
        restClient.deleteCustomer(adminToken, customerPojo);
    }
}