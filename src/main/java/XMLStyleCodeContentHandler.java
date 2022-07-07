
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import Identity.Identity;
//import Identity.IdentityParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class XMLStyleCodeContentHandler extends ToXMLContentHandler {
    private final Logger LOG = LoggerFactory.getLogger(XMLStyleCodeContentHandler.class);

    private Identity[] identities;
    private final int identityWindow = 3;
    private int atIdentity;
    private int prevIdentity;

    public XMLStyleCodeContentHandler(OutputStream stream, String encoding, String identityFile) throws UnsupportedEncodingException {
        super(stream, encoding);
        setIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String encoding, String identityFile) {
        super(encoding);
        setIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String identityFile) {
        super(null);
        setIdentities(identityFile);
    }

    private void setIdentities(String identityFile) {
        try {
            //identities = IdentityParser.parse(identityFile);
        }
        catch (Exception e) {
            LOG.error("Failed to parse identityFile, defaulting to regular XML!");
            identities = new Identity[0];
        }
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
