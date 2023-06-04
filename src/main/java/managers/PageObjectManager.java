package managers;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import pageObjects.CourseDetailsPage;
import pageObjects.CoursesPage;
import pageObjects.HomePage;
import pageObjects.SoftwareTestingPage;

/**
 * Makes new page instance or returns the one already made.
 * In the case of multiple-step definition files, we will be creating an object of Pages again and again. Which is against the coding principle.
 * <p>
 * To avoid this situation, we can create a Page Object Manager.
 * The duty of the Page Object Manager is to create the page's object and also to make sure that the same object
 * should not be created again and again. But to use a single object for all the step definition files.
 */
public class PageObjectManager {

    /**
     * WebDriver that is used to create the different pages.
     */
    private WebDriver driver;

    /**
     * The one instance of the Selenium Page of Software Testing course, following the page pattern and
     * avoiding the creation of different objects when using different steps.
     */
    private SoftwareTestingPage softwareTestingPage;

    /**
     * Selenium Page of Software Testing course, following the page pattern
     * Here we are not avoiding the creation of different objects when using different step files,
     * because we need different URLs for different courses.
     */
    private CourseDetailsPage courseDetailsPage;

    /**
     * The one instance of the Selenium Page of all Courses, following the page pattern and
     * avoiding the creation of different objects when using different steps.
     */
    private CoursesPage coursesPage;

    /**
     * The one instance of the Selenium Page of the homepage, following the page pattern and
     * avoiding the creation of different objects when using different steps.
     */
    private HomePage homePage;

    /**
     * Constructor of the PageObjectManager
     *
     * @param driver Driver used to create all page instances.
     */
    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
        assertNotNull(this.driver, "driver is empty");
    }

    /**
     * Get the homepage instance
     * If there is not a Homepage created yet, make a new one.
     * Otherwise, use the one we have.
     *
     * @return HomePage The one instance of this class that is used in the scenario.
     */
    public HomePage getHomePage() {
        assertNotNull(this.driver, "driver is empty");
        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
    }

    /**
     * Get the CoursesPage instance
     * If there is not a CoursesPage created yet, make a new one.
     * Otherwise, use the one we have.
     *
     * @return CoursesPage The one instance of this class that is used in the scenario.
     */
    public CoursesPage getCoursesPage() {
        assertNotNull(this.driver, "driver is empty");
        return (coursesPage == null) ? coursesPage = new CoursesPage(driver) : coursesPage;
    }

    /**
     * Create a new instance of a CourseDetailsPage
     * Here, we do not follow the method of the other pages because we need a new page for each course.
     *
     * @return CourseDetailsPage new instance of the CourseDetailsPage the driver is currently on.
     */
    public CourseDetailsPage getNewCourseDetailsPage() {
        assertNotNull(this.driver, "driver is empty");
        courseDetailsPage = new CourseDetailsPage(driver); // do make new page every time -> changes url per course
        return courseDetailsPage;
    }

    /**
     * Get the SoftwareTestingPage instance
     * If there is not a SoftwareTestingPage created yet, make a new one.
     * Otherwise, use the one we have.
     *
     * @return SoftwareTestingPage The one instance of this class that is used in the scenario.
     */
    public SoftwareTestingPage getSoftwareTestingPage() {
        assertNotNull(this.driver, "driver is empty");
        return (softwareTestingPage == null) ? softwareTestingPage = new SoftwareTestingPage(driver) : softwareTestingPage;
    }
}