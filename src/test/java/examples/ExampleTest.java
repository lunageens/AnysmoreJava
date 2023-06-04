package examples;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples for working with JUnit 5 and its tags.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Example Test Class")
@Epic("Working with JUnit 5 - Examples")
public class ExampleTest {

    // use my live template of a junit5 test method by typing the word test

    @Test
    @Tag("Example")
    @Order(1)
    @DisplayName("Determine the order test method are executed in with the Order and TestMethodOrder tag")
    void shouldBeTheFirstMethod(){
        assertEquals(1,1);
    }


    @Test
    @Tag("Example")
    @Order(2)
    @DisplayName("Give methods names too with the DisplayName tag")
    void shouldShowDisplayName(){
        assertEquals(1,1);
    }

    @Test
    @Tag("Example")
    @Order(3)
    @DisplayName("Not implemented tests can be disabled")
    @Disabled("Not implemented yet")
    void disabledExampleTag(){
        assertEquals(1,1);
    }

    @Test
    @Tag("Example")
    @Order(4)
    @DisplayName("Not implemented tests can also be failed")
    void disabledExampleFail(){
        fail("Not implemented yet");
    }

    @Test
    @Tag("Example")
    @Order(5)
    @DisplayName("Multiple assertions should fail at same time.")
    void assertAllExample() {
        assertAll(() -> assertEquals(1,1),
                () -> assertEquals(1, 0),
                () -> assertEquals(1,2));
    }

    @ParameterizedTest(name = "{0}") // Name of the test is first parameter
    @Tag("Example")
    @Order(6)
    @DisplayName("Run the same test with different data sets")
    @ValueSource(ints = {1,2,3,4})
    void parameterizedTestExample(int userNumInput) {
        assertEquals(1, userNumInput);
    }


}
