package com.vk.testing.final_project.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$x;

public class DemoPage {
    private static final SelenideElement toggleColorCheckbox = $x("//label[contains(., 'Изменить цвет фона')]/input");
    private static final SelenideElement toggleTextCheckbox = $x("//label[contains(., 'Изменить текст')]/input");
    private static final SelenideElement toggleShiftCheckbox = $x("//label[contains(., 'Сместить блок')]/input");
    private static final SelenideElement toggleResizeCheckbox = $x("//label[contains(., 'Изменить размер блока')]/input");
    private static final SelenideElement toggleImageCheckbox = $x("//label[contains(., 'Показать изображение')]/input");
    private static final SelenideElement toggleAdCheckbox = $x("//label[contains(., 'Показать баннер')]/input");
    private static final SelenideElement toggleClockCheckbox = $x("//label[contains(., 'Показать часы')]/input");
    private static final SelenideElement toggleAnimateCheckbox = $x("//label[contains(., 'Показать движущийся элемент')]/input");


    public static void toggleColor() {
        toggleColorCheckbox.click();
    }

    public static void toggleText() {
        toggleTextCheckbox.click();
    }

    public static void toggleShift() {
        toggleShiftCheckbox.click();
    }

    public static void toggleResize() {
        toggleResizeCheckbox.click();
    }

    public static void toggleImage() {
        toggleImageCheckbox.click();
    }

    public static void toggleAd() {
        toggleAdCheckbox.click();
    }

    public static void toggleClock() {
        toggleClockCheckbox.click();
    }

    public static void toggleAnimate() {
        toggleAnimateCheckbox.click();
    }
}
