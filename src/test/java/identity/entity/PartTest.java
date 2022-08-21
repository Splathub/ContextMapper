package identity.entity;

import identity.action.BaseIdentityAction;
import identity.checker.ToggleChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

class PartTest {

    RootIdentityContentHandler root = new RootIdentityContentHandler("test", null, null);
    StringBuffer sb = new StringBuffer();
    CheckedIdentity[] identities;

    @BeforeEach
    void setUp() {
        identities = new CheckedIdentity[5];
        for(int i=0; i<5; i++) {
            identities[i] = new CheckedIdentity(
                    new ToggleChecker(null),
                    new BaseIdentityAction(),
                    "<P STYLE=\"font: 9pt Arial, Helvetica, Sans-Serif; margin: 4pt 0 0; color: #404040\">%s</P>",
                    null);
        }
    }

    @Test
    void directPushPoint() {
        Part part = new Part(identities);
        part.pushPoint(4);
        assertEquals(4, part.getOnPoint(), "Part direct call of pushPoint failed!");
    }

    @Test
    void directAdjustedRange() {
        Part part = new Part(identities);
        part.adjustedRange(4);
        assertEquals(4, part.getRange(), "Part direct positive call of adjustedRange failed!");
        part.adjustedRange(-1);
        assertEquals(1, part.getRange(), "Part direct negative call of adjustedRange failed!");
    }

    @Test
    void directIncludedProcess() throws SAXException {
        Part part = new Part(identities);
        part.includedProcess(3, sb, root);
        assertEquals(3, part.getOnPoint(), "Part identity positive call of include onPoint failed!");
        part.includedProcess(-2, sb, root);
        assertEquals(1, part.getOnPoint(), "Part identity negative call of include onPoint failed!");
    }

    @Test
    void includedProcessOFR() throws SAXException {
        Part part = new Part(identities);
        part.includedProcess(100, sb, root);    // takes max
        assertEquals(4, part.getOnPoint(), "Part identity OFR positive call of include onPoint failed!");
        part.includedProcess(-100, sb, root);     // takes min
        assertEquals(0, part.getOnPoint(), "Part identity OFR negative call of include onPoint failed!");
    }

}
