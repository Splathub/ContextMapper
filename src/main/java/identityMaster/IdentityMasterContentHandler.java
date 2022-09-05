package identityMaster;

import identityMaster.entity.Element;
import identityMaster.entity.IdentityMaster;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Deprecated
public class IdentityMasterContentHandler {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityMasterContentHandler.class);

    private IdentityMaster identityMaster;
    private File html;
    private File other;

    // 0=HTML, 1=Other
    private volatile StringBuffer[] text = new StringBuffer[2];
    private volatile Element element;
    private volatile int[] statuses = new int[2];
    // 0 working, 1 done/waiting, -1 ended/issue
    private boolean hasSplit;
    private int split;

    public IdentityMasterContentHandler(File html, File other) {
        this.html = html;
        this.other = other;
    }

    private class WrappedHTMLHandler extends ToHTMLContentHandler {
        private final int ID = 0;
        IdentityMasterContentHandler master;

        public WrappedHTMLHandler(IdentityMasterContentHandler master, ContentHandler handler, Metadata metadata) {

            this.master = master;
        }

        @Override
        public void startElement(String uri, String local, String name, Attributes attributes) throws SAXException {
            text[ID] = new StringBuffer();
            element = new Element();
            element.setTag(local);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text[ID].append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String local, String name) throws SAXException {
           // element.setText(text.toString());
            statuses[ID] = 1;
            while(statuses[ID] == 1) {
                // wait(150L);
            }

            if (statuses[ID] == -1) {
                throw new RuntimeException("End Thread, -1 "+ID);
            }

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            LOG.info("End of thread:"+ID);
        }
    }

    private class WrappedOtherHandler extends XHTMLContentHandler {
        private final int ID = 1;
        IdentityMasterContentHandler master;

        public WrappedOtherHandler(IdentityMasterContentHandler master, ContentHandler handler, Metadata metadata) {
            super(handler, metadata);
            this.master = master;
        }

        @Override
        public void startElement(String uri, String local, String name, Attributes attributes) throws SAXException {
            text[ID] = new StringBuffer();
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text[ID].append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String local, String name) throws SAXException {
            statuses[ID] = 1;
            while(statuses[ID] == 1) {
                // wait(150L);
            }

            if (statuses[ID] == -1) {
                throw new RuntimeException("End Thread, -1 "+ID);
            }

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            LOG.info("End of thread:"+ID);
        }
    }


    public void start() {
        Runnable htmlThreadRunner = () -> {

            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            WrappedHTMLHandler handler = new WrappedHTMLHandler(this, new BodyContentHandler(), metadata);

            try ( FileInputStream inputStream = new FileInputStream(html) ){
                HtmlParser parser = new HtmlParser();
                parser.parse(inputStream, handler, metadata, context);
            } catch (TikaException | SAXException | FileNotFoundException e) {
                LOG.error("Tika IdentityMaster htmlThread process: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable otherThreadRunner = () -> {

            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            WrappedOtherHandler handler = new WrappedOtherHandler(this, new BodyContentHandler(), metadata);

            try ( FileInputStream inputStream = new FileInputStream(other) ){
                AutoDetectParser parser = new AutoDetectParser();
                parser.parse(inputStream, handler, metadata, context);
            } catch (TikaException | SAXException | FileNotFoundException e) {
                LOG.error("Tika IdentityMaster otherThread process: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        ThreadGroup tg = new ThreadGroup("handles");
        Thread htmlThread = new Thread(tg, htmlThreadRunner,"htmlThreadedHandler");
        Thread otherThread = new Thread(tg, otherThreadRunner,"otherThreadedHandler");

        htmlThread.start();
        otherThread.start();

        asyncProcess(tg, htmlThread, otherThread);
    }

    private void asyncProcess(ThreadGroup tg, Thread htmlThread, Thread otherThread) {
        try {
            int i =0;
            while(tg.activeCount() == 2) {
                while (!(statuses[0] == 1 && statuses[1] == 1)) {
                    //wait(100L);
                    if (tg.activeCount() != 2) {
                        throw new RuntimeException("Invalid element match");
                    }
                }

                matchElements();
                statuses[0] = 0;
                statuses[1] = 0;
            }
        } finally {
            statuses[0] = -1;
            statuses[1] = -1;
            if (!tg.isDestroyed()) {
                tg.destroy();
            }
        }
    }

    private void matchElements() {

        while(!validElement(0)) { statuses[0] = 0; }
        while(!validElement(1)) { statuses[1] = 0; }

        if (text[0].equals(text[1])) {
            LOG.info("Matched text from Elements!" + "'" + text[0] + "'");
        }
        else {
            LOG.info("Not a Matching text from Elements!" + text[0] + " \nvs \n" + text[1]);
        }
    }

    private boolean validElement(int id) {
        if (text[id].length() == 0) {
            return false;
        }
        return true;
    }

}
