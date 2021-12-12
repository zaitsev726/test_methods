package org.nsu.fit.tests.ui.customer;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class DeleteCustomerTest {
    private Browser browser = null;
    private RestClient restClient;
    private AccountTokenPojo adminToken;
    private Faker faker;
    private CustomerPojo customer;
    private AdminScreen result;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");
        faker = new Faker();
    }

    @Test(description = "Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {
        String customerFirstName = faker.name().firstName();
        String customerLastName = faker.name().lastName();
        String customerLogin = faker.internet().emailAddress();
        String customerPass = faker.internet().password(7, 11);

        result = new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customerLogin)
                .fillPassword(customerPass)
                .fillFirstName(customerFirstName)
                .fillLastName(customerLastName)
                .clickSubmit();

        List<CustomerPojo> customers = restClient.getCustomers(adminToken, customerLogin);

        assertNotNull(customers);
        assertNotEquals(customers.size(), 0);

        for (CustomerPojo customerPojo : customers) {
            if (customerPojo.firstName.equals(customerFirstName) &&
                    customerPojo.lastName.equals(customerLastName) &&
                    customerPojo.login.equals(customerLogin) &&
                    customerPojo.pass.equals(customerPass)) {
                customer = customerPojo;
            }
        }
        assertNotNull(customer);
    }

    @Test(description = "Delete customer via UI.",
            dependsOnMethods = {"createCustomer"})
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature.")
    public void deleteCustomerTest() {
        result.deleteLastCustomer();
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
