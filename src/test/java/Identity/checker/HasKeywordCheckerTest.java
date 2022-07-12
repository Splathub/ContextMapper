package Identity.checker;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HasKeywordCheckerTest {

    public static  char[] TEST_SAMPLE_1 = ("You should read this pricing supplement together with the accompanying prospectus, as supplemented by the accompanying\n" +
            "a prospectus supplement relating to our Series A medium-term notes, of which these notes are a part, and the more detailed\n" +
            "information contained in the accompanying product supplement. This pricing supplement, together with the documents\n" +
            "listed below, contains the terms of the notes and supersedes all other prior").toCharArray();

    Map<String, Object> data = new HashMap<>();


    @Test
    public void shouldDetectWordWhenPresentInContext(){
        data.put("keyword", "should read this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 0, 50));
    }

    @Test
    public void shouldReturnFalseIfWordIsNotPresent(){
        data.put("keyword", "You should read this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertFalse(identityChecker.check(TEST_SAMPLE_1, 7, 25));
    }

    @Test
    public void shouldDetectKeyWordAtStart(){
        data.put("keyword", "You should read this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 0, 50));
    }

    @Test
    public void shouldDetectKeyWordAtEnd(){
        data.put("keyword", "all other prior");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, 23, TEST_SAMPLE_1.length-1));
    }

}
