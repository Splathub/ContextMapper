package Identity.checker;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MatchingTextCheckerTest {

    public static String TEST_SAMPLE_1 = "This pricing supplement";
    Map<String, Object> data = new HashMap<>();

    @Test
    public void shouldMatchContext(){
        data.put("text", "This pricing supplement");
        IdentityChecker identityChecker = new MatchingTextChecker(data);
        assertTrue(identityChecker.check(TEST_SAMPLE_1), TEST_SAMPLE_1);
    }

    @Test
    public void shouldNotMatchContext(){
        data.put("text", "This pricing supplement");
        IdentityChecker identityChecker = new MatchingTextChecker(data);
        assertFalse(identityChecker.check(TEST_SAMPLE_1), TEST_SAMPLE_1 + "234");
    }

}
