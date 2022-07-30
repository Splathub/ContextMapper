package identity.checker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HasKeywordsCheckerTest extends CheckerTest {

    private HasKeywordsChecker newHasKeyWordsChecker(String[] keywords){
        emptyData.put("keywords", new ArrayList<>(Arrays.asList(keywords)));

        return new HasKeywordsChecker(emptyData);
    }

    @Test
    void containsAllKeywords() {
        IdentityChecker checker = newHasKeyWordsChecker(new String[]{"should"});
        assertTrue(checker.check(TEST_SAMPLE_2, root));
    }

    @Test
    void containsSomeKeywords() {
        IdentityChecker checker =  newHasKeyWordsChecker(new String[]{"should", "pricing", "fishing"});
        assertFalse(checker.check(TEST_SAMPLE_2, root ));
    }

    @Test
    void containsNoKeywords() {
        IdentityChecker checker = newHasKeyWordsChecker(new String[]{"fishing", "poker tour", "and bay was filled"});
        assertFalse(checker.check(TEST_SAMPLE_2, root));
    }

    @Test
    void containsDuplicateKeywordsWithOnlyOne() {
        IdentityChecker checker = newHasKeyWordsChecker(new String[]{"should", "should", "prici"});
        assertTrue(checker.check(TEST_SAMPLE_2, root ));
    }

    @Test
    void containsDuplicateKeywordsWithMultipleDuplicates() {
        IdentityChecker checker = newHasKeyWordsChecker(new String[]{"supple", "supplement", "supplement", "supplement", "supple"});
        assertTrue(checker.check(TEST_SAMPLE_1, root));
    }

}
