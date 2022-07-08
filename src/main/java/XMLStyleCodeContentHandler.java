
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import Identity.Identity;

import Identity.utils.IdentityParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <p>This class tags content based on the context in elements as XML. A tag is decided from a matching Identity that gives
 * a TRUE check. The tag will contain the attributes id (Identity id), and sometimes style (Identity Action Class).
 * The id attribute is used to for matching XSLT tags and the style attribute if there means a Java method to use when
 * stylizing during transformation.</p>
 *
 * <p>An Identity is selected by testing for a TRUE check in-order in a window range. If a TRUE check is found in the
 * range, that becomes the new starting point for the next checks. If no check returns TRUE, the last used Identity is
 * used.</p>
 *
 * <p>When using an Identity, the IdentityActionType is checked. If NONE, write the char[] as-is, REPLACE is to use the
 * Identity's contextAdjustment's String instead, and ABSORB is to append all following char[] till the next TRUE
 * Identity check or end of document.</p>
 **/
public class XMLStyleCodeContentHandler extends ToXMLContentHandler {
    private final Logger LOG = LoggerFactory.getLogger(XMLStyleCodeContentHandler.class);

    private Identity[] identities;
    private final int identityWindow = 3;
    private int atIdentity;
    private int prevIdentity;

    private boolean absorb = false;
    private StringBuffer sb;

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

    public void endDocument() throws SAXException {
        if (absorb) { // TODO: may not end element properly
            super.write(identities[atIdentity].contextAdjustment(sb.toString().toCharArray(), 0, sb.length()-1));
        }
        super.endDocument();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (super.inStartElement && identities.length > 0) {
            try {
                super.inStartElement = false;
                String atts = null;

                for (int i = atIdentity; i < identityWindow && i < identities.length; i++) {
                    atts = identities[i].checkedProcess(ch, start, length);
                    if (atts != null) {
                        if(absorb) {
                            super.write(identities[atIdentity].contextAdjustment(sb.toString().toCharArray(), 0, sb.length()-1));
                            absorb = false;
                        }
                        prevIdentity = atIdentity;
                        atIdentity = i;
                        break;
                    }
                }

                if(absorb) {
                    sb.append(ch);
                }
                else {
                    if (atts == null) {
                        atts = identities[atIdentity].getAttribute();
                    }
                    super.write(atts);

                    switch (identities[atIdentity].getType()) {
                        case REPLACE:
                            super.write(identities[atIdentity].contextAdjustment(ch, start, length);
                            break;

                        case ABSORB:
                            absorb = true;
                            sb = new StringBuffer();
                            break;

                        default:
                            super.characters(ch, start, length);
                    }
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
