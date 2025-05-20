package com.vk.testing.final_project.util;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
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
            SelenideElement target,
            String screenshotName,
            List<String> ignoredCssSelectors
    ) throws IOException {

        var ashot = new AShot().coordsProvider(new WebDriverCoordsProvider());
        for (var selector : ignoredCssSelectors) {
            ashot.addIgnoredElement(By.cssSelector(selector));
        }

        var actual = ashot.shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(getWebDriver(), target);
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
