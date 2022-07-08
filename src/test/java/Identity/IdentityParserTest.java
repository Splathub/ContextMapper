package Identity;

import Identity.utils.IdentityParser;
import org.junit.jupiter.api.Test;

import static org.testng.AssertJUnit.assertEquals;

class IdentityParserTest {

    @Test
    public void parsingSample1ShouldHaveCorrectLength() throws Exception {
        Identity[] identities = IdentityParser.parse(
                "src/test/resources/identities/sample1.yml");

        assertEquals(3, identities.length);
    }

    @Test
    public void parsingSample1FirstElementShouldBeCorrect() throws Exception {
        Identity[] identities = IdentityParser.parse(
                "src/test/resources/identities/sample1.yml");

        assertEquals(identities[0].toString(), "text");
    }

}