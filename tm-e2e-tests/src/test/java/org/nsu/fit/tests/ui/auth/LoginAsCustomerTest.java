package org.nsu.fit.tests.ui.auth;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;

public class LoginAsCustomerTest {
    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Login as Customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Authentication feature")
    public void loginAsCustomer() {
        new LoginScreen(browser).loginAsCustomer("test@gmail.com", "gfhjkm");
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
