package identity.utils;

import identity.entity.Identity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CheckedIdentityFactoryTest {

    @Test
    void createIdentityJustMinimal() {
        Map<String, Object> data = new HashMap<>();
        data.put("template", "Some text");
        Identity checkedIdentity = IdentityFactory.createIdentity(data);

        assertNotNull(checkedIdentity);
    }

    @Test
    void createIdentityNull() {
        Identity checkedIdentity = IdentityFactory.createIdentity(null);
        assertNotNull(checkedIdentity);
    }

    @Test
    void getInstance() {
        IdentityFactory idFac1 = IdentityFactory.getInstance();
        IdentityFactory idFac2 = IdentityFactory.getInstance();
        assertEquals(idFac1, idFac2);
    }

    @Test
    void findAllClassesUsingClassLoader() {
        IdentityFactory factory = IdentityFactory.getInstance();
        Set<Class> checkers = factory.findAllClassesUsingClassLoader("identity.checker");
        assertEquals(checkers.size(), 3);
    }

}
