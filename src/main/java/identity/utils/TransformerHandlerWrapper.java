package identity.utils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.Writer;
import java.net.ContentHandler;

public class TransformerHandlerWrapper {
    SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    private final TransformerHandler transformerHandler;
    private final Writer writer;

    public TransformerHandlerWrapper(Writer writer) throws TransformerConfigurationException {
        this.writer = writer;
        transformerHandler = factory.newTransformerHandler();

        transformerHandler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        transformerHandler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        transformerHandler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformerHandler.setResult(new StreamResult(this.writer));
    }

    public LineNumberContentHandler getTransformerHandler() {
        return new LineNumberContentHandler( transformerHandler ) ;
    }

    @Override
    public String toString() {
        return this.writer.toString();
    }
}
