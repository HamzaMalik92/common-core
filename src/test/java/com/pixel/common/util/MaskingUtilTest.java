package com.pixel.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MaskingUtil")
class MaskingUtilTest {

    @Nested
    @DisplayName("hide(value)")
    class Hide {

        @Test
        @DisplayName("hide_nullValue_returnsEmptyString")
        void hide_nullValue_returnsEmptyString() {
            assertEquals("", MaskingUtil.hide(null));
        }

        @Test
        @DisplayName("hide_emptyValue_returnsEmptyString")
        void hide_emptyValue_returnsEmptyString() {
            assertEquals("", MaskingUtil.hide(""));
        }

        @ParameterizedTest(name = "[{index}] hide(\"{0}\") -> [PROTECTED]")
        @CsvSource({
                "secret",
                "1234",
                "' '",
                "a",
                "very-long-secret-value-that-should-be-fully-masked"
        })
        @DisplayName("hide_nonEmptyValue_returnsProtectedPlaceholder")
        void hide_nonEmptyValue_returnsProtectedPlaceholder(String input) {
            assertEquals("[PROTECTED]", MaskingUtil.hide(input));
        }
    }

    @Nested
    @DisplayName("maskEmail(email)")
    class MaskEmail {

        @ParameterizedTest(name = "[{index}] maskEmail(\"{0}\") -> [INVALID EMAIL]")
        @CsvSource(value = {
                "NULL",
                "''",
                "no-at-symbol",
                "plainaddress",
                "missingdomain.com"
        }, nullValues = "NULL")
        @DisplayName("maskEmail_missingAtSymbol_returnsInvalidEmailMarker")
        void maskEmail_missingAtSymbol_returnsInvalidEmailMarker(String input) {
            assertEquals("[INVALID EMAIL]", MaskingUtil.maskEmail(input));
        }

        @ParameterizedTest(name = "[{index}] maskEmail(\"{0}\") -> {1}")
        @CsvSource({
                "a@gmail.com,         ***@gmail.com",
                "ab@gmail.com,        ***@gmail.com",
                "abc@gmail.com,       ***@gmail.com"
        })
        @DisplayName("maskEmail_localPartUpToThreeChars_returnsTripleAsterisksAndDomain")
        void maskEmail_localPartUpToThreeChars_returnsTripleAsterisksAndDomain(String input, String expected) {
            assertEquals(expected, MaskingUtil.maskEmail(input));
        }

        @ParameterizedTest(name = "[{index}] maskEmail(\"{0}\") -> {1}")
        @CsvSource({
                "abcd@gmail.com,           abc****@gmail.com",
                "hamza@gmail.com,          ham****@gmail.com",
                "john.doe@company.org,     joh****@company.org",
                "user_name@sub.domain.io,  use****@sub.domain.io"
        })
        @DisplayName("maskEmail_localPartLongerThanThreeChars_keepsFirstThreeAndAppendsAsterisks")
        void maskEmail_localPartLongerThanThreeChars_keepsFirstThreeAndAppendsAsterisks(String input, String expected) {
            assertEquals(expected, MaskingUtil.maskEmail(input));
        }

        @Test
        @DisplayName("maskEmail_emptyLocalPart_returnsTripleAsterisksAndDomain")
        void maskEmail_emptyLocalPart_returnsTripleAsterisksAndDomain() {
            assertEquals("***@gmail.com", MaskingUtil.maskEmail("@gmail.com"));
        }
    }
}
