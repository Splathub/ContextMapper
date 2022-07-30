package identity.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MatchingTextCheckerTest extends CheckerTest {

    private MatchingTextChecker newMatchingTextChecker(String str){
        emptyData.put("text", str);
        return new MatchingTextChecker(emptyData);
    }
    @Test
    public void shouldMatchContextWithExactContent(){
        assertTrue(newMatchingTextChecker(TEST_SAMPLE_1.toString()).check(TEST_SAMPLE_1, root));
    }

    @Test
    public void shouldNotMatchContextWithDifferentContent(){
        assertFalse(newMatchingTextChecker("This pricing supplement")
                      .check(TEST_SAMPLE_1.append("additional-content"), root));
    }

}
