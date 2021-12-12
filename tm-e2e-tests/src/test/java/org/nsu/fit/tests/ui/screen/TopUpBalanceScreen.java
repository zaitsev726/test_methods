package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class TopUpBalanceScreen extends Screen {
    public TopUpBalanceScreen(Browser browser) {
        super(browser);
        try {
            browser.waitForElement(By.xpath("//button[@type = 'submit']"), 15);
        } catch (TimeoutException e) {
            throw new IllegalArgumentException("Top Up Balance Screen hasn't been loaded");
        }
    }

    public TopUpBalanceScreen fillMoney(Integer money) {
        browser.typeText(By.name("money"), String.valueOf(money));
        return this;
    }

    public CustomerScreen clickSubmit() throws IllegalArgumentException {
        browser.click(By.xpath("//button[@type = 'submit']"));
        return new CustomerScreen(browser);
    }

    public CustomerScreen clickCancel() {
        browser.click(By.xpath("//button"));
        return new CustomerScreen(browser);
    }
}
