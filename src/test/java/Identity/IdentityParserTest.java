package Identity;

import Identity.utils.IdentityParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class IdentityParserTest {

    @Test
    public void parsingSample1ShouldHaveCorrectLength() throws Exception {
        Identity[] identities = IdentityParser.parse(
                "src/test/resources/identities/sample1.yml");

        assertEquals(3, identities.length);
    }

}
