package org.nsu.fit.tests.api.subscription;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.services.rest.data.SubscriptionPojo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

import static org.testng.Assert.*;

@Test(priority = 1)
public class GetAvailableSubscriptionsTest {
    private AccountTokenPojo customerToken;
    private SubscriptionPojo subscriptionPojo;
    private RestClient restClient;
    private String login;

    @BeforeClass
    public void beforeClass() {
        restClient = new RestClient();
        login = "test@gmail.com";
        customerToken = restClient.authenticate(login, "");
    }

    @Test(description = "Create subscription.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Subscription feature.")
    public void createSubscriptionTest() {
        SubscriptionPojo newSubscriptionPojo = new SubscriptionPojo();
        List<PlanPojo> availablePlans = restClient.getAvailablePlans(login);
        assertNotNull(availablePlans);
        assertNotEquals(availablePlans.size(), 0);

        PlanPojo plan = availablePlans.get(0);
        newSubscriptionPojo.planId = plan.id;
        newSubscriptionPojo.planName = plan.name;
        newSubscriptionPojo.planDetails = plan.details;
        newSubscriptionPojo.planFee = plan.fee;


        subscriptionPojo = restClient.createSubscriptionPojo(newSubscriptionPojo, customerToken);
        assertNotNull(subscriptionPojo);
        assertEquals(subscriptionPojo.planId, newSubscriptionPojo.planId);
        assertEquals(subscriptionPojo.planName, newSubscriptionPojo.planName);
        assertEquals(subscriptionPojo.planDetails, newSubscriptionPojo.planDetails);
        assertEquals(subscriptionPojo.planFee, newSubscriptionPojo.planFee);
    }

    @Test(description = "Get available subscriptions.",
            dependsOnMethods = "createSubscriptionTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Subscription feature.")
    public void getAvailableSubscriptionsTest() {
        List<String> result = restClient.getAvailableSubscriptions(customerToken);
        assertNotNull(result);
        assertNotEquals(result.size(), 0);
    }

    @AfterClass
    public void afterClass() {
        restClient.deleteSubscription(subscriptionPojo, customerToken);
    }
}