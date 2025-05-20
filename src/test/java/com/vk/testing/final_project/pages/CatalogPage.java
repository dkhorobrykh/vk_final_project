package com.vk.testing.final_project.pages;

import com.codeborne.selenide.SelenideElement;
import com.vk.testing.final_project.base.BasePage;

import static com.codeborne.selenide.Selenide.$;

public class CatalogPage implements BasePage {
    @Override
    public SelenideElement getRoot() {
        return $("body");
    }
}
