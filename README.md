
**This maven project is set up to use Selenium WebDriver to test the GUI of a web-application (and not the implementation).
It uses testing framework JUnit 5, with Behaviour-Driven development (BDD) mindset.
It can be tested on different browsers and operating programs.
The implementation of the tests is written in Java en uses logging framework log4j.**

# 1 Use of different frameworks
## 1.1 Selenium Webdriver
Selenium is a powerful open-source framework for automated web testing.
It has two different versions, 1) the WebDriver, and 2) the IDE.
Using the Selenium IDE we can easily record and playback scripts.
For this particular exercises, we could use the Selenium IDE to export code, but these scripts should be runnable java
programs (which is why we will use maven). Therefore, I choose to use the **Selenium Webdriver** dependency
(see [`WebDriverManager.java`](src/main/java/managers/WebDriverManager.java) class).

## 1.2 BDD testing 
We will use Selenium with a mindset of Behaviour-Driven Development (BDD).
BDD focuses on defining and specifying desired behaviours of (the GUI of) the web-application in a common natural language.
However, I have not written scenarios in natural languages or use a specific framework.

### 1.2.1 Scenario description
As mentioned before, there is no separate scenario file. 
In the test classes I have made use of the Allure annotations `@Step`, `@Feature` and `@Epic` to give the project some 
structure. 

### 1.2.2 Scenario implementation
The implementation of the tests can be found in the test directory.

-  The actual implementation is written following the page pattern, where methods are categorized per page (see the package [`pageObjects`](src/main/java/pageObjects)). 
-  The tests are written in the test directory and are categorized per exercise (see the package [`pageObject`](src/test/java/pageObjects)).
-  Both are written in **Java**.
-  The **JUnit** testing framework for Java is used to support the implementation of assertions in the test, to check if the tests pass or fails. We have opted for JUnit 5, which has different modules in the pom.xml file (in contradiction to JUnit 4). With the help of Cucumber, the test are run by JUnit with the `mvn install` command. Specific configurations about that process are specified in the [`TestAll.java`](src/test/java/TestAll.java) class, which is the overall suite. This is the overall class we use to run the tests. 

Junit also supports [`Hooks.java`](src/test/java/pageObjects/Hooks.java), that tell the program what to do before
and after the execution of each class and method, for example.

We have used several JUnit annotations and other features, as illustrated in the [`ExampleTest.java`](src/test/java/examples/ExampleTest.java) class.

## 1.3 Logging frameworks
**Log4j** and **SLF4J** are used to log application-related messages, such as the warnings were needed in some tests.
Therefore, it is used in the Step files.

-  Log4j is a logging library to log messages in Java applications. It allows developers to configure logging behavior dynamically and provides various logging levels (e.g., DEBUG, INFO, WARN, ERROR) to differentiate the severity of logged messages. Log4j also supports different output targets such as console, file, and database. The logging messages are configured in the [`log4j.properties`](src/test/resources/log4j.properties) file. We use this framework in the Step classes to give warnings and informational messages to the user.
-  SLF4J (Simple Logging Facade for Java) provides a common API for various logging frameworks, including Log4j.

Browser-specific logs in Firefox were long and suppressed via the following line in the [`WebDriverManager.java`](src/main/java/managers/WebDriverManager.java) class:
`System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");`

The log4j framework is configured that it will include log output into the Allure reports, thanks to the[`LogAttachmentExtension.java`](src/test/java/pageObjects/LogAttachmentExtension.java) class.

