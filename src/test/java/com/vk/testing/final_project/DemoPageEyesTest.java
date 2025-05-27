package com.vk.testing.final_project;

import com.codeborne.selenide.Configuration;
import com.vk.testing.final_project.pages.DemoPage;
import com.vk.testing.final_project.util.EyesUtilDemo;
import org.junit.jupiter.api.*;

import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.open;

public class DemoPageEyesTest {

    private static final String URL = "https://kseniyanesterenko.github.io/vk/";

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = null;

        Configuration.browserCapabilities = new ChromeOptions()
                .addArguments("--window-size=1920,1080")
                .addArguments("--force-device-scale-factor=1")
                .addArguments("--force-color-profile=srgb")
                .addArguments("--disable-infobars")
                .addArguments("--disable-notifications")
                .addArguments("--disable-extensions");
    }

    @BeforeEach
    void openDemoPage() {
        open(URL);
    }

    @AfterEach
    void cleanupEyes() {
        EyesUtilDemo.abortIfNotClosed();
    }

    private void checkWithEyes(String appName, String tag) {
        EyesUtilDemo.openEyes(appName);
        EyesUtilDemo.checkWindow(tag);
        EyesUtilDemo.closeEyes();
    }

    @Test
    @DisplayName("Отображение времени")
    void clockChangeError() {
        DemoPage.toggleClock();
        checkWithEyes("Тест часов", "Часы");
    }

    @Test
    @DisplayName("Отображение анимации")
    void animateChangeError() {
        DemoPage.toggleAnimate();
        checkWithEyes("Тест анимации", "Анимация");
    }
}
