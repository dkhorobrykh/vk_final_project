package com.vk.testing.final_project.util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class ScreenshotUtil {
    private static final int MAX_ALLOWED_DIFF_PIXELS = 500;
    public static void compareWithBaseline(
            String screenshotName,
            List<By> ignoredCssSelectors,
            List<By> popUpWidgets
    ) throws IOException {

        loadPromoWidgets();
        closePopUpWidgets(popUpWidgets);

        boolean rerunSelectors;
        do {
            rerunSelectors = false;

            for (By selector : ignoredCssSelectors) {
                List<WebElement> elements;
                try {
                    elements = getWebDriver().findElements(selector);
                } catch (InvalidSelectorException ex) {
                    log.warn("Элемент с селектором '{}' не найден, пропускаем его", selector);
                    continue;
                }

                for (WebElement el : elements) {
                    try {
                        ((JavascriptExecutor) getWebDriver()).executeScript(
                                "arguments[0].remove();", el
                        );
                    } catch (StaleElementReferenceException e) {
                        log.warn("Элемент устарел, пропускаем");
                        continue;
                    }

                    if ($x(".//h3[text()='Отключите блокировщик рекламы']").isDisplayed()) {
                        log.warn("Блокировка рекламы обнаружена, ждем 25 секунд для прогрузки страницы");

                        try {
                            Thread.sleep(25000);
                        } catch (InterruptedException ignored) {}

                        closePopUpWidgets(popUpWidgets);

                        rerunSelectors = true;
                        break;
                    }
                }

                if (rerunSelectors) break;
            }
        } while (rerunSelectors);

        var ashot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .shootingStrategy(ShootingStrategies.viewportPasting(500));

        var actual = ashot.takeScreenshot(getWebDriver());
        var actualImage = actual.getImage();

        var fallbackBaselineFile = new File("src/test/resources/baselines/" + screenshotName + ".png");
        fallbackBaselineFile.getParentFile().mkdirs();

        BufferedImage expectedImage;

        try (var stream = ScreenshotUtil.class.getResourceAsStream("/baselines/" + screenshotName + ".png")) {
            if (stream == null) {
                write(actualImage, "PNG", fallbackBaselineFile);
                System.out.println("Baseline not found, saved current screenshot to: " + fallbackBaselineFile.getPath());
                return;
            }
            expectedImage = read(stream);
        }

        var diff = new ImageDiffer().makeDiff(expectedImage, actualImage);

        if (diff.hasDiff() && diff.getDiffSize() > MAX_ALLOWED_DIFF_PIXELS) {
            var diffFile = new File("target/" + screenshotName + "-diff.png");
            write(diff.getMarkedImage(), "PNG", diffFile);
            fail("Screenshot mismatch! Pixels diff: " + diff.getDiffSize() + ", allowed: " + MAX_ALLOWED_DIFF_PIXELS);
        }
    }

    private static String getOverlayBase64() {
        try (var stream = ScreenshotUtil.class.getResourceAsStream("/ads_overlay.png")) {
            if (stream == null) throw new RuntimeException("ads_overlay.png not found in resources");
            byte[] bytes = stream.readAllBytes();
            return java.util.Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read overlay image", e);
        }
    }

    private static void loadPromoWidgets() {
        long lastHeight = (long) ((JavascriptExecutor) getWebDriver()).executeScript("return document.body.scrollHeight");

        while (true) {
            ((JavascriptExecutor) getWebDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(500);
            } catch (Exception ignored) {}

            long newHeight = (long) ((JavascriptExecutor) getWebDriver()).executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) break;
            lastHeight = newHeight;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    private static void closePopUpWidgets(List<By> popUpWidgets) {
        try {
            for (var widget : popUpWidgets) {
                var element = $(widget);
                if (element.isDisplayed()) {
                    element.click();
                }
            }
        } catch (Exception ignored) {
        }
    }
}