## 1.5 Reporting frameworks
Using the **JavaDoc** Tools of IntelliJ IDEA, one can find the index.html file in the [`javadoc`](javadoc/index.html) directory that describes the utility of each method, class and variable.
> URL Javadoc: [Ansymore site on Netlify](https://ansymojavasite.netlify.app/).

With the test results, u can also generate additional **Allure** reports that are more detailed.
For Windows operating systems, Allure needs to be downloaded via the PowerShell Command Prompt.
In case u haven't installed Scope already, run the following commands first:

``` 
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser 
irm get.scoop.sh | iex
```

To generate the Allure reports and open it in your default browser, use the following command:

```
mvn clean install
allure serve 
```

Don't forget to end the session in the terminal afterwards, and remove the allure-results directory when running again. 


# 2 Expected behaviour
The program is set up to test certain behaviours of the [Ansymo web-application](https://ansymore.uantwerpen.be). One could alter the `@IncludeTags` key in the [`TestAll.java`](src/test/java/TestAll.java) class to only test the script from specific exercises.

## 2.1 Browse Courses
The [`AllCoursesTest.java`](src/test/java/pageObjects/AllCoursesTest.java) file refers to `@Exercise5`, where we browse courses from the homepage on to the overall courses page and then for each course, a detailed page with more information. The script is the following:

> Create a test script that navigates to all the different courses from the menu.
> The script should loop over the different menu items and verify that each page is loaded.
> Verify that each course has a professor (field not empty).
> The script should only fail when a page cannot be loaded or when the course does not have a professor.

## 2.2 Browse Software Testing
Firstly, in `@Exercise6`, a script should check the link existence and format of assignments of the software testing
course. This is  implemented in the [`AssignmentLinksTest.java`](src/test/java/pageObjects/AssignmentLinksTest.java). The test script is the following:

> Create a script that navigates to the “Software Testing” course.
> For each assignment, verify that the link to the assignment is: “/system/files/uploads/courses/Testing/assignment<NR>.pdf”.
> The script should fail when the link layout differs. Make sure that the script is dynamic! Adding or removing assignments should not result in a failure.
> Verify that the document (link) exists on the server. Give a warning when it does not exist.

Secondly, in `@Exercise7` and `@Exercise8`, the user gives a student group number and student name as input. A student group has one date, and then different presenters and opponents. A student that is in any group, should be in one group as opponent and one group as presenter.
For this input, we specified some test data in the [`TestData.csv`](configs/TestData.csv) file. This data is read in by the [`CSVFileReader.java`](src/main/java/dataProviders/CSVFileReader.java) class. All those
file readers are managed by the [`FileReaderManager.java`](src/main/java/managers/FileReaderManager.java) class to avoid extensive object creation. In the [`Hooks.java`](src/test/java/pageObjects/Hooks.java) class, we make use of a method `getTestData()` that is 
extended by all test classes (of the application) and is used to provide the data to run parameterized tests. 

A student name can be in no, or two groups (one time as a presenter, and one time as opponent). In `@Exercise7`, this is checked. One can find the code in the 
[`AssingmentGroupsTest.java`](src/test/java/pageObjects/AssignmentGroupsTest.java) class. The script goes as follows:

> Create a function with student and group as input that verifies whether the student is in the provided group.
> Give a warning when the student is not in the provided group. Fail if the student cannot be found in any of the groups.
> Create a script that verifies that you are in the correct group.

In `@Exercise8`, we can use that function to create a function that shows the user the date he should present and the date he should be an opponent. 
The test script is implemented in the [`AssingmentPresenceTest.java`](src/test/java/pageObjects/AssignmentPresenceTest.java) and is specified as follows:

> Using the function from exercise 7:
>   A) Create a function that returns the date a student needs to present a lecture.
>   B) Create a function that returns the date a student is an opponent.
> Create a script that verifies when you needed to present/were the opponent using the above functions.

Note that I did not make use of unit tests per class or kept following the page pattern of the main directory, because this would create a very long Software Testing class.

# 3 Configurations
When using this program, alter the [`Configuration.properties`](configs/Configuration.properties) file as you wish. The different keys have the following meaning:

