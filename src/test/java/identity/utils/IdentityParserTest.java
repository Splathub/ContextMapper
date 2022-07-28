package identity.utils;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class IdentityParserTest {

    @Test
    public void parsingIdentities() throws Exception {
        Identity[] identities = IdentityParser.parseIdentities(
                "src/test/resources/identities/sampleTest.yml");

        assertEquals(3, identities.length);
    }

    @Test
    public void parsingRootIdentity() throws Exception {
        RootIdentityContentHandler rootId = IdentityParser.parseRoot(
                "src/test/resources/identities/root/Sample1Docx.yml",
                "src/test/resources/identities/parts/");

        assertNotEquals(rootId, null);
    }

}
