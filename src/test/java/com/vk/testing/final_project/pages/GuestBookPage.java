package com.vk.testing.final_project.pages;

import com.codeborne.selenide.SelenideElement;
import com.vk.testing.final_project.base.BasePage;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class GuestBookPage implements BasePage {
    @Override
    public SelenideElement getRoot() {
        return $("body");
    }

    @Override
    public List<By> getIgnoredElements() {
        return List.of();
    }
}
