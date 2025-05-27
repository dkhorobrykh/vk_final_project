package com.vk.testing.final_project;

import com.codeborne.selenide.Configuration;
import com.vk.testing.final_project.pages.DemoPage;
import com.vk.testing.final_project.util.ScreenshotUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.fail;

public class DemoPageTest {
    private static final String BASELINE_NAME = "demo_default";
    private static final String URL = "https://kseniyanesterenko.github.io/vk/";

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = null;

        Configuration.browserCapabilities = new ChromeOptions()
                .addArguments("--window-size=1920,1080")
                .addArguments("--force-device-scale-factor=1")
                .addArguments("--force-color-profile=srgb")
                .addArguments("--disable-infobars")
                .addArguments("--disable-notifications")
                .addArguments("--disable-extensions");
    }

    @BeforeEach
    void openDemoPage() {
        open(URL);
    }

    private void assertFailsOnChange(String whatChanged, String baselineName, Runnable action) throws IOException {
        action.run();
        try {
            ScreenshotUtil.compareWithBaseline(baselineName, Collections.emptyList(), Collections.emptyList());
        } catch (AssertionError ae) {
            System.out.println("Ожидаемое отличие при: " + whatChanged + " — " + ae.getMessage());
            return;
        }
        fail("Тест не провалился при изменении " + whatChanged + "!");
    }

    @Test
    @DisplayName("Создать эталонный скриншот")
    void createBaseline() throws IOException {
        ScreenshotUtil.compareWithBaseline(BASELINE_NAME, Collections.emptyList(), Collections.emptyList());
    }

    @Test
    @DisplayName("Ошибка при изменении цвета")
    void demonstrateColorChangeError() throws IOException {
        assertFailsOnChange("color change", BASELINE_NAME, DemoPage::toggleColor);
    }

    @Test
    @DisplayName("Ошибка при изменении текста")
    void demonstrateTextChangeError() throws IOException {
        assertFailsOnChange("text change", BASELINE_NAME, DemoPage::toggleText);
    }

    @Test
    @DisplayName("Ошибка при смещении блока")
    void demonstrateShiftError() throws IOException {
        assertFailsOnChange("block shift", BASELINE_NAME, DemoPage::toggleShift);
    }

    @Test
    @DisplayName("Ошибка при изменении размера")
    void demonstrateResizeError() throws IOException {
        assertFailsOnChange("resize", BASELINE_NAME, DemoPage::toggleResize);
    }

    @Test
    @DisplayName("Ошибка при изменении изображения")
    void demonstrateImageChangeError() throws IOException {
        assertFailsOnChange("image change", BASELINE_NAME, DemoPage::toggleImage);
    }

    @Test
    @DisplayName("Ошибка при отображении рекламы")
    void demonstrateAdDisplayError() throws IOException {
        assertFailsOnChange("ad display", BASELINE_NAME, DemoPage::toggleAd);
    }

    @ParameterizedTest(name = "{0} — проверка динамических элементов")
    @MethodSource("dynamicElements")
    void demonstrateDynamicError(String name, String baselineName, Runnable toggleAction, int delayMs) throws IOException, InterruptedException {
        toggleAction.run();
        Thread.sleep(delayMs);
        assertFailsOnChange(name, baselineName, () -> {});
    }

    private static Stream<Arguments> dynamicElements() {
        return Stream.of(
                Arguments.of("clock display", "demo_clock", (Runnable) DemoPage::toggleClock, 1000),
                Arguments.of("animation", "demo_animated", (Runnable) DemoPage::toggleAnimate, 1000)
        );
    }

    @Test
    @DisplayName("Создание эталонов для динамических элементов")
    void createDynamicBaselines() throws IOException {
        DemoPage.toggleClock();
        ScreenshotUtil.compareWithBaseline("demo_clock", Collections.emptyList(), Collections.emptyList());

        open(URL);
        DemoPage.toggleAnimate();
        ScreenshotUtil.compareWithBaseline("demo_animated", Collections.emptyList(), Collections.emptyList());
    }
}
