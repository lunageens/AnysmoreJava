package pageObjects;

import io.qameta.allure.junit5.AllureJunit5;
import managers.FileReaderManager;
import managers.PageObjectManager;
import managers.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Specifies hooks
 */
@ExtendWith(AllureJunit5.class)
public class Hooks implements BeforeAllCallback, AfterAllCallback {

    static WebDriverManager webDriverManager;
    static PageObjectManager pageObjectManager;

    private static LogAttachmentExtension logAttachmentExtension;
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(Hooks.class);

    // Will execute with beginning each class

    /**
     * Set up driver before each class
     */
    @BeforeAll
    static public void setUp(){
        webDriverManager = new WebDriverManager();
        assertNotNull(webDriverManager, "WebdriverManager created is empty");
        pageObjectManager = new PageObjectManager(webDriverManager.getDriver());
        assertNotNull(pageObjectManager, "PageObjectManager created is empty.");
        assertNotNull(webDriverManager.getDriver(), "Driver is emtpy.");
    }

    @Override
    public void beforeAll(ExtensionContext context){
        LogAttachmentExtension logAttachmentExtension = new LogAttachmentExtension();
        getStore(context).put("logAttachmentExtension", logAttachmentExtension);
    }

    /**
     * Get all test data
     * @return Stream of arguments that we can use in the @MethodSource
     */
    public static Stream<Arguments> getTestData() {
        List<String> studentNames = FileReaderManager.getInstance().getCsvFileReader().getStudentNames();
        List<Integer> groupNumbers = FileReaderManager.getInstance().getCsvFileReader().getStudentGroupNumbers();

        return Stream.iterate(0, i -> i < studentNames.size(), i -> i + 1)
                .map(i -> Arguments.of(studentNames.get(i), groupNumbers.get(i)));
    }

    /**
     * Tear down driver after each class.
     */
    @AfterAll
    static public void tearDown(){
        webDriverManager.closeDriver();
    }

    @Override
    public void afterAll(ExtensionContext context){

        getStore(context).remove("logAttachmentExtension");
        logAttachmentExtension = null;
    }

    /**
     * Get log attachment extension, for logging output in allure report
     * @return LogAttachExtension of this method call
     */
    public static LogAttachmentExtension getLogAttachmentExtension() {
        if (logAttachmentExtension == null) {
            throw new IllegalStateException("LogAttachmentExtension is not available. Ensure Hooks is registered as an extension.");
        }
        return logAttachmentExtension;
    }

    private static ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE);
    }

}
