package identity.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToggleCheckerTest extends CheckerTest {

    ToggleChecker toggleChecker;

    @BeforeEach
    public void initializeTestEnvironment(){
        toggleChecker = new ToggleChecker(emptyData);
    }
    @Test
    void shouldReturnTrueOnFirstCheck() {
        assertTrue(toggleChecker.check(TEST_SAMPLE_1, root));
    }

    @Test
    void shouldReturnFalseOnSecondCheck() {
        toggleChecker.check(TEST_SAMPLE_1, root);
        assertFalse(toggleChecker.check(TEST_SAMPLE_1, root));
    }
}