package org.nsu.fit.tests.api.customer;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class AllCustomerScenarioTest {
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
    }

    @Test(description = "Get customers.",
            dependsOnMethods = "createCustomerTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature.")
    public void getCustomersTest() {
        List<CustomerPojo> result = restClient.getCustomers(adminToken, customerPojo.login);
        assertNotNull(result);
        assertNotEquals(result.size(), 0);
    }

    @Test(description = "Top up customer balance.",
            dependsOnMethods = "createCustomerTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature.")
    public void topUpCustomerBalanceTest() {
        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = customerPojo.id;
        topUpBalancePojo.money = 10_000;

        AccountTokenPojo customerToken = restClient.authenticate(customerPojo.login, "");

        restClient.topUpCustomerBalance(customerToken, topUpBalancePojo);
    }

    @Test(description = "Delete customer",
            dependsOnMethods = {
                    "createCustomerTest",
                    "getCustomersTest",
                    "topUpCustomerBalanceTest"})
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature.")
    public void deleteCustomerTest() {
        restClient.deleteCustomer(adminToken, customerPojo);
    }
}