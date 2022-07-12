package Identity.utils;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdentityFactoryTest {

    @Test
    void getInstance() {
        IdentityFactory idFac1 = IdentityFactory.getInstance();
        IdentityFactory idFac2 = IdentityFactory.getInstance();
        assertEquals(idFac1, idFac2);
    }

    @Test
    void findAllClassesUsingClassLoader() {
        IdentityFactory factory = IdentityFactory.getInstance();
        Set<Class> checkers = factory.findAllClassesUsingClassLoader("Identity.checker");
        assertEquals(checkers.size(), 3);
    }
}