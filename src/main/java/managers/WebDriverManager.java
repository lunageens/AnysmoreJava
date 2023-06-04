package managers;

import enums.DriverType;
import enums.EnvironmentType;
import enums.OperatingSystemType;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.time.Duration;



/**
 * Allows us to let the user specify different type of browser and environment and run the test correctly.
 * On top of that, it makes more sense to handle the logic of creating and quitting the WebDriver in a separate
 * class instead of in the tests or Hooks. The test should not be worried about how to start the driver. but just
 * need a driver to execute the script.
 */
public class WebDriverManager {

    /**
     * WebDriver used in this scenario.
     */
    private WebDriver driver;

    /**
     * Type of driver to use, specified in Configuration.properties.
     */
    private static DriverType driverType;

    /**
     * Type of environment to use, specified in Configuration.properties.
     */
    private static EnvironmentType environmentType;

    /**
     * Constructor of the Webdriver manager, where driverType and EnvironmentType are read by calling FileReaderManager.
     */
    public WebDriverManager() { // read types from config file, with singleton pattern of FileReaderManager
        driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
        environmentType = FileReaderManager.getInstance().getConfigReader().getEnvironment();
    }

    /**
     * Together with closeDriver() only public method.
     * GetDriver method will decide if the driver is already created or needs to be created.
     * GetDriver method further call the method createDriver() if needed.
     *
     * @return driver from type and environment specified in config
     */
    public WebDriver getDriver() {
        if (driver == null) driver = createDriver();
        assertNotNull(this.driver, "driver is empty");
        return driver;
    }

    /**
     * Will decide that the remote driver is needed or local driver for the execution.
     * Calls appropriate methods to create local or remote driver
     *
     * @return driver from type and environment specified in config
     */
    private WebDriver createDriver() {
        switch (environmentType) {
            case LOCAL -> driver = createLocalDriver();
            case REMOTE -> driver = createRemoteDriver();
        }
        assertNotNull(this.driver, "driver is empty");
        return driver;
    }

    /**
     * Throws exception when remote driver is asked in Configuration.properties file,
     * since this is not yet implemented.
     *
     * @return WebDriver Remote Web Driver that was asked.
     */
    private WebDriver createRemoteDriver() {
        throw new RuntimeException("RemoteWebDriver is not yet implemented");
    }

    /**
     * Creates local driver of type that was specified in the Configuration.properties file.
     * Sets driver headless when asked and possible with that type, gives notification when not possible and creates driver with head.
     * Maximizes window of driver when asked.
     * Sets implicitly wait (in seconds) of driver that is also specified in Configuration.properties file.
     *
     * @return WebDriver Local webdriver of right type, with headless option if asked and possible, with maximized windows when asked, and with the specified implicitly wait time.
     */
    private WebDriver createLocalDriver() {
        switch (driverType) {
            case FIREFOX:
                FirefoxOptions optionsFirefox = new FirefoxOptions();
                if (FileReaderManager.getInstance().getConfigReader().getHeadLess())
                    optionsFirefox.setHeadless(true); // Run firefox in headless mode (without GUI)
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");  // disable the logs by sending them to /dev/null via a system property
                driver = new FirefoxDriver(optionsFirefox);
                break;
            case CHROME:
                ChromeOptions optionsChrome = new ChromeOptions();
                if (FileReaderManager.getInstance().getConfigReader().getHeadLess()) optionsChrome.setHeadless(true);
                driver = new ChromeDriver(optionsChrome);
                break;
            case INTERNETEXPLORER:
                if (FileReaderManager.getInstance().getConfigReader().getHeadLess())
                    System.out.println("Headless mode not possible in Internet Explorer. Running with GUI.");
                driver = new InternetExplorerDriver();
                break;
            case SAFARI:
                if (FileReaderManager.getInstance().getConfigReader().getHeadLess())
                    System.out.println("Headless mode not possible in Safari. Running with GUI.");
                driver = new SafariDriver();
            case EDGE:
                EdgeOptions optionsEdge = new EdgeOptions();
                if (FileReaderManager.getInstance().getConfigReader().getHeadLess()) optionsEdge.setHeadless(true);
                driver = new EdgeDriver(optionsEdge);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + driverType);
        }

        // extra settings -  if maximizes needed to happen and implicitly wait
        if (FileReaderManager.getInstance().getConfigReader().getBrowserWindowSize())
            driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FileReaderManager.getInstance().getConfigReader().getImplicitlyWait()));

        assertNotNull(driver, "The driver at the end of createLocalDriver() is null");
        return driver;
    }

    /**
     * Closes driver in use safely.
     */
    public void closeDriver() {
        driver.quit();
        this.driver = null;
    }

    /**
     * It's essential to ensure that you are closing the browser instances correctly to prevent unnecessary memory usage.
     * I encountered multiple Firefox applications or instances of the Windows Driver Foundation after running the tests,
     * which indicates that the browser processes are not being terminated properly.
     * <p>
     * To address this, I modified WebDriverManager and Hooks class to explicitly kill any remaining browser processes
     * after each test.
     * </p>
     */
    public static void killBrowserProcesses() {
        DriverType driverType = FileReaderManager.getInstance().getConfigReader().getBrowser(); // Get the current driver type
        OperatingSystemType operatingSystemType = FileReaderManager.getInstance().getConfigReader().getOperatingSystem(); // Get the operating system
        try {
            switch (driverType) {
                case FIREFOX:
                    switch (operatingSystemType){
                        case WINDOWS ->  Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
                        case LINUX, UBUNTU, MACOS -> Runtime.getRuntime().exec("pkill firefox");
                    }
                    break;
                case CHROME:
                    switch (operatingSystemType){
                        case WINDOWS -> Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
                        case LINUX, UBUNTU, MACOS -> Runtime.getRuntime().exec("pkill chrome");
                    }
                    break;
                case EDGE:
                    switch (operatingSystemType) {
                        case WINDOWS-> Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
                        case LINUX, UBUNTU, MACOS -> System.out.println("Handling Edge termination on this operating system is not yet implemented. Program could take up a lot of memory.");
                    }
                    break;
                case SAFARI:
                    switch (operatingSystemType){
                        case WINDOWS -> Runtime.getRuntime().exec("taskkill /F /IM Safari.exe");
                        case MACOS -> Runtime.getRuntime().exec("pkill Safari");
                        case LINUX, UBUNTU -> System.out.println("Handling Safari termination on this operating system is not yet implemented. Program could take up a lot of memory.");
                    }
                    break;
                case INTERNETEXPLORER:
                    switch (operatingSystemType){
                        case WINDOWS -> Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
                        case MACOS -> System.out.println("Internet Explorer is not available on macOS");
                        case LINUX, UBUNTU -> System.out.println("Handling Internet Explorer termination on this operating system is not yet implemented. Program could take up a lot of memory.");
                    }
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
