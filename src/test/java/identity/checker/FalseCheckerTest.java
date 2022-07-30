package identity.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FalseCheckerTest extends CheckerTest {

    FalseChecker falseChecker;

    @BeforeEach
    public void initializeTestEnvironment(){
        falseChecker = new FalseChecker(emptyData);
    }

    @Test
    void check() {
        assertFalse(falseChecker.check(TEST_SAMPLE_1, root));
    }
}