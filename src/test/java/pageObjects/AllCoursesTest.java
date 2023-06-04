package pageObjects;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import managers.FileReaderManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for first exercise.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Verify all courses from homepage (name and professors)")
@Epic("Verifying the Ansymore web-application")
public class AllCoursesTest extends Hooks {

    private HomePage homePage = pageObjectManager.getHomePage();
    private CoursesPage coursesPage = pageObjectManager.getCoursesPage();

    @RegisterExtension
    static LogAttachmentExtension logAttachmentExtension = new LogAttachmentExtension();

    /**
     * Check if the user is on the courses page after clicking courses in the menu section of the homepage.
     * Passes if driver is at the right URL after performing those actions.
     */
    @Test
    @Tag("Exercise5")
    @Order(1)
    @Step("The user navigates from HomePage to Courses page via menu.")
    void navigateToHomePage() {
        assertNotNull(pageObjectManager, "The pageObjectManager is not null");
        assertNotNull(homePage, "HomePage is not null.");
        assertNotNull(coursesPage, "CoursesPage is not null.");

        homePage.navigateToHomePage();
        homePage.clickMenuLink("Courses");
        try {
            assertEquals(webDriverManager.getDriver().getCurrentUrl(), FileReaderManager.getInstance().getConfigReader().getApplicationCoursesUrl(),
                    "The user is on the overall Courses page.");
        } catch (AssertionError e) {
            logAttachmentExtension.appendLog("ERROR - The user is not on the overall Courses page.");
            throw e;
        }
    }


    /**
     * Checks if there are any courses listed at all on the CoursesPage.
     * Passes if there is 1 or more course listed on the overall CoursesPage.
     */
    @Test
    @Tag("Exercise5")
    @Order(3)
    @Step("The user should see a page with several courses listed.")
    void verifyAllCoursesListed() {
        List<WebElement> courseElements = coursesPage.getAllCourses();

        try {
            assertTrue(!courseElements.isEmpty(), "There are courses listed on the Courses page.");
        } catch (AssertionError e) {
            logAttachmentExtension.appendLog("ERROR - There are no courses listed on the Courses page.");
            throw e;
        }
    }

    /**
     * Checks for each course on the coursesPage if the details page of that course loads.
     * Gives assertion if that's not the case.
     * Checks for each course on that details page if there is 1 or more professor specified.
     * Gives assertion if that's not the case.
     */
    @Test
    @Tag("Exercise5")
    @Order(4)
    @Step("For each course, there should be a more detailed course page that is loaded and there should be a professor.")
    void verifyCourseDetailsForAllCourses() {
        List<WebElement> courseElements = coursesPage.getAllCourses();

        // Initiate result variables
        List<String> courseTitlesNotLoaded = new ArrayList<>();
        List<String> courseTitlesNoProfessor = new ArrayList<>();

        // Loop trough Each course
        int index;
        int numberOfCourses = courseElements.size();
        for (index = 0; index < numberOfCourses; index++) {

            // Get web-element of this course with correct ID (The x time that we are on course page)
            List<WebElement> courseElementsThisDom = coursesPage.getAllCourses();
            WebElement courseElement = courseElementsThisDom.get(index);

            // Go to specific course details page
            courseElement.click();
            CourseDetailsPage courseDetailsPage = pageObjectManager.getNewCourseDetailsPage();

            // Verify course page load for each course
            // TODO Add CoursePageLoaded here?
            if (!courseDetailsPage.isCoursePageLoaded()) {
                courseTitlesNotLoaded.add(courseDetailsPage.getCourseTitle());
            }

            // Verify professor information for each course
            List<String> professorNames = courseDetailsPage.getProfessorName();
            if (professorNames.isEmpty()) {
                courseTitlesNoProfessor.add(courseDetailsPage.getCourseTitle());
            }

            // Back to courses page
            // if we navigate 'back' to other page, web-element shall not be found anymore.
            // This is because driver creates reference ID for the element and its place to find in the dom.
            // Shall not find the element in the dom of the new courses page since it does not have identical ID.
            coursesPage.navigateToCoursesPage();
        }

        // Assertions
        try {
            assertAll(() -> assertTrue(courseTitlesNotLoaded.isEmpty(), "All course detail pages are loaded."),
                    () -> assertTrue(courseTitlesNoProfessor.isEmpty(), "All courses have at least one professor."));
        } catch (AssertionError e) {
            if (!courseTitlesNotLoaded.isEmpty()) {
                String notLoadedMessage = formatListToText(courseTitlesNotLoaded, "not loaded");
                LogAttachmentExtension logAttachmentExtension = Hooks.getLogAttachmentExtension();
                logAttachmentExtension.appendLog(notLoadedMessage);
            }
            if (!courseTitlesNoProfessor.isEmpty()) {
                String noProfessorMessage = formatListToText(courseTitlesNoProfessor, "no professor");
                logAttachmentExtension.appendLog("ERROR - " + noProfessorMessage);
            }
            throw e;
        }
    }

    /**
     * Helper method
     * Formats course Titles in to the right type of assertionText when the page of the course is not loaded or has no professor
     *
     * @param stringList    Titles that did not comply to assertion
     * @param typeAssertion String that can be "not loaded", or "no professor". Otherwise, assertion error.
     * @return String AssertionText that will be given.
     */
    public String formatListToText(List<String> stringList, String typeAssertion) {
        String assertionText;
        assertTrue(typeAssertion.equals("not loaded") || typeAssertion.equals("no professor"), "Formatting assertion texts for this type of assertion is not yet implemented.");

        if (stringList.isEmpty()) {
            assertionText = null;
        } else if (stringList.size() == 1) {
            if (typeAssertion.equals("not loaded")) {
                assertionText = "The page of the course " + stringList.get(0) + " has not loaded successfully.";
            } else {
                assertionText = "The course " + stringList.get(0) + " has no professor.";
            }
        } else {
            // last name with and, others with comma: e.g., Assignments 1, 2, and 3 ...
            String lastTitle = stringList.remove(stringList.size() - 1);
            String otherTitlesJoined = String.join(", ", stringList);
            String allTitlesJointed = otherTitlesJoined + " and " + lastTitle;
            if (typeAssertion.equals("not loaded")) {
                assertionText = "The pages of the courses " + allTitlesJointed + " have not loaded successfully.";
            } else {
                assertionText = "The courses " + allTitlesJointed + " have no professor";
            }
        }
        return assertionText;
    }

}