-  The urlHome is the URL to the home page of the application. In case this URL ever changes, please alter this value.
-  The urlCourses is the URL to the page where all the courses are listed together. In case this URL ever changes, please alter this value.
-  The urlSoftwareTesting is the URL to the detailed page about the Software Testing course. In case this URL ever changes, please alter this value.
-  The environment key can have values as specified in the [`EnvironmentType`](src/main/java/enums/EnvironmentType.java) enum file. Currently, only local browsing is implemented.
-  The browser key can have values as specified in the [`DriverType`](src/main/java/enums/DriverType.java) enum file. It is the browser where the tests are run on using Selenium. The program is written and tested with a recent version of Firefox (133.0.2). Make sure u use a browser (version) that is compatible with the Selenium webdriver version you're using. For example, Selenium WebDriver 4x requires Firefox 78 or greater. Lower versions of Selenium also require the use of an additional webdriver ([`geckodriver.exe`](drivers/geckodriver.exe)) for Firefox. Please be aware that after all tests are run, this program closes all processes related to that browser in order to enhance memory use.
-  The implicitlyWait key specifies the number of seconds to tell the web driver to wait for a certain amount of time before it throws a “No Such Element Exception”. Once we set the time, the web driver will wait for the element for that time before throwing an exception.
-  The windowMaximize key is set to true if u want the browser to maximize the window while testing.
-  The headless key is set to true if u do not want to see the browser GUI while running the tests.
-  The operatingSystem key can have values as specified in the [`OperatingSystemType`](src/main/java/enums/OperatingSystemType.java) enum file.

The [`Configuration.properties`](configs/Configuration.properties) file is read in with the [`ConfigFileReader.groovy`](src/main/java/dataProviders/ConfigFileReader.java) class, which is managed by the [`FileReaderManager.java`](src/main/java/managers/FileReaderManager.java) class using the Singleton pattern.

# 4 Technicalities
In the project, several managers and other technical classes have been implemented.

## 4.1 Reading (configuration) files
A [`Configuration.properties`](configs/Configuration.properties) file avoids hardcoding URLs, BrowserTypes, and others in the code. For example, this gives the user the option to run the tests on different browsers and operating systems.

The [`ConfigFileReader.groovy`](src/main/java/dataProviders/ConfigFileReader.java) reads the configuration file and passes the value with get() methods. In the case of type of browser, operating system and environment, it gives back the correct type that is specified in the [`enums`](src/main/java/enums) package.

If we continued using this Cucumber Selenium Framework, we would have multiple file readers. Therefore, it is better to have an overall [`FileReaderManager.java`](src/main/java/managers/FileReaderManager.java) class above all File Readers.
It is also better to make the file manager class as singleton. This limits the number of objects of this class to one. We then have a method `FileReaderManager.getInstance()` that is our global access point for that one object.
For each FileReader, we call the get method on that one instance of the 'FileReaderManager' class. Logically, we can only have one configReader (for example). The `FileReaderManager.getInstance().getConfigReader()` is our global point of access for configuration variables throughout the whole system. By implementing the **Singleton Design Pattern**, the file is only read once, and we limit object creation.

Note that, as explained earlier, the test data is also read in a CSV file reader and managed by this file reader manager class.

## 4.2 Managing WebDrivers
One could initiate the webdriver in the constructor of the Step file and close the driver in the implementation of the last step in the step file. However, there are multiple reasons to implement a [`WebDriverManager.java`](src/main/java/managers/WebDriverManager.java) class:

-  It Allows us to let the user specify different type of browser and environment and run the test correctly.
-  On top of that, it makes more sense to handle the logic of creating and quitting the WebDriver in a separate class instead of in the tests or Hooks. The test should not be worried about how to start the driver, but just needs a driver to execute the script.
-  Most importantly, the `WebDriverManager.getDriver()` method allows us to share a driver over different steps of a scenario instead of creating one for each page of each step needed.

## 4.3 Managing Selenium Object pages
We want to avoid having one really long Step file with thousands of steps in. We would want to reuse some code for different steps. To better manage the code and to improve re-usability, the **Page Object Design Pattern** suggests programmers to divide an application in different web pages. In the [`pageObjects`](src/main/java/pageObjects) package , every page in our application will be represented by a unique class of its own.
The **Selenium PageFactory** is an inbuilt tool for Selenium WebDriver and allows us to better optimize the code in classes related to the page pattern.

One could make new objects of pages for every (step of) scenario. We do not want to create the same pages over and over again. To avoid this situation, a [`PageObjectManager.java`](src/main/java/managers/PageObjectManager.java) is implemented. It creates a single object of each page for all the step definition files.

## 4.4 BaseClass and Hooks
The Hooks class helps us to avoid code duplication. It allows us to do certain things before and after each scenario, and close all browser processes after all scenarios are tested.
