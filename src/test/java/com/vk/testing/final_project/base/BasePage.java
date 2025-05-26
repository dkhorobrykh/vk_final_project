package com.vk.testing.final_project.base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.List;

public interface BasePage {
    SelenideElement getRoot();

    List<By> getIgnoredElements();
}
