package com.vk.testing.final_project;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.vk.testing.final_project.base.BaseTest;
import com.vk.testing.final_project.pages.MainPage;
import com.vk.testing.final_project.util.ScreenshotUtil;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MainPageTest extends BaseTest {
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        openMainPage();
    }

    @Test
    public void mainFirstTest() throws Exception {
        openMainPage();

        ScreenshotUtil.compareWithBaseline("main-page", mainPage.ignoredElements);
    }

    @Test
    public void catalogFirstTest() throws Exception {
        openMainPage();
        mainPage.goToCatalogPage();

        ScreenshotUtil.compareWithBaseline("catalog-page", mainPage.ignoredElements);
    }

    @Test
    public void teacherFirstTest() throws Exception {
        openMainPage();
        mainPage.goToTeacherPage();

        ScreenshotUtil.compareWithBaseline("teacher-page", mainPage.ignoredElements);
    }

    @Test
    public void guestBookFirstTest() throws Exception {
        openMainPage();
        mainPage.goToGuestBookPage();

        ScreenshotUtil.compareWithBaseline("guestbook-page", mainPage.ignoredElements);
    }

    @Test
    public void testLightTheme() throws Exception {
        mainPage.switchToLightTheme();
        ScreenshotUtil.compareWithBaseline(
                "main-light",
                mainPage.ignoredElements
        );
    }

    @Test
    public void testDarkTheme() throws Exception {
        mainPage.switchToDarkTheme();
        ScreenshotUtil.compareWithBaseline(
                "main-dark",
                mainPage.ignoredElements
        );
    }
}
