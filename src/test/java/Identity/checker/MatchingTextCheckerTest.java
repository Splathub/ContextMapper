package Identity.checker;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MatchingTextCheckerTest {

    public static  char[] TEST_SAMPLE_1 = ("You should read this pricing supplement together with the accompanying prospectus, as supplemented by the accompanying\n" +
            "a prospectus supplement relating to our Series A medium-term notes, of which these notes are a part, and the more detailed\n" +
            "information contained in the accompanying product supplement. This pricing supplement, together with the documents\n" +
            "listed below, contains the terms of the notes and supersedes all other prior").toCharArray();

    @Test
    public void shouldDetectWordWhenPresentInContext(){
        IdentityChecker identityChecker = new MatchingTextChecker(Map.of("type", "should read this pricing"));
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 4, 20));
    }

    @Test
    public void shouldNotMatchIfKeywordIsNotAtIndex0(){
        IdentityChecker identityChecker = new MatchingTextChecker(Map.of("type", "should read this pricing"));
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 4, 20));
    }

    @Test
    public void shouldReturnFalseIfWordIsNotPresent(){
        IdentityChecker identityChecker = new MatchingTextChecker(Map.of("type", "should read this no pricing"));
        assertFalse(identityChecker.check(TEST_SAMPLE_1, 0, 20));
    }

    @Test
    public  void shouldDetectKeyWordAtStart(){
        IdentityChecker identityChecker = new MatchingTextChecker(Map.of("type", "You should read this pricing"));
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 0, 20));
    }

}