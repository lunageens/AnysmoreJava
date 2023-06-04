package pageObjects;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import managers.FileReaderManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for third exercise.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Verify student groups in software testing course")
@Epic("Verifying the Ansymore web-application")
public class AssignmentGroupsTest extends Hooks {

    private SoftwareTestingPage softwareTestingPage = pageObjectManager.getSoftwareTestingPage();

    @RegisterExtension
    static LogAttachmentExtension logAttachmentExtension = new LogAttachmentExtension();


    /**
     * Passes if driver is at the correct URL.
     */
    @Test
    @Tag("Exercise7")
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
     * The given user should be at least in one of the groups.
     * Fails if the user given is not in any group.
     *
     * @param name        The full name of the student
     * @param groupNumber The group number of the student.
     */
    @ParameterizedTest(name = "Student {0}")
    @Tag("Exercise7")
    @Order(2)
    @MethodSource("getTestData")
    @Step("The given student should be in at least one group.")
    public void verifyStudentGroupMembership(String name, Integer groupNumber) {
        try {
            assertFalse(softwareTestingPage.getGroupNumbers(name).isEmpty(), "The student is in at least one group.");
        } catch (AssertionError e) {
            String errorMessage = "ERROR - The student " + name + " is not in any group.";
            logAttachmentExtension.appendLog(errorMessage);
            throw e;
        }
    }

    /**
     * User receives warning when not in the given student group.
     *
     * @param name        The full name of the student
     * @param groupNumber The group number of the student.
     */
    @ParameterizedTest(name = "Student {0} in group {1}")
    @Tag("Exercise7")
    @Order(3)
    @MethodSource("getTestData")
    @Step("The user receives warning if the given student is not in the given group.")
    public void verifyStudentGroupWarning(String name, Integer groupNumber) {
        boolean isInThatStudentGroup = (softwareTestingPage.getPresenters(groupNumber).contains(name)) ||
                (softwareTestingPage.getOpponents(groupNumber).contains(name));
        boolean isInAnyStudentGroup = !softwareTestingPage.getGroupNumbers(name).isEmpty();
        if (isInAnyStudentGroup) {
            if (!isInThatStudentGroup) {
                String warningMessage = "WARN - Student " + name + " is not in a group with student number " + groupNumber + ". ";
                logAttachmentExtension.appendLog(warningMessage);
            }
        } else {
            try {
                assertTrue(isInAnyStudentGroup, "The student is in at least one group.");
            } catch (AssertionError e) {
                String errorMessage = "ERROR - Student " + name + " is not in any group.";
                logAttachmentExtension.appendLog(errorMessage);
                throw e;
            }
        }
    }

}
