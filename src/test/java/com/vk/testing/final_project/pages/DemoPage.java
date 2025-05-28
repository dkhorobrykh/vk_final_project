package com.vk.testing.final_project.pages;

import com.codeborne.selenide.SelenideElement;
import com.vk.testing.final_project.base.BasePage;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DemoPage implements BasePage {
    private static final By toggleColorCheckbox = By.xpath(".//label[contains(., 'Изменить цвет фона')]/input");
    private static final By toggleTextCheckbox = By.xpath(".//label[contains(., 'Изменить текст')]/input");
    private static final By toggleShiftCheckbox = By.xpath(".//label[contains(., 'Сместить блок')]/input");
    private static final By toggleResizeCheckbox = By.xpath(".//label[contains(., 'Изменить размер блока')]/input");
    private static final By toggleImageCheckbox = By.xpath(".//label[contains(., 'Показать изображение')]/input");
    private static final By toggleAdCheckbox = By.xpath(".//label[contains(., 'Показать баннер')]/input");
    private static final By toggleClockCheckbox = By.xpath(".//label[contains(., 'Показать часы')]/input");
    private static final By toggleAnimateCheckbox = By.xpath(".//label[contains(., 'Показать движущийся элемент')]/input");


    public static void toggleColor() {
        $(toggleColorCheckbox).click();
    }

    public static void toggleText() {
        $(toggleTextCheckbox).shouldBe(visible.because("Элемент text не отображен на странице")).click();
    }

    public static void toggleShift() {
        $(toggleShiftCheckbox).shouldBe(visible.because("Элемент shift не отображен на странице")).click();
    }

    public static void toggleResize() {
        $(toggleResizeCheckbox).shouldBe(visible.because("Элемент resize не отображен на странице")).click();
    }

    public static void toggleImage() {
        $(toggleImageCheckbox).shouldBe(visible.because("Элемент image не отображен на странице")).click();
    }

    public static void toggleAd() {
        $(toggleAdCheckbox).shouldBe(visible.because("Элемент ad не отображен на странице")).click();
    }

    public static void toggleClock() {
        $(toggleClockCheckbox).shouldBe(visible.because("Элемент clock не отображен на странице")).click();
    }

    public static void toggleAnimate() {
        $(toggleAnimateCheckbox).shouldBe(visible.because("Элемент animate не отображен на странице")).click();
    }

    @Override
    public SelenideElement getRoot() {
        return $("body");
    }

    @Override
    public List<By> getIgnoredElements() {
        return List.of();
    }
}
