package identity.utils;

import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

public class LineNumberContentHandler extends ExpandedTitleContentHandler {
    private final TransformerHandler handler ;
    public LineNumberContentHandler(TransformerHandler transformerHandler) {
        super(transformerHandler);
        this.handler = transformerHandler;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            this.handler.characters(ch, start, length);
        } catch (SAXException var5) {
            this.handleException(var5);
        }
    }

}
