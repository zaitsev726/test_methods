package org.nsu.fit.tests.ui.plan;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
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

public class DeletePlanTest {
    private Browser browser = null;
    private RestClient restClient;
    private AccountTokenPojo adminToken;
    private Faker faker;
    private PlanPojo plan;
    private AdminScreen result;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");

        faker = new Faker();
    }

    @Test(description = "Create plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan feature")
    public void createPlanTest() {
        String details = faker.commerce().productName();
        String name = faker.name().name();
        int fee = faker.number().numberBetween(1, 10);

        result = new LoginScreen(browser)
                .loginAsAdmin()
                .createPlan()
                .fillFee(fee)
                .fillName(name)
                .fillDetails(details)
                .clickSubmit();

        List<PlanPojo> plans = restClient.getPlans(adminToken);

        assertNotNull(plans);
        assertNotEquals(plans.size(), 0);

        for (PlanPojo planPojo : plans) {
            if (planPojo.details.equals(details) &&
                    planPojo.fee == fee &&
                    planPojo.name.equals(name)) {
                plan = planPojo;
            }
        }
        assertNotNull(plan);
    }

    @Test(description = "Delete plan via UI.",
            dependsOnMethods = {"createPlanTest"})
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plan feature.")
    public void deleteCustomerTest() {
        result.deleteLastPlan();
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
