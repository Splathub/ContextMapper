package identity.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.Stack;

public class GeneralContentHandler extends DefaultHandler {
    private final Logger LOG = LoggerFactory.getLogger(GeneralContentHandler.class);
    private final char[] ENDS = new char[]{'\n'};
    private boolean inBody = false;

    private Stack<Identity> identityStack = new Stack<>();
    private Identity currentIdentity;
    private final TextToAction toa;
    private StringBuilder sb;


    public GeneralContentHandler(TextToAction toa) {
        this.toa = toa;
    }

    @Override
    public void startDocument() throws SAXException {
        this.write("<HTML>\n<HEAD>\n<TITLE> </TITLE>\n</HEAD>\n<BODY>\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        if (inBody) {
            if (currentIdentity != null) {
                currentIdentity.process(sb, this);
                identityStack.push(currentIdentity);
                currentIdentity = null;
            }

            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("body")) {
            inBody = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (inBody) {
            if (currentIdentity == null) {
                currentIdentity = toa.identify(sb); //TODO: may move to handle return of set and select for proxy
            }
            currentIdentity.finalProcess(sb, this);

            if (!identityStack.isEmpty()) {
                currentIdentity = identityStack.pop();
            }
            else {
                currentIdentity = null;
            }
            sb = new StringBuilder();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        if (inBody) {
            if (sb != null) {
                //processText(); TODO: close all exisiting identitys
            }
            this.write("\n</BODY>\n</HTML>");
        }
    }

    public void write(String str) throws SAXException {
        super.characters(str.toCharArray(), 0, str.length());
        super.characters(ENDS, 0, 1);
    }

    public void characters(char[] ch, int start, int length) {
        sb.append(ch, start, length);
    }

    private void processText() {
        currentIdentity = toa.identify(sb);
        //currentIdentity.process(...);
    }


}
