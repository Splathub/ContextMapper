package Identity;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.assertEquals;

class IdentityParserTest {
    @Test
    public void parsingSample1ShouldHaveCorrectLength() throws Exception {
        ArrayList<Identity> identities = IdentityParser.parse(
                "src/main/resources/samples/identities/sample1.yml");

        assertEquals(3, identities.size());
    }

    @Test
    public void parsingSample1FirstElementShouldBeCorrect() throws Exception {
        ArrayList<Identity> identities = IdentityParser.parse(
                "src/main/resources/samples/identities/sample1.yml");

        assertEquals(identities.get(0).toString(), "text");
    }

}