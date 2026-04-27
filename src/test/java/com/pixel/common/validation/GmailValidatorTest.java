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

@DisplayName("GmailValidator")
class GmailValidatorTest {

    private GmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new GmailValidator();
    }

    @AfterEach
    void tearDown() {
        validator = null;
    }

    @Nested
    @DisplayName("isValid(value, context)")
    class IsValid {

        @Test
        @DisplayName("isValid_nullValue_returnsFalse")
        void isValid_nullValue_returnsFalse() {
            assertFalse(validator.isValid(null, null));
        }

        @ParameterizedTest(name = "[{index}] \"{0}\" -> valid")
        @CsvSource({
                "user@gmail.com",
                "hamza.malik@gmail.com",
                "a@gmail.com",
                "first.last+tag@gmail.com"
        })
        @DisplayName("isValid_addressEndingWithGmailDomain_returnsTrue")
        void isValid_addressEndingWithGmailDomain_returnsTrue(String input) {
            assertTrue(validator.isValid(input, null));
        }

        @ParameterizedTest(name = "[{index}] \"{0}\" -> invalid")
        @CsvSource({
                "user@yahoo.com",
                "user@hotmail.com",
                "user@gmail.co",
                "user@gmail.com.fake",
                "GMAIL.COM",
                "''",
                "user@",
                "@gmail.com.evil"
        })
        @DisplayName("isValid_addressNotEndingWithGmailDomain_returnsFalse")
        void isValid_addressNotEndingWithGmailDomain_returnsFalse(String input) {
            assertFalse(validator.isValid(input, null));
        }

        @Test
        @DisplayName("isValid_caseSensitiveDomain_returnsFalseForUppercase")
        void isValid_caseSensitiveDomain_returnsFalseForUppercase() {
            assertFalse(validator.isValid("user@GMAIL.COM", null));
        }
    }
}
