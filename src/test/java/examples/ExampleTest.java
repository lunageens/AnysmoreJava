package examples;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
public class ExampleTest {

    @Test
    @Tag("Example")
    @DisplayName("Not implemented tests can be disabled")
    @Disabled("Not implemented yet")
    void shouldBeAnotherExample(){
        assertEquals(1,1);
    }

    @Test
    @Tag("Example")
    @DisplayName("Multiple assertions should fail at same time.")
    void methodName() {

        assertAll(() -> assertEquals(1,1),
                () -> assertEquals(1, 0),
                () -> assertEquals(1,2));
    }

    @ParameterizedTest(name = "{0}") // Name of the test is first parameter
    @Tag("Example")
    @DisplayName("Run the same test with different data sets")
    @ValueSource(ints = {1,2,3,4})
    void methodName2(int userNumInput) {
        assertEquals(1, userNumInput);
    }


}
