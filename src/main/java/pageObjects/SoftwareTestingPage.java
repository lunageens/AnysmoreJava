package pageObjects;

import managers.FileReaderManager;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Applying the page pattern, all the methods related to the page where the details of the software testing course
 * are specified are implemented in this class.
 * This is a Selenium Page Object.
 */
// page_url = https://ansymore.uantwerpen.be/courses/software-testing
public class SoftwareTestingPage extends CourseDetailsPage {

    /**
     * WebDriver used to create page and execute methods.
     */
    WebDriver driver;

    /**
     * Constructor of SoftwareTestingPage
     *
     * @param driver WebDriver used to create page and execute methods.
     */
    public SoftwareTestingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        assertNotNull(driver, "The webdriver in SoftwareTestingPage constructor is null.");
    }

    /**
     * Driver goes to Software Testing detailed course page.
     */
    public void navigateToSoftWareTestingPage() {
        driver.get(FileReaderManager.getInstance().getConfigReader().getSoftwareTestingUrl()); // instead of making instance of configfile reader everytime -> use file reader manager with singleton pattern
    }

    /**
     * List of WebElements of assignments and resources links.
     */
    @FindAll(@FindBy(css = "html > body > div > div:nth-of-type(3) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div:nth-of-type(3) > ul:nth-of-type(6) > li > a"))
    List<WebElement> AssignmentsElements;

    /**
     * Get all assignment links that are specified for the course. Do not include resource links.
     *
     * @return List A list with each WebElement the link to particular assignment on the Software Testing course page.
     */
    public List<WebElement> getAssignmentsLinkElements() {
        List<WebElement> assignmentsLinkElements = new ArrayList<>();

        // Get only the links, not including the resource links
        for (WebElement assignmentElement : AssignmentsElements) {
            if (assignmentElement.getText().contains("Assignment")) {
                assignmentsLinkElements.add(assignmentElement);
            }
        }
        return assignmentsLinkElements;
    }

    /**
     * Calculates the number of assignment links.
     * @return int The number of assignment links on the Software Testing course.
     */
    public int getNumberOfAssignmentLinks(){
        return getAssignmentsLinkElements().size(); //size starts at 1
    }


    /**
     * List of WebElements where each item is a full block description of one group.
     */
    @FindAll(@FindBy(css = "html > body > div > div:nth-of-type(3) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div:nth-of-type(3) > ul:nth-of-type(7) > ul"))
    List<WebElement> fullGroups;

    /**
     * Number of groups in total.
     *
     * @return int The number of groups that there are in total.
     */
    public int getNumberOfGroups() {
        return fullGroups.size();
    }

    /**
     * List of WebElements where each item is the full presentation date sentence (e.g., Presentation date: 28 May 2020).
     * Found with CSS selector.
     */
    @FindAll(@FindBy(css = "html > body > div > div:nth-of-type(3) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div:nth-of-type(3) > ul:nth-of-type(7) > ul > li:nth-of-type(1)"))
    List<WebElement> allDatesWebElements;

    /**
     * Reforms date sentence of correct group-number to a date object.
     *
     * @param groupNumber Int that is the group number that is linked with a certain date
     * @return date LocalDate object that is the date where the group with this group number is linked to.
     */
    public LocalDate getDate(int groupNumber) {
        // Get date text of right WebElement
        WebElement fullDateWebElement = allDatesWebElements.get(groupNumber - 1);
        String fullDate = fullDateWebElement.getText();

        // Make text into date
        String dateString = fullDate.replaceAll("Presentation Date: ", ""); // Extract the date portion from the full date string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH); // Define the formatter for parsing the date
        return LocalDate.parse(dateString, inputFormatter); // Parse the date string into a LocalDate object
    }

    /**
     * List of WebElements where each web-element is the full presenter sentence of a group (e.g., Presenters: Luna Geens, Tom Smith, Angela Merkel, Sophie Link)
     * Found with CSS Selector
     */
    @FindAll(@FindBy(css = "html > body > div > div:nth-of-type(3) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div:nth-of-type(3) > ul:nth-of-type(7) > ul > li:nth-of-type(2)"))
    List<WebElement> allPresentersWebElements;

    /**
     * Gets the names of people that are in a group and need to present.
     *
     * @param groupNumber Int that is the group number where u want the presenters from.
     * @return presenters List of Strings where each item is a full name of a presenter.
     */
    public List<String> getPresenters(int groupNumber) {
        // Get presenter text of right WebElement
        WebElement fullPresentersWebElement = allPresentersWebElements.get(groupNumber - 1);
        String fullPresenters = fullPresentersWebElement.getText();

        // Make text into list
        fullPresenters = fullPresenters.replace("Presenters: ", "");
        return List.of(fullPresenters.split(", "));
    }

    /**
     * List of WebElements where each web-element is the full opponent sentence of a group (e.g., Opponents: Luna Geens, Tom Smith, Angela Merkel, Sophie Link)
     * Found with CSS Selector
     */
    @FindAll(@FindBy(css = "html > body > div > div:nth-of-type(3) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > div > div:nth-of-type(3) > ul:nth-of-type(7) > ul > li:nth-of-type(3)"))
    List<WebElement> allOpponentsWebElements;

    /**
     * Gets the names of people that are in a group and have the role of opponent.
     *
     * @param groupNumber Int that is the group number where u want the opponents from.
     * @return List of Strings where each item is a full name of an opponent.
     */
    public List<String> getOpponents(int groupNumber) {
        // Get text from right WebElement
        WebElement fullOpponentsWebElement = allOpponentsWebElements.get(groupNumber - 1);
        String fullOpponents = fullOpponentsWebElement.getText();

        // Make text into List
        fullOpponents = fullOpponents.replace("Opponents: ", "");
        return List.of(fullOpponents.split(", "));
    }

    /**
     * Search the group number of the group where a student is presenter, and where a student is opponent by the name of the student.
     * Assumes that if the student is in any group, that the student is two groups (one as presenter and one as opponent).
     *
     * @param studentName The full name of the student.
     * @return List of Integer that is either null when the student is not in any groups, or contains two Integers,
     * where the first one is the presenter group and the second one is the opponent group.
     */
    public List<Integer> getGroupNumbers(String studentName) {
        // Initiate result variables
        List<Integer> groupNumbers = new ArrayList<>(); // Not just add to this list -> right order
        int presenterGroup = 0;
        int opponentGroup = 0;

        // Loop over all groups
        int totalNumberOfGroups = getNumberOfGroups();
        for (int i = 0; i < totalNumberOfGroups; i++) {
            int groupNum = i + 1;
            // Check if they are presenter in this group
            List<String> presenters = getPresenters(groupNum);
            if (presenters.contains(studentName)) {presenterGroup = groupNum;}
            // Check if they are opponent in this group
            List<String> opponents = getOpponents(groupNum);
            if (opponents.contains(studentName)){opponentGroup = groupNum;}
        }
        // If we have found groups, add them in right order to result variable
        if (presenterGroup != 0){groupNumbers.add(presenterGroup);}
        if (opponentGroup != 0){groupNumbers.add(opponentGroup);
        }
        return groupNumbers;
    }

    /**
     * Searches the date where the given student should present on.
     * Formats that date and puts it into an output message.
     * @param name The name of the student in String format
     * @return String Informational message that contains the date the student should present on.
     */
    public String presencePresenter(String name) {
        // Get presenter group numbers of student, and search the date.
        List<Integer> groupNums = getGroupNumbers(name);
        LocalDate presenterDate = getDate(groupNums.get(0));
        // Reformat date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = presenterDate.format(formatter);
        return "The student " + name + " should present on " + formattedDate + ". ";
    }

    /**
     * Searches the date where the given student should play the role of opponent on.
     * Formats that date and puts it into an output message.
     * @param name The name of the student in String format.
     * @return String Informational message that contains the date the student should present on.
     */
    public String presenceOpponent(String name) {
        // Get opponent group numbers of student, and search the date.
        List<Integer> groupNums = getGroupNumbers(name);
        LocalDate opponentDate = getDate(groupNums.get(1));
        // Reformat date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = opponentDate.format(formatter);

        return "The student " + name + " should play the role of opponent on " + formattedDate + ". ";
    }
}