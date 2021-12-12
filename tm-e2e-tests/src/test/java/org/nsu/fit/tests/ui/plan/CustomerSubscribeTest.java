package org.nsu.fit.tests.ui.plan;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class CustomerSubscribeTest {
    private Browser browser = null;
    private String login;
    private String pass;
    private RestClient restClient;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        restClient = new RestClient();
        login = "test@gmail.com";
        pass = "gfhjkm";
    }

    @Test(description = "Subscribe to the first Plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer feature")
    public void subscribeToFirstPlanTest() {
        new LoginScreen(browser)
                .loginAsCustomer(login, pass)
                .subscribeToFirst();

        List<PlanPojo> subscriptions = restClient.getAvailablePlans(login);

        assertNotNull(subscriptions);
        assertNotEquals(0, subscriptions.size());
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
