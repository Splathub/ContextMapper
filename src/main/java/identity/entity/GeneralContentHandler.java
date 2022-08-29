package identity.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

public class GeneralContentHandler extends DefaultHandler {
    private final Logger LOG = LoggerFactory.getLogger(GeneralContentHandler.class);
    private final char[] ENDS = new char[]{'\n'};
    private boolean inBody = false;

    private Stack<Identity> identityStack = new Stack<>();
    private Identity currentIdentity;
    private final TextToAction toa;
    private StringBuilder sb;

    private final Writer writer;


    public GeneralContentHandler(TextToAction toa, Writer writer) {
        this.toa = toa;
        this.writer = writer;
    }

    @Override
    public void startDocument() {
        this.write("<HTML>\n<HEAD>\n<TITLE> </TITLE>\n</HEAD>\n<BODY>\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
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
    public void endElement(String uri, String localName, String qName) throws SAXException {
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
    public void endDocument() {
        if (inBody) {
            if (sb != null && currentIdentity != null) {
                currentIdentity.finalProcess(sb, this);
                sb = new StringBuilder();
            }

            while (!identityStack.isEmpty()) {
                currentIdentity = identityStack.pop();
                currentIdentity.finalProcess(sb, this);
            }

            this.write("\n</BODY>\n</HTML>");
        }
    }

    public void write(String str)  {
        try {
            writer.write(str.toCharArray(), 0, str.length());
            writer.write(ENDS, 0, 1);
        }
        catch (IOException e) {
            LOG.error("Failed to write to file :" + str);
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (inBody && sb != null) {
            sb.append(ch, start, length);
        }
        else {
            //this.write(ch.toString());
        }
    }

    public String toString() {
        return writer.toString();
    }

}
