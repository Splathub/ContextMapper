package identity.checker;

import identity.entity.RootIdentityContentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

public abstract class CheckerTest {
    Map<String, Object> emptyData;
    RootIdentityContentHandler root;
    public static  StringBuffer TEST_SAMPLE_1;
    public static StringBuffer TEST_SAMPLE_2;

    @BeforeEach
    public void initializeTestEnvironment(){
        emptyData = new HashMap<>();
        root  = new RootIdentityContentHandler("test", null, null);

        TEST_SAMPLE_1 = new StringBuffer("You should read this pricing supplement together with the accompanying prospectus, as supplemented by the accompanying\n" +
                "a prospectus supplement relating to our Series A medium-term notes, of which these notes are a part, and the more detailed\n" +
                "information contained in the accompanying product supplement. This pricing supplement, together with the documents\n" +
                "listed below, contains the terms of the notes and supersedes all other prior");

        TEST_SAMPLE_2 = new StringBuffer( "This pricing supplement should do stuff.") ;
    }

}
