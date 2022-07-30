package identity.checker;

import identity.entity.RootIdentityContentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HasKeywordCheckerTest {

    Map<String, Object> data;
    RootIdentityContentHandler root;
    public static  StringBuffer TEST_SAMPLE_1;

    @BeforeEach
    public void initializeTestEnvironment(){
        data = new HashMap<>();
        root  = new RootIdentityContentHandler("test", null, null);
        TEST_SAMPLE_1 = new StringBuffer("You should read this pricing supplement together with the accompanying prospectus, as supplemented by the accompanying\n" +
                "a prospectus supplement relating to our Series A medium-term notes, of which these notes are a part, and the more detailed\n" +
                "information contained in the accompanying product supplement. This pricing supplement, together with the documents\n" +
                "listed below, contains the terms of the notes and supersedes all other prior");
    }

    @Test
    public void shouldDetectWordWhenPresentInContext(){
        data.put("keyword", "should read this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, null));
    }

    @Test
    public void shouldReturnFalseIfWordIsNotPresent(){
        data.put("keyword", "You should erase this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertFalse(identityChecker.check(TEST_SAMPLE_1, root));
    }

    @Test
    public void shouldDetectKeyWordAtStart(){
        data.put("keyword", "You should read this pricing");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, root));
    }

    @Test
    public void shouldDetectKeyWordAtEnd(){
        data.put("keyword", "all other prior");
        IdentityChecker identityChecker = new HasKeywordChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1, root));
    }

}
