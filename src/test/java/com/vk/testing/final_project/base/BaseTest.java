package com.vk.testing.final_project.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BaseTest {
    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = false;
        Configuration.pageLoadTimeout = 60000;
    }

    protected void openMainPage() {
        open("https://inf-ege.sdamgia.ru/");

        try {
            SelenideElement cookieButton = $x("//div[text()='OK']");
            if (cookieButton.isDisplayed()) {
                cookieButton.click();
            }
        } catch (Exception ignored) {
        }
    }
}
