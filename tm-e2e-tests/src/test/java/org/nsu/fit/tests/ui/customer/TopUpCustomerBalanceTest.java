package org.nsu.fit.tests.ui.customer;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import static org.testng.AssertJUnit.assertEquals;

public class TopUpCustomerBalanceTest {
    private Browser browser = null;
    private String login;
    private String pass;
    private AccountTokenPojo accountToken;
    private RestClient restClient;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        restClient = new RestClient();
        login = "test@gmail.com";
        pass = "gfhjkm";
        accountToken = restClient.authenticate(login, pass);
    }

    @Test(description = "Login as Customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature")
    public void topUpCustomerBalance() {
        int topUpSum = new Faker().number().numberBetween(1, 10);
        CustomerPojo customerPojo = restClient.getMe(accountToken, CustomerPojo.class);
        new LoginScreen(browser)
                .loginAsCustomer(login, pass)
                .topUpBalance()
                .fillMoney(topUpSum)
                .clickSubmit();

        assertEquals(customerPojo.balance + topUpSum,
                restClient.getMe(accountToken, CustomerPojo.class).balance);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
