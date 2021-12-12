package org.nsu.fit.tests.api.subscription;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.services.rest.data.SubscriptionPojo;
import org.testng.annotations.Test;
import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class AllSubscriptionScenarioTest {
    private AccountTokenPojo customerToken;
    private SubscriptionPojo subscriptionPojo;
    private RestClient restClient;

    @Test(description = "Create subscription.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Subscription feature.")
    public void createSubscriptionTest() {
        restClient = new RestClient();
        String login = "test@gmail.com";
        customerToken = restClient.authenticate(login, "");
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
    }

    @Test(description = "Get subscriptions.",
            dependsOnMethods = "createSubscriptionTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Subscription feature.")
    public void getSubscriptionsTest() {
        AccountTokenPojo adminToken = restClient.authenticate("admin", "setup");

        List<String> result = restClient.getSubscriptions(adminToken, String.valueOf(customerToken.id));
        assertNotNull(result);
        assertNotEquals(result.size(), 0);
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

    @Test(description = "Delete plan",
            dependsOnMethods = {"createSubscriptionTest",
                    "getSubscriptionsTest",
                    "getAvailableSubscriptionsTest"})
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Subscription feature.")
    public void authAsCustomerTest() {
        restClient.deleteSubscription(subscriptionPojo, customerToken);
    }
}