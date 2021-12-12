package org.nsu.fit.tests.api.plan;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test(priority = 4)
public class DeletePlanTest {
    private RestClient restClient;
    private Faker faker;

    private AccountTokenPojo adminToken;
    private PlanPojo planPojo;

    @BeforeClass
    public void beforeClass() {
        restClient = new RestClient();
        adminToken = restClient.authenticate("admin", "setup");

        faker = new Faker();
    }

    @Test(description = "Create plan.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plan feature.")
    public void createPlanTest() {
        PlanPojo newPlanPojo = new PlanPojo();
        newPlanPojo.details = faker.commerce().productName();
        newPlanPojo.name = faker.name().name();
        newPlanPojo.fee = faker.number().numberBetween(1, 10);

        planPojo = restClient.createPlanPojo(newPlanPojo, adminToken);
        assertNotNull(planPojo);
        assertEquals(planPojo.details, newPlanPojo.details);
        assertEquals(planPojo.name, newPlanPojo.name);
        assertEquals(planPojo.fee, newPlanPojo.fee);
    }

    @Test(description = "Delete plan", dependsOnMethods = {"createPlanTest"})
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Plan feature.")
    public void authAsCustomerTest() {
        restClient.deletePlanPojo(planPojo, adminToken);
    }
}
