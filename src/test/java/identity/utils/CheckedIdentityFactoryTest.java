package identity.utils;

import identity.entity.CheckedIdentity;
import identity.entity.RootIdentityContentHandler;
import identity.exception.IdentityCrisisException;
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
        CheckedIdentity checkedIdentity = IdentityFactory.createIdentity(data);

        assertNotNull(checkedIdentity);
    }

    @Test
    void createIdentityNull() {
        CheckedIdentity checkedIdentity = IdentityFactory.createIdentity(null);
        assertNotNull(checkedIdentity);
    }

    @Test
    void createRootIdentityNull() {
        Map<String, Object> data = new HashMap<>();
        Exception exception = assertThrows(IdentityCrisisException.class, () -> {
            IdentityFactory.createRootIdentity(null, null);
        });
        assertTrue(exception.getMessage() != null);
    }

    @Test
    void createRootIdentityMinimal() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Some text");
        RootIdentityContentHandler root =
                IdentityFactory.createRootIdentity(data, "src/test/resources/identities/parts/");

        assertNotNull(root);
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
