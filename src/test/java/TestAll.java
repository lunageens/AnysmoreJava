
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

// TODO README.md

/**
 * Full test suite.
 */
@SelectPackages({"examples", "pageObjects"}) // denk da je ook select classes kan done -> ja met .class
@IncludeTags({"Example", "Exercise5", "Exercise6", "Exercise7", "Exercise8"})
@Suite
@SuiteDisplayName("Total test suite.")
public class TestAll {
}