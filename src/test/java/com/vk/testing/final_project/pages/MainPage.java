package com.vk.testing.final_project.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vk.testing.final_project.base.BasePage;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class MainPage implements BasePage {
    private final SelenideElement themeSwitcher = $(".DarkSwitcher-switcher");
    private final SelenideElement themeImage = $(".DarkSwitcher-switcher img");
    private final SelenideElement catalogButton = $("a[href='/prob-catalog']");
    private final SelenideElement teacherButton = $("a[href='/teacher']");
    private final SelenideElement guestBookButton = $("a[href='/guestbook']");

    @Override
    public SelenideElement getRoot() {
        return $("body");
    }

    public void switchToDarkTheme() {
        for (int i = 0; i < 3; i++) {
            if (isDarkThemeActive()) break;
            themeSwitcher.click();
        }
        themeImage.shouldHave(Condition.attributeMatching("src", ".*new_dark\\.svg"));
    }

    public void switchToLightTheme() {
        for (int i = 0; i < 3; i++) {
            if (isLightThemeActive()) break;
            themeSwitcher.click();
        }
        themeImage.shouldHave(Condition.attributeMatching("src", ".*new_light\\.svg"));
    }

    public boolean isDarkThemeActive() {
        return themeImage.getAttribute("src").contains("new_dark.svg");
    }

    public boolean isLightThemeActive() {
        return themeImage.getAttribute("src").contains("new_light.svg");
    }

    public void goToCatalogPage() {
        catalogButton.shouldBe(Condition.visible).click();
    }

    public void goToTeacherPage() {
        teacherButton.shouldBe(Condition.visible).click();
    }

    public void goToGuestBookPage() {
        guestBookButton.shouldBe(Condition.visible).click();
    }

    public List<String> ignoredElements = List.of(
            ".TopAdBanner",
            ".footer",
            "#clock",
            ".Header-Logo",
            "div.Sidebar"
    );
}
