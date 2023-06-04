package pageObjects;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import managers.FileReaderManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for fourth exercise.
 */
@Epic("Verifying the Ansymore web-application")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Verify Mandatory Presence of a student in in software testing course")
public class AssignmentPresenceTest extends Hooks {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentGroupsTest.class);
    private SoftwareTestingPage softwareTestingPage = pageObjectManager.getSoftwareTestingPage();

    @RegisterExtension
    static LogAttachmentExtension logAttachmentExtension = new LogAttachmentExtension();


    /**
     * Passes if driver is at the correct URL.
     */
    @Test
    @Tag("Exercise8")
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


    @ParameterizedTest(name = "Student {0}")
    @Tag("Exercise8")
    @Order(2)
    @MethodSource("getTestData")
    @Step("When the user gives a name, he should see his mandatory presence as presenter")
    void presencePresenter(String name, Integer groupNumber) {
        try {
            assertTrue(!softwareTestingPage.getGroupNumbers(name).isEmpty(), "The student is in at least one group.");
        } catch (AssertionError e) {
            String errorMessage = "The student " + name + " is not in any group.";
            logAttachmentExtension.appendLog("ERROR - " + errorMessage);
            throw e;
        }
        logAttachmentExtension.appendLog("INFO - " + softwareTestingPage.presencePresenter(name));
    }

    @ParameterizedTest(name = "Student {0}")
    @Tag("Exercise8")
    @Order(3)
    @MethodSource("getTestData")
    @Step("When the user gives a name, he should see his mandatory presence as opponent")
    void presenceOpponent(String name, Integer groupNumber) {
        try {
            assertTrue(!softwareTestingPage.getGroupNumbers(name).isEmpty(), "The student is in at least one group.");
        } catch (AssertionError e) {
            String errorMessage = "The student " + name + " is not in any group.";
            logAttachmentExtension.appendLog("ERROR - "+ errorMessage);
            throw e;
        }
        logAttachmentExtension.appendLog("INFO - " + softwareTestingPage.presenceOpponent(name));
    }

}
