package identity.checker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

class TagCheckerTest extends CheckerTest {

    TagChecker tagChecker;


    @Test
    void shouldReturnFalseIfIsNotInBodyButTagMatches() throws SAXException {
        root.startElement(null, "body", null, null);

        emptyData.put("tag", "tag-2");
        root.startElement(null, "tag-1", null, null);
        assertFalse(new TagChecker(emptyData).check(TEST_SAMPLE_1, root));
    }

    @Test
    void shouldReturnFalseIfIsInBodyAndTagDoesNotMatches() throws SAXException {
        root.startElement(null, "body", null, null);

        emptyData.put("tag", "tag-2");
        root.startElement(null, "tag-1", null, null);
        assertFalse(new TagChecker(emptyData).check(TEST_SAMPLE_1, root));
    }

    @Test
    void shouldReturnTrueIfIsInBodyAndTagMatches() throws SAXException {
        root.startElement(null, "body", null, null);

        emptyData.put("tag", "tag-1");
        root.startElement(null, "tag-1", null, null);
        assertTrue(new TagChecker(emptyData).check(TEST_SAMPLE_1, root));
    }
}