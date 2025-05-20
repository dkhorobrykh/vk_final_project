package com.vk.testing.final_project.util;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;
import static org.junit.jupiter.api.Assertions.fail;

public class ScreenshotUtil {
    public static void compareWithBaseline(
            String screenshotName,
            List<String> ignoredCssSelectors
    ) throws IOException {

        for (String selector : ignoredCssSelectors) {
            List<WebElement> elements = getWebDriver().findElements(By.cssSelector(selector));
            for (WebElement el : elements) {
                ((JavascriptExecutor) getWebDriver()).executeScript(
                        "arguments[0].style.visibility='hidden'; arguments[0].style.display='none';", el
                );
            }
        }

        var ashot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .shootingStrategy(ShootingStrategies.viewportPasting(100));

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
}
