package net.nowtryz.mcutils.templating;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static net.nowtryz.mcutils.templating.PatternHelper.extractKeys;
import static org.junit.jupiter.api.Assertions.*;

class PatternHelperTest_KeyExtraction {
    @DisplayName("Pattern line matching from invalid input")
    @ParameterizedTest(name = "Extracting keys from `{0}` with error: {1}")
    @CsvSource({
            "%, only 1 char",
            "%%, only 2 chars",
            "%%%, only 3 chars",
            "%%%%, only 4 chars",
            "%%%%%, only 5 chars",
            "%%%%%%, only 6 chars",
            "%%%%%%%, only 7 chars",
            "%%%%%%%%, only 8 chars",
            "%%%%%%%%%%, 10 chars",
            "% %, 2 separated chars",
            "% % %, 3 separated chars",
            "% % % %, 4 separated chars",
            "% % % % % %, 6 separated chars",
            "% % % % % % %, 7 separated chars",
            "% % % % % % % %, 8 separated chars",
    })
    void failOninvalidKeysExtraction(String line, String message) {
        assertThrows(IllegalArgumentException.class, () -> extractKeys(line), message);
    }

    @Test
    @DisplayName("Extracting keys from null must fail")
    void extractKeysFromNull() {
        assertThrows(NullPointerException.class, () -> extractKeys(null));
    }

    @DisplayName("Pattern line matching from valid input")
    @ParameterizedTest(name = "Extracting keys from `{0}` with success")
    @CsvSource({
            // We cannot test with '#' as it is not supported by junit
            "% % % % % % % % %, %|%|%|%|%|%|%|%|%",
            "%     1 2 3     %, %| | |1|2|3| | |%",
            "% % % % ^ % % % %, %|%|%|%|^|%|%|%|%",
            "%%%%%%%%%, %|%|%|%|%|%|%|%|%",
            "%  123  %, %| | |1|2|3| | |%",
            "%%%%^%%%%, %|%|%|%|^|%|%|%|%",
            "% I F W % S B E %, %|I|F|W|%|S|B|E|%",
            "% - - - - - - - %, %|-|-|-|-|-|-|-|%",
            "% % < % ^ % > % %, %|%|<|%|^|%|>|%|%",
            "% / / - / - / / %, %|/|/|-|/|-|/|/|%",
            "% / - / - / - / %, %|/|-|/|-|/|-|/|%",
    })
    void extractValidKeys(String line, String expected) {
        assertArrayEquals(expected.split("\\|"), extractKeys(line).toArray());
    }

    @Test
    @DisplayName("Patterns with #")
    void extractHashtagKeys() {
        assertArrayEquals(new String[]{"#", "I", "F", "W", "#", "S", "B", "E", "#"}, extractKeys("# I F W # S B E #").toArray());
        assertArrayEquals(new String[]{"#", "I", "F", "W", "#", "S", "B", "E", "#"}, extractKeys("#IFW#SBE#").toArray());
    }

    @Test
    @DisplayName("Patterns beginning or ending with spaces")
    void extractSpaces() {
        assertArrayEquals(new String[]{" ", " ", " ", "A", "B", "C", " ", " ", " "}, extractKeys("   ABC   ").toArray());
        assertArrayEquals(new String[]{" ", " ", " ", "A", "B", "C", " ", " ", " "}, extractKeys("      A B C      ").toArray());
    }
}
