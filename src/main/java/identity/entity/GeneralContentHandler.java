package identity.entity;

import constants.Constants;
import identity.action.CSVProcessAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class GeneralContentHandler extends DefaultHandler {
    private final Logger LOG = LoggerFactory.getLogger(GeneralContentHandler.class);
    private final char[] ENDS = new char[]{'\n'};
    private boolean inBody = false;
    private CSVProcessAction csvProcessAction = null;    // Set by ACtion and prevents additional stacking
    private LinkedList<LinkedList<String>> csv;
    private LinkedList<String> row;
    private StringBuilder delayedString;
    private String currentTag = null;

    private LinkedList<Identity> identityStack = new LinkedList<>();
    private Identity currentIdentity;
    private final TextToAction tta;
    private StringBuilder sb;

    private final Writer writer;


    public GeneralContentHandler(TextToAction tta, Writer writer) {
        this.tta = tta;
        this.writer = writer;
    }

    @Override
    public void startDocument() {
        LOG.info("Started document writing");
        this.write("<HTML>\n<HEAD>\n<TITLE> </TITLE>\n</HEAD>\n<BODY>\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        if (inBody) {
            currentTag = localName;

            if (csvProcessAction != null) { // extra logic to ensure closing tags for csv, no sub tr, and td
                if (localName.equalsIgnoreCase("tr") && row != null) {
                    csv.add(row);
                    row = new LinkedList<>();
                }
                else if (localName.equalsIgnoreCase("td") && delayedString.length() > 0) {
                    row.add(delayedString.toString());
                    delayedString = new StringBuilder();
                }
            }

            if (currentIdentity != null) {  // pending text
                currentIdentity.process(sb, this);
                identityStack.push(currentIdentity);    // push for end tag. allow for sub element
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
            currentTag = localName;

            if (currentIdentity == null) {
                try {
                    currentIdentity = tta.identify(sb); //TODO: may move to handle return of set and select for proxy
                    appendParents(currentIdentity);
                } catch (CloneNotSupportedException e) {
                    throw new SAXException("Identity clone failed");
                }

                if (currentTag.equalsIgnoreCase("table")) {
                    if (currentIdentity.getAction().equals(csvProcessAction)) {
                        delayedString = null;
                        csvProcessAction.processCSV(csv, this);
                        csvProcessAction = null;
                        csv = null;
                        row = null;
                    }
                    else if (currentIdentity.getAction() instanceof CSVProcessAction) {
                        if (csvProcessAction != null) {
                            LOG.error("Sub table not setup and not allowed ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                            throw new RuntimeException("Sub table not setup and not allowed");
                        }
                        csvProcessAction = (CSVProcessAction) currentIdentity.getAction();
                        csv = new LinkedList<>();
                        row = new LinkedList<>();
                        delayedString = new StringBuilder();
                    }
                }
            }

            if (currentIdentity != null) {
                currentIdentity.finalProcess(sb, this);
            }

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
            currentTag = "body";
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

    private void appendParents(Identity identity) throws CloneNotSupportedException {
        Deque<Identity> parentStack = new ArrayDeque<>();
        Iterator<Identity> iterator = identityStack.descendingIterator();
        String key = (String) identity.getArgs(Constants.PARENT);
        Identity parent;

        while(iterator.hasNext() && key != null) {
            parent = tta.getIdentityByKey(key);
            if (!parent.equals(iterator.next())) {
                key = (String) identity.getArgs(Constants.PARENT);
                parentStack.push(parent);
            }
            else {
                break;
            }
        }

        for(Identity parentIdentity : parentStack) {
            parentIdentity.process(null, this); // Assume parents are empty writes since textKey targeted the given identity
            identityStack.push(parentIdentity);
        }
    }

    public void write(String str)  {
        if (delayedString != null) {
            delayedString.append(str);
        }
        else {
            try {
                writer.write(str.toCharArray(), 0, str.length());
                writer.write(ENDS, 0, 1);
            } catch (IOException e) {
                LOG.error("Failed to write to file :" + str);
            }
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

    public String getCurrentTag() {
        return currentTag;
    }

    public void setActiveSubListHandler(CSVProcessAction csvProcessAction) {
        this.csvProcessAction = csvProcessAction;
    }

    public String toString() {
        return writer.toString();
    }

}
