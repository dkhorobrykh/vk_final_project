package com.vk.testing.final_project.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vk.testing.final_project.base.BasePage;
import lombok.Getter;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class MainPage implements BasePage {
    private static final By themeSwitcher = By.cssSelector(".DarkSwitcher-switcher");
    private static final By themeImage = By.cssSelector(".DarkSwitcher-switcher img");
    private static final By catalogButton = By.cssSelector("a[href='/prob-catalog']");
    private static final By teacherButton = By.cssSelector("a[href='/teacher']");
    private static final By guestBookButton = By.cssSelector("a[href='/guestbook']");
    private final List<By> ignoredElements = List.of(
            By.xpath(".//main/div[2]"),
            By.xpath(".//main/div[last()]"),
//            ".//*[contains(@id, 'yandex_')]",
//            ".//*[contains(@id, 'yandex_rtb')]",
//            ".//*[contains(@id, 'ya_partner')]",
//            ".//*[contains(@id, 'ya_rtb')]",
//            ".//center",
            By.xpath(".//*[contains(@class, 'Header-Logo')]"),
            By.xpath(".//div[@class='Sidebar']"),
            By.xpath(".//aside[contains(@class, 'Sidebar')]/div[last()]"),
            By.xpath(".//div[@class='menu']/div[1]"),
            By.xpath(".//div[@class='sgia-main-content']/div[1]"),
            By.xpath(".//div[@class='left_column']/div[last()]"),
            By.xpath(".//div[@class='sgia-main-content']/ul[last()]/li[last()]/div[last() - 1]")
//            ".//*[@data-container='outer']"
    );
    private final List<By> promoWidgets = List.of(
            By.xpath("/html/body/*[contains(@class, 'csr-uniq')]/div/div/div"),
            By.xpath("//div[text()='OK']")
    );

    @Override
    public SelenideElement getRoot() {
        return $("body");
    }

    public void switchToDarkTheme() {
        for (int i = 0; i < 3; i++) {
            if (isDarkThemeActive()) break;
            $(themeSwitcher).click();
        }
        $(themeImage).shouldHave(Condition.attributeMatching("src", ".*new_dark\\.svg"));
    }

    public void switchToLightTheme() {
        for (int i = 0; i < 3; i++) {
            if (isLightThemeActive()) break;
            $(themeSwitcher).click();
        }
        $(themeImage).shouldHave(Condition.attributeMatching("src", ".*new_light\\.svg"));
    }

    public boolean isDarkThemeActive() {
        return $(themeImage).getAttribute("src").contains("new_dark.svg");
    }

    public boolean isLightThemeActive() {
        return $(themeImage).getAttribute("src").contains("new_light.svg");
    }

    public void goToCatalogPage() {
        $(catalogButton).shouldBe(Condition.visible).click();
    }

    public void goToTeacherPage() {
        $(teacherButton).shouldBe(Condition.visible).click();
    }

    public void goToGuestBookPage() {
        $(guestBookButton).shouldBe(Condition.visible).click();
    }
}
