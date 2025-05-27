package com.vk.testing.final_project.util;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.WebDriver;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class EyesUtilDemo {
    private static Eyes eyes;

    public static Eyes initEyes() {
        if (eyes == null) {
            eyes = new Eyes();
            eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
            eyes.setBatch(new BatchInfo("VK Final Project"));
        }
        return eyes;
    }

    public static void openEyes(String testName) {
        WebDriver driver = getWebDriver();
        initEyes().open(driver, "VK Demo", testName);
    }

    public static void checkWindow(String tag) {
        initEyes().check(tag, Target.window().fully());
    }

    public static void closeEyes() {
        initEyes().closeAsync();
    }

    public static void abortIfNotClosed() {
        if (eyes != null) {
            eyes.abortIfNotClosed();
        }
    }
}
