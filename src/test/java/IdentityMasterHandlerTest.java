import identityMaster.IdentityMasterContentHandler;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.stream.events.StartElement;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Deprecated
public class IdentityMasterHandlerTest {

    static private Metadata metadata = new Metadata();

    final static String SAMPLE_PDF = "src/test/resources/samples/sample18.PDF";
    final static String SAMPLE_DOCX = "src/test/resources/samples/sample18.docx";
    final static String SAMPLE_HTML= "src/test/resources/samples/sample18.htm";

    public static void main(String[] args) throws IOException {

        HashMap<String, Object> map = new HashMap<>();
        class ToTextContentHandler2 extends ToTextContentHandler {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                super.characters(">>".toCharArray(), 0, 2);
                super.startElement(uri, localName, qName, atts);
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);
                super.characters("<<\n".toCharArray(), 0, 3);
            }
        }


        System.out.println(ContextMapper.processTo(
              //  new XHTMLContentHandler(new BodyContentHandler(), metadata),
                //new ToHTMLContentHandler(),
                new ToXMLContentHandler(),
               // new ToTextContentHandler2(),
               // new ElementMappingContentHandler(new BodyContentHandler(), map),
                new File(SAMPLE_PDF)
        ));

        //IdentityMasterContentHandler imch = new IdentityMasterContentHandler(new File(SAMPLE_HTML), new File(SAMPLE_DOCX));
        //imch.start();
    }

}
