package pageObjects;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import managers.FileReaderManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for second exercise
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Verify assignment links in software testing course")
@Epic("Verifying the Ansymore web-application")
public class AssignmentLinksTest extends Hooks {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentLinksTest.class);

    private SoftwareTestingPage softwareTestingPage = pageObjectManager.getSoftwareTestingPage();

    @RegisterExtension
    static LogAttachmentExtension logAttachmentExtension = new LogAttachmentExtension();


    /**
     * Passes if driver is at the correct URL.
     */
    @Test
    @Tag("Exercise6")
    @Order(1)
    @Step("The user is on the Software Testing course page.")
    void navigateToSoftwareTestingPage() {
        softwareTestingPage.navigateToSoftWareTestingPage();
        try {
            assertEquals(webDriverManager.getDriver().getCurrentUrl(), FileReaderManager.getInstance().getConfigReader().getSoftwareTestingUrl(),
                    "The user is on the Software Testing course page.");
        } catch (AssertionError e) {
            logAttachmentExtension.appendLog("ERROR - The user is not on the Software Testing course URL.");
            throw e;
        }
    }

    /**
     * Passes if there are 1 or more assignments listed on the Software Testing page.
     */
    @Test
    @Tag("Exercise6")
    @Order(2)
    @Step("The user should see several assignment links.")
    void verifyAssignmentLinksPresent() {
        List<WebElement> assignmentElements = softwareTestingPage.getAssignmentsLinkElements();
        try {
            assertFalse(assignmentElements.isEmpty(), "There is at least one assignment link.");
        } catch (AssertionError e) {
            logAttachmentExtension.appendLog("ERROR - There are no assignment links.");
            throw e;
        }
    }

    /**
     * Check for each assignment link that the link exists.
     * Gives a warning when the links of certain assignments do not exist.
     */
    @Test
    @Tag("Exercise6")
    @Order(3)
    @Step("The user should receive a warning when an assignment link doesn't exist")
    void verifyAssignmentsLinkExistenceWarning() {
        // Get data links
        List<WebElement> Links = softwareTestingPage.getAssignmentsLinkElements();
        int numberOfLinks = softwareTestingPage.getNumberOfAssignmentLinks();

        // Loop over all links
        List<Integer> nonExistentLinks = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++) {

            // Get data of that link
            WebElement link = Links.get(i); // Get WebElement
            String urlText = link.getAttribute("href"); // Retrieve url associated with link

            // Try connecting to server
            boolean doesLinkExists;
            try {
                URL linkURL = new URL(urlText); // Make new URL object
                HttpURLConnection connection = (HttpURLConnection) linkURL.openConnection(); // Make new connection
                connection.setRequestMethod("GET"); // Try connecting
                int responseCode = connection.getResponseCode(); // Get response code
                doesLinkExists = responseCode != HttpURLConnection.HTTP_NOT_FOUND; // If no 200, return false
            } catch (IOException e) { // If not even response code and error, also false
                doesLinkExists = false;
            }

            // If that URL does not exist, add them to list
            if (!doesLinkExists) {
                nonExistentLinks.add(i + 1);
            } // If that URL doesn't exist, add to result var
        }

        // Give warning if there are links in the list
        if (!nonExistentLinks.isEmpty()) { // only give warning when there is a link that does not exist
            String warningText = formatLinkExistenceWarning(nonExistentLinks);
            logAttachmentExtension.appendLog("WARN - " + warningText);
        }
    }

    /**
     * Helper method
     * Precondition: there is at least one non-existent link.
     * Formats the warning text of the non-existent links
     *
     * @param nonExistentLinks A list of Integers tht are the number of the assignments with non-existent links
     * @return String WarningText with formatted numbers (comma's, and, ...)
     */
    public String formatLinkExistenceWarning(List<Integer> nonExistentLinks) {
        assertNotNull(nonExistentLinks, "There should be at least one non-existent link to format a warning message.");
        String warningText;
        if (nonExistentLinks.size() == 1) { // singular
            warningText = "Assignment " + nonExistentLinks.get(0).toString() + " has a link that does not exists on the server.";
        } else { // plural. Use formatNumListToText for numbers
            warningText = "Assignments " + formatNumListToText(nonExistentLinks) + " have a link that does not exists on the server.";
        }
        return warningText;
    }

    /**
     * For each assignment listed, checks that the link is of the format  '/system/files/uploads/courses/Testing/assignment[NR].pdf'
     * Gives assertion if the layout differs for one of the assignments.
     */
    @Test
    @Tag("Exercise6")
    @Order(4)
    @Step("The user should verify the link format of each assignment link")
    void verifyAssignmentsLinkFormat() {
        // Get data links
        List<WebElement> Links = softwareTestingPage.getAssignmentsLinkElements();
        int numberOfLinks = softwareTestingPage.getNumberOfAssignmentLinks();

        // Loop over all links
        List<Integer> nonFormattedLinks = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++) {
            WebElement link = Links.get(i); // Get WebElement
            String urlText = link.getAttribute("href"); // Retrieve url associated with link
            String correctText = "/system/files/uploads/courses/Testing/assignment" + i + 1 + ".pdf"; //What URL should be
            if (!urlText.equals(correctText)) { // Not the right format
                nonFormattedLinks.add(i + 1); // Then add assignment number to result
            }
        }

        try {
            assertNotNull(nonFormattedLinks, "The list of links with an incorrect format is empty.");
        } catch (AssertionError e) {
            logAttachmentExtension.appendLog("ERROR - " + formatLinkFormatAssertion(nonFormattedLinks));
            throw e;
        }
    }

    /**
     * Helper method
     * Formats the assertion for the links that are not formatted correctly
     *
     * @param nonFormattedLinks A list of Integers tht are the number of the assignments with non-existent links
     * @return String AssertionText with formatted numbers
     */
    public String formatLinkFormatAssertion(List<Integer> nonFormattedLinks) {
        String assertionText;
        if (nonFormattedLinks.isEmpty()) { // No assertion
            assertionText = null;
        } else if (nonFormattedLinks.size() == 1) { // Singular
            assertionText = "Assignment " + nonFormattedLinks.get(0).toString() + " has a link with an incorrect format.";
        } else { // Plural
            assertionText = "Assignments " + formatNumListToText(nonFormattedLinks) + " have a link with an incorrect format.";
        }
        return assertionText;
    }

    /**
     * Helper method
     * Precondition: at least two numbers to format
     * Formats a list of integers [1,2, 3] to a string of the format "1, 2 and 3"
     * Used to make warnings and assertion texts.
     *
     * @param numList List of integers
     * @return String Formatted integers in text form
     */
    public String formatNumListToText(List<Integer> numList) {
        assertTrue(numList.size() > 1, "There should be at least two numbers to format.");
        // From integer list to string list
        List<String> formattedList = new ArrayList<>();
        for (Object num : numList) {
            formattedList.add(num.toString());
        }
        // Last name with and, others with comma: e.g., Assignments 1, 2, and 3 ...
        String lastNum = formattedList.remove(formattedList.size() - 1);
        String otherNumJoined = String.join(", ", formattedList);
        return otherNumJoined + " and " + lastNum;
    }

}
