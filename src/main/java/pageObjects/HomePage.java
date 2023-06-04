package pageObjects;

import managers.FileReaderManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Applying the page pattern, all the methods related to the homepage
 * are specified are implemented in this class.
 * This is a Selenium Page Object.
 */
// page_url = https://ansymore.uantwerpen.be/
public class HomePage {

    /**
     * WebDriver used to create page and execute methods.
     */
    WebDriver driver;

    /**
     * Constructor of HomePage
     *
     * @param driver WebDriver used to create page and execute methods.
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    /**
     * WebElement of the Courses menu option, found by css operator.
     */
    @FindBy(css = "a[title='Courses']")
    public WebElement linkCourses;


    /**
     * Driver goes to HomePage.
     */
    public void navigateToHomePage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationHomeUrl()); // instead of making instance of configfile reader everytime -> use file reader manager with singleton pattern
    }

    /**
     * Clicks the menu item that has that text.
     * Only implemented for Courses menu item currently.
     *
     * @param linkText Text in menu on homepage u want to click on. Only implemented for 'Courses'.
     */
    public void clickMenuLink(String linkText) { // will always navigate to courses and not other menu items, only works for our string
        linkCourses.click();
    }
}