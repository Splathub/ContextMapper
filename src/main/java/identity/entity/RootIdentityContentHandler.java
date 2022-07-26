package identity.entity;

import identity.action.IdentityAction;
import identity.checker.IdentityChecker;
import identity.utils.IdentityParser;
import javafx.util.Pair;
import org.apache.tika.sax.ToTextContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RootIdentityContentHandler extends ToTextContentHandler {
    private final Logger LOG = LoggerFactory.getLogger(RootIdentityContentHandler.class);

    private String name;
    //private String globalStyle;
    private IdentityAction defaultAction;
    private final char[] ends = new char[]{'\n'};
    private final int window = 3;
    private Pair<IdentityChecker, String>[] parts;
    private final String partsPath;
    private Part part;
    private int onPoint = 0;

    private final List passiveTag = Arrays.asList("b", "i", "u");
    private StringBuffer sbText = new StringBuffer();
    private StringBuffer sbOriginal = new StringBuffer();
    private boolean mergeElements;
    private String tag;
    private int level = 0;

    private boolean inBody;

    public RootIdentityContentHandler(String name, String partsPath, Pair<IdentityChecker, String>[] parts) {
        this.name = name;
        //this.defaultAction = defaultAction;
        this.parts = parts;
        this.partsPath = partsPath + name + "/";
    }

    /**
    * Constructs as HTML with pre-defined head, no value, could eb moved to the Root file to define.
     */
    public void startDocument() throws SAXException
    {
        this.write("<HTML>\n<HEAD>\n<TITLE></TITLE>\n</HEAD>\n<BODY>\n");
        /*

        if (this.encoding != null) {
            this.write("<?xml version=\"1.0\" encoding=\"");
            this.write(this.encoding);
            this.write("\"?>\n");
        }
         */
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (inBody) {
            if (tag == null) {
                tag = localName;
                sbText = new StringBuffer();
                sbOriginal = new StringBuffer();
            } else if (passiveTag.contains(localName)) {
                sbOriginal.append("<").append(localName).append(">");
            } else {
                level++;
            }
        } else if (localName.equals("body")) {
            inBody = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(inBody) {
            if (tag.equals(localName) && level == 0) {
                identify();
                tag = null;
            } else if (passiveTag.contains(localName)) {
                sbOriginal.append("</").append(localName).append(">");
            } else {
                level--;
            }
        }
    }

    public void endDocument() throws SAXException {
        this.write("\n</BODY>\n</HTML>");
    }

    public void write(String str) throws SAXException {
        super.characters(str.toCharArray(), 0, str.length());
        super.characters(ends, 0, 1);

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        sbOriginal.append(ch, start, length);
        sbText.append(ch, start, length);
    }

    private void identify() throws SAXException {
        for(int i=onPoint; i < parts.length && i < onPoint + window; i++) {
            if (parts[i].getKey().check(sbText, this) ) {
                if (i != onPoint || part == null) {
                    if (part != null) {
                        part.finalProcess(this);
                    }

                    onPoint = i;
                    String path = partsPath + parts[onPoint].getValue() + ".yml";
                    try {
                        part = new Part(IdentityParser.parseIdentities(path));
                    } catch (IOException e) { //TODO: May not stop process
                        LOG.error("Part location not found, or bad parse: " + path);
                        throw new SAXException("Part invalid: " + path);
                    }
                }
                break;
            }
        }
        part.process(sbOriginal, this);
    }

    public String getTag() {
        return tag;
    }

    public void setMergeElements(boolean mergeElements) {
        this.mergeElements = mergeElements;
    }

}
