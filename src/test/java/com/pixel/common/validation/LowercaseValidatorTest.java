package com.pixel.common.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LowercaseValidator")
class LowercaseValidatorTest {

    private LowercaseValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LowercaseValidator();
    }

    @AfterEach
    void tearDown() {
        validator = null;
    }

    @Nested
    @DisplayName("isValid(value, context)")
    class IsValid {

        @Test
        @DisplayName("isValid_nullValue_returnsTrue")
        void isValid_nullValue_returnsTrue() {
            assertTrue(validator.isValid(null, null));
        }

        @ParameterizedTest(name = "[{index}] \"{0}\" -> valid")
        @CsvSource(value = {
                "''",
                "hamza",
                "lowercase",
                "with spaces and digits 123",
                "underscores_and-dashes",
                "1234567890",
                "!@#$%^&*()"
        })
        @DisplayName("isValid_valueWithoutUppercaseLetters_returnsTrue")
        void isValid_valueWithoutUppercaseLetters_returnsTrue(String input) {
            assertTrue(validator.isValid(input, null));
        }

        @ParameterizedTest(name = "[{index}] \"{0}\" -> invalid")
        @CsvSource({
                "Hello",
                "HELLO",
                "camelCase",
                "MixedCase123",
                "A",
                "lowerThenUPPER"
        })
        @DisplayName("isValid_valueContainingUppercaseLetters_returnsFalse")
        void isValid_valueContainingUppercaseLetters_returnsFalse(String input) {
            assertFalse(validator.isValid(input, null));
        }
    }
}
