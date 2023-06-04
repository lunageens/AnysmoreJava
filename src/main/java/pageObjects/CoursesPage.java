package pageObjects;

import managers.FileReaderManager;
import managers.PageObjectManager;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Applying the page pattern, all the methods related to the page where all the courses are listed
 * are specified are implemented in this class.
 * This is a Selenium Page Object.
 */
// page_url = https://ansymore.uantwerpen.be/courses
public class CoursesPage {

    /**
     * WebDriver used to create page and execute methods.
     */
    private WebDriver driver;

    /**
     * Constructor of CoursesPage
     *
     * @param driver WebDriver used to create page and execute methods.
     */
    public CoursesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        assertNotNull(driver, "The webdriver in CoursesPage constructor is null" );
    }

    /**
     * Driver goes to CoursesPage.
     */
    public void navigateToCoursesPage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationCoursesUrl());  // instead of making instance of configfile reader everytime -> use file reader manager with singleton pattern
    }

    /**
     * List of WebElements where each item is a text on the course page (either a professor or the link to the course itself), found by css operator.
     */
    @FindAll(@FindBy(css = "div.views-field"))
    private List<WebElement> courseElements;

    /**
     * Get all courses listed on the courses page.
     *
     * @return List with as each item the WebElement that is a link to each course.
     */
    public List<WebElement> getAllCourses() {
        List<WebElement> courseNameElements = new ArrayList<>();
        for (WebElement courseElement : courseElements) {  // gets the names of courses but also professors underneath it.
            if (!courseElement.getText().contains("Professor:")) {
                courseNameElements.add(courseElement); // Make new list with only web-elements that are courses themselves
            }
        }
        return courseNameElements;
    }

}