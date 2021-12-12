package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class CreateCustomerScreen extends Screen {
    public CreateCustomerScreen(Browser browser) {
        super(browser);
        if (!browser.waitPage()) {
            throw new IllegalArgumentException("Redirect to create customer failed");
        }
        if (!browser.containsTitle("add-customer")) {
            throw new IllegalArgumentException("Can't create CreateCustomerScreen");
        }
    }

    public CreateCustomerScreen fillEmail(String email) {
        browser.typeText(By.name("login"), email);
        return this;
    }

    public CreateCustomerScreen fillPassword(String password) {
        browser.typeText(By.name("password"), password);
        return this;
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        browser.typeText(By.name("firstName"), firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        browser.typeText(By.name("lastName"), lastName);
        return this;
    }

    // Лабораторная 4: Подумайте как обработать ситуацию,
    // когда при нажатии на кнопку Submit ('Create') не произойдет переход на AdminScreen,
    // а будет показана та или иная ошибка на текущем скрине.
    public AdminScreen clickSubmit() throws IllegalArgumentException {
        browser.click(By.xpath("//button[@type = 'submit']"));
        return new AdminScreen(browser);
    }

    public AdminScreen clickCancel() {
        browser.click(By.xpath("//button"));
        return new AdminScreen(browser);
    }
}
