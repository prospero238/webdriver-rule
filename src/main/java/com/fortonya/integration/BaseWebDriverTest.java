import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseWebDriverTest {

    private static final long WAIT_TIMEOUT_IN_SECONDS = 10;
    private static Logger log = LoggerFactory.getLogger(BaseWebDriverTest.class);

    @Rule
    public TestRule screenshotRule = new ScreenshotTestWatcher();

    @Value("${webdriver.base.url}")
    protected String baseUrl;

    @Value("${screenshot.output.directory}")
    protected String screenshotOutputDirectory;

//    @Value("${webdriver.chrome.driver}")
//    private String chromedriverLocation;

    protected WebDriver webDriver;
    protected WebDriverWait wait;

    private String currentMethodName;

    @Before
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "/home/tohrel/chromedriver");
        baseUrl = "https://enterprise1.informatics.illumina.com:9000";
        screenshotOutputDirectory = "target" + File.separator + "surefire-reports" + File.separator;

        webDriver = new ChromeDriver();
        webDriver.get(baseUrl);
        wait = new WebDriverWait(webDriver, WAIT_TIMEOUT_IN_SECONDS);
    }

    @After
    public void saveScreenshotAndCloseBrowser() throws IOException {
        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        new File(screenshotOutputDirectory).mkdirs(); // Ensure directory exists
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        try {
            FileUtils.copyFile(scrFile,
                    new File(screenshotOutputDirectory + "SCREENSHOT-" + currentMethodName + timeStamp + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        webDriver.quit();
    }

    class ScreenshotTestWatcher extends TestWatcher {
        @Override
        protected void starting(Description description) {
            currentMethodName = description.getMethodName();
        }
    }
}