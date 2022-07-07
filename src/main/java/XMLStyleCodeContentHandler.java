
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import Identity.Identity;

import Identity.utils.IdentityParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLStyleCodeContentHandler extends ToXMLContentHandler {
    private final Logger LOG = LoggerFactory.getLogger(XMLStyleCodeContentHandler.class);

    private Identity[] identities;
    private final int identityWindow = 3;
    private int atIdentity;
    private int prevIdentity;
    private boolean absorb = false;

    public XMLStyleCodeContentHandler(OutputStream stream, String encoding, String identityFile) throws UnsupportedEncodingException {
        super(stream, encoding);
        createIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String encoding, String identityFile) {
        super(encoding);
        createIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String identityFile) {
        super(null);
        createIdentities(identityFile);
    }

    private void createIdentities(String identityFile) {
        try {
            identities = IdentityParser.parse(identityFile);
        }
        catch (Exception e) {
            LOG.error("Failed to parse identityFile, defaulting to regular XML!");
            identities = new Identity[0];
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
       if(!absorb) { super.startElement(uri, localName, qName, atts); }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(!absorb) { super.endElement(uri, localName, qName); }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (super.inStartElement && identities.length > 0) {
            try {
                super.inStartElement = false;
                String atts = null;

                for (int i = atIdentity; i < identityWindow && i < identities.length; i++) {
                    atts = identities[i].checkedProcess(ch, start, length);
                    if (atts != null) {
                        prevIdentity = atIdentity;
                        atIdentity = i;
                        break;
                    }
                }

                if (atts == null) {
                    atts = identities[atIdentity].getAttribute();
                }

                super.write(atts);

                String replacement = identities[atIdentity].contextAdjustment(ch, start, length);
                if (replacement != null) {
                    super.write(replacement);
                } else {
                    super.characters(ch, start, length);
                }

            }
            catch (RuntimeException e) {
                LOG.error("Failed to identify context: " + e.getMessage());
                throw new SAXException("Identify Error: " + e.getMessage());
            }

        } else {
            super.characters(ch, start, length);
        }
    }

}
