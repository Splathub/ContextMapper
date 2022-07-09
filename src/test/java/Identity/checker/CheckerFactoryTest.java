package Identity.checker;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CheckerFactoryTest {

    @Test
    public void shouldCreateMatchingTestChecker() {
        IdentityChecker checker = CheckerFactory.build(Map.of(
                "type","MATCHING-TEXT",
                "data","nor relva"
        ));

        assertNotNull(checker);
    }

    @Test
    public void shouldThrowAnErrorIfTheCheckerDoesNotExist() {
        assertThrows(RuntimeException.class, () -> {
            IdentityChecker checker = CheckerFactory.build(Map.of(
                    "type","CHECKER-THAT-DOES-NOT-EXIST",
                    "data","nor relva"
            ));
        });
    }

    @Test
    public void checkersShouldBeOfTheProperInstance(){
        IdentityChecker checker = CheckerFactory.build(Map.of(
                "type","HASH-KEY",
                "data","nor relva"
        ));

        assertEquals(HashKeywordChecker.class, checker.getClass());
    }
}