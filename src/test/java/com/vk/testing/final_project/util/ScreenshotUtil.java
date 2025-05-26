package com.vk.testing.final_project.util;

import com.codeborne.selenide.SelenideElement;
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
    public static void compareWithBaseline(
            String screenshotName,
            List<By> ignoredCssSelectors,
            List<By> promoWidgets
    ) throws IOException {

        loadPromoWidgets(promoWidgets);

        for (By selector : ignoredCssSelectors) {
            List<WebElement> elements;
            try {
                elements = getWebDriver().findElements(selector);
            } catch (InvalidSelectorException ex) {
                log.warn("Элемент с селектором '{}' не найден, пропускаем его", selector);
                continue;
            }
            for (WebElement el : elements) {
                ((JavascriptExecutor) getWebDriver()).executeScript(
                        "const el = arguments[0];" +
                                "const rect = el.getBoundingClientRect();" +
                                "const overlay = document.createElement('img');" +
                                "overlay.src = 'data:image/png;base64," + getOverlayBase64() + "';" +
                                "overlay.style.position = 'absolute';" +
                                "overlay.style.left = rect.left + window.scrollX + 'px';" +
                                "overlay.style.top = rect.top + window.scrollY + 'px';" +
                                "overlay.style.width = rect.width + 'px';" +
                                "overlay.style.height = rect.height + 'px';" +
                                "overlay.style.zIndex = 999999;" +
                                "overlay.style.pointerEvents = 'none';" +
                                "document.body.appendChild(overlay);",
                        el
                );
            }
        }

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

        if (diff.hasDiff()) {
            var diffFile = new File("target/" + screenshotName + "-diff.png");
            write(diff.getMarkedImage(), "PNG", diffFile);
            fail("Screenshot mismatch! See diff: " + diffFile.getAbsolutePath());
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

    private static void loadPromoWidgets(List<By> promoWidgets) {
        // TODO: придумать, как по другому прогружать всю страницу целиком
        for (var i = 0; i < 20; i++) {
            getWebDriver().findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_DOWN);
        }

        for (var i = 0; i < 20; i++) {
            getWebDriver().findElement(By.cssSelector("body")).sendKeys(Keys.PAGE_UP);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}

        try {
            for (var widget : promoWidgets) {
                var element = $(widget);
                if (element.isDisplayed()) {
                    element.click();
                }
            }
        } catch (Exception ignored) {
        }
    }

}
