
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import identity.entity.Identity;

import identity.action.JoinIdentityAction;
import identity.exception.IdentityCrisisException;
import identity.utils.IdentityParser;
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

@Deprecated
public class XMLStyleCodeContentHandler extends ToXMLContentHandler {
    /*
    private final Logger LOG = LoggerFactory.getLogger(XMLStyleCodeContentHandler.class);

    private Identity[] identities;
    private final int identityWindow = 3;
    private int atIdentity=0;
    private int prevIdentity;
    private boolean defaultToXML;
    private boolean inBody;

    private boolean absorb = false;
    private JoinIdentityAction joinAction;

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

    public XMLStyleCodeContentHandler(OutputStream stream, String encoding, String identityFile, boolean defaultToXML) throws UnsupportedEncodingException {
        super(stream, encoding);
        this.defaultToXML = defaultToXML;
        createIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String encoding, String identityFile, boolean defaultToXML) {
        super(encoding);
        this.defaultToXML = defaultToXML;
        createIdentities(identityFile);
    }

    public XMLStyleCodeContentHandler(String identityFile, boolean defaultToXML) {
        super(null);
        this.defaultToXML = defaultToXML;
        createIdentities(identityFile);
    }


    private void createIdentities(String identityFile) {
        try {
            identities = IdentityParser.parse(identityFile);
        } catch (Exception e) {
            if (defaultToXML) {
                LOG.error("Failed to parse identityFile, defaulting to regular XML! " + e.getMessage());
                identities = new Identity[0];
            }
            else {
                LOG.error("Failed to parse identityFile! " + e.getMessage());
                throw new IdentityCrisisException("Failed to parse identityFile!");
            }
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        LOG.info(String.format("start Name; %s, Q: %s, Atts: %d", localName, qName, atts.getLength()));
        if (localName.equals("body") ) {
            inBody = true;
        }

        if (!absorb) {
            super.startElement(uri, localName, qName, atts);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        LOG.info(String.format("End Name; %s, Q: %s", localName, qName));
        if (!absorb) {
            super.endElement(uri, localName, qName);
        }
    }

    public void endDocument() throws SAXException {
        LOG.info("Ending Doc");
        if (absorb) { // TODO: may not end element properly
            super.write( joinAction.process() );
        }
        super.endDocument();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (absorb || inBody && super.inStartElement && identities.length > 0) {
            try {
                super.inStartElement = false;
                for (int i = atIdentity; i < atIdentity+identityWindow && i < identities.length; i++) {
                    LOG.info("On identitiy name: " + identities[i].getAttribute());
                    if (identities[i].check(ch, start, length)) {
                        LOG.info("true");
                        if (absorb) {
                            super.write( joinAction.process() + "</p> <p "); //TODO: Fix for last element handle!
                            absorb = false;
                        }
                        prevIdentity = atIdentity;
                        atIdentity = i;
                        break;
                    }
                }

                if (absorb) {
                    joinAction.append(ch, start, length);
                } else {
                    super.write(identities[atIdentity].getAttribute() +">");

                    switch (identities[atIdentity].getType()) {
                        case REPLACE:
                            super.write(identities[atIdentity].contextAdjustment(ch, start, length));
                            break;

                        case ABSORB:
                            absorb = true;
                            joinAction = (JoinIdentityAction) identities[atIdentity].getAction();
                            joinAction.append(ch, start, length);
                            break;

                        default:
                            super.characters(ch, start, length);
                    }
                }

            } catch (RuntimeException e) {
                LOG.error("Failed to identify context: " + e.getCause());
                e.printStackTrace();
                throw new SAXException("Identify Error: " + e.getMessage());
            }

        } else {
            super.characters(ch, start, length);
        }
    }
*/
}
