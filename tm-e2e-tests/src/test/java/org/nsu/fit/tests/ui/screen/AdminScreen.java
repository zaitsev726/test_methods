package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class AdminScreen extends Screen {
    public AdminScreen(Browser browser) {
        super(browser);
        try {
            browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"), 15);
        } catch (TimeoutException e) {
            throw new IllegalArgumentException("Admin Screen hasn't been loaded");
        }
    }

    public CreateCustomerScreen createCustomer() {
        browser.click(By.xpath("//button[@title = 'Add Customer']"));
        return new CreateCustomerScreen(browser);
    }

    public void deleteLastCustomer() {
        browser.click(By.xpath("//*[@id='root']/div/div/div/div/div[1]/table/tfoot/tr/td/div/div[3]/span[5]/button"));

        for (int i = 5; i > 0; i--) {
            try {
                String rowXPath = "//*[@id='root']/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/tr[" + i + "]/td[1]/div/";
                browser.click(By.xpath(rowXPath + "button"));
                browser.click(By.xpath(rowXPath + "button[1]"));
                return;
            } catch (Exception ignored) {
            }
        }
    }

    public void deleteFirstCustomer() {
        try {
            String rowXPath = "//*[@id='root']/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/tr[1]/td[1]/div/";
            browser.click(By.xpath(rowXPath + "button"));
            browser.click(By.xpath(rowXPath + "button[1]"));
        } catch (Exception ignored) {
        }
    }

    public void searchCustomer(String searchParam) {
        browser.typeText(By.xpath("//*[@id='root']/div/div/div/div/div[1]/div[1]/div[3]/div/input"), searchParam);
    }

    public CreatePlanScreen createPlan() {
        browser.click(By.xpath("//*[@id='root']/div/div/div/div/div[2]/div[1]/div[4]/div/div/span/button"));
        return new CreatePlanScreen(browser);
    }

    public void searchPlan(String searchParam) {
        browser.typeText(By.xpath("//*[@id='root']/div/div/div/div/div[2]/div[1]/div[3]/div/input"), searchParam);
    }


    public void deleteLastPlan() {
        try {
            browser.click(By.xpath("//*[@id='root']/div/div/div/div/div[2]/table/tfoot/tr/td/div/div[3]/span[5]/button"));
        } catch (Exception ignored) {

        }
        for (int i = 5; i > 0; i--) {
            try {
                String rowXPath = "//*[@id='root']/div/div/div/div/div[2]/div[2]/div/div/div/table/tbody/tr[" + i + "]/td[1]/div/";
                browser.click(By.xpath(rowXPath + "button"));
                browser.click(By.xpath(rowXPath + "button[1]"));
                return;
            } catch (Exception ignored) {
            }
        }
    }

    public void deleteFirstPlan() {
        try {
            String rowXPath = "//*[@id='root']/div/div/div/div/div[2]/div[2]/div/div/div/table/tbody/tr[1]/td[1]/div/";
            browser.click(By.xpath(rowXPath + "button"));
            browser.click(By.xpath(rowXPath + "button[1]"));
        } catch (Exception ignored) {
        }
    }
}
