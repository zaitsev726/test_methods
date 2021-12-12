package org.nsu.fit.tests.api.auth;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertNotNull;

public class AuthAsAdminTest {
    private RestClient restClient;
    private AccountTokenPojo adminToken;

    @BeforeClass
    public void beforeClass() {
        restClient = new RestClient();
    }

    @Test(description = "Authenticate as admin.")
    //@Severity annotation is used in order to prioritize test methods by severity
    @Severity(SeverityLevel.BLOCKER)
    //Позволяет группировать тесты по проверяемому функционалу.
    //Данная аннотация принимает параметр «value» — наименование функционала.
    @Feature("Authentication feature.")
    public void authAsAdminTest() {
        adminToken = restClient.authenticate("admin", "setup");
    }

    @Test(description = "Me.", dependsOnMethods = "authAsAdminTest")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Authentication feature.")
    public void getMeTest(){
        ContactPojo contactPojo = restClient.getMe(adminToken, ContactPojo.class);
        assertNotNull(contactPojo);
    }
}
