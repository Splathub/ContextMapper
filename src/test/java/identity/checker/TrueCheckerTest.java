package identity.checker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrueCheckerTest extends CheckerTest{

    @Test
    void shouldReturnTrueOnSample2() {
        assertTrue(new TrueChecker(emptyData).check(TEST_SAMPLE_2, root));
    }

    @Test
    void shouldReturnTrueOnSample1() {
        assertTrue(new TrueChecker(emptyData).check(TEST_SAMPLE_1, root));
    }
}