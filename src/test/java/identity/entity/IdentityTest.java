package identity.entity;

import identity.action.BaseIdentityAction;
import identity.checker.FalseChecker;
import identity.checker.ToggleChecker;
import identity.checker.TrueChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

class IdentityTest {

    RootIdentityContentHandler root = new RootIdentityContentHandler("test", null, null);
    StringBuffer sb = new StringBuffer();
    Identity[] identities;

    @BeforeEach
    void setUp() {
        identities = new Identity[5];
        identities[0] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                null,
                0,0, Part.getDefaultWindow(),"");

        identities[1] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                null,
                0,0,3,"");

        identities[2] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                null,
                0,0,0,"");

        identities[3] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                null,
                0,0,0,"");

        identities[4] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                null,
                0,0,0,"");
    }

    @Test
    void identityPushPoint() throws SAXException {
        identities[0] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                4,0,0,"");
        identities[4] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                -2,0,0,"");
        Part part = new Part(identities);

        part.process(sb, root);
        assertEquals(4, part.getOnPoint(), "Part identity positive call of pushPoint failed!");
        part.process(sb, root);
        assertEquals(2, part.getOnPoint(), "Part identity negative call of pushPoint failed!");
    }

    @Test
    void identityAdjustedRange() throws SAXException {
        identities[0] = new Identity(
                new TrueChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                1,0,5,"");
        identities[1] = new Identity(
                new FalseChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,0,0,"");
        identities[2] = new Identity(
                new FalseChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,0,0,"");
        identities[3] = new Identity(
                new FalseChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,0,0,"");
        identities[4] = new Identity(
                new ToggleChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,0,-4,"");
        Part part = new Part(identities);
        part.process(sb, root); // Search process true on 0, process range for 5
        assertEquals(5, part.getRange(), "Part identity positive call of adjustedRange failed!");
        part.process(sb, root); // Search process true on 4, process range for -4 (4), move point to 0
        assertEquals(4, part.getRange(), "Part identity negative call of adjustedRange failed!");
        assertEquals(0, part.getOnPoint(), "Part identity negative call of adjustedRange onPoint failed!");
        part.process(sb, root); // Search process true on 0, process range for -4 (4), move point to 0
        assertEquals(0, part.getOnPoint(), "Part identity negative call of adjustedRange onPoint failed!");
    }

    @Test
    void identityIncludedProcess() throws SAXException {
        identities[0] = new Identity(
                new TrueChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,3,Part.getDefaultWindow(),"");
        identities[3] = new Identity(
                new FalseChecker(null),
                new BaseIdentityAction(),
                "Jump Away",
                null,
                0,-1,Part.getDefaultWindow(),"");
        Part part = new Part(identities);
        part.process(sb, root);
        assertEquals(2, part.getOnPoint(), "Part identity positive, negative call of include failed!");
    }

}
