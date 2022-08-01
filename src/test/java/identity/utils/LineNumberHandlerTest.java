package identity.utils;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ContentHandler;

import static org.junit.jupiter.api.Assertions.*;

public class LineNumberHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(LineNumberHandlerTest.class);


    @Test
    public void characters() throws TransformerConfigurationException {
        CustomWriter writer  = new CustomWriter();

        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();
        TransformerHandlerWrapper wrapper = new TransformerHandlerWrapper(writer);

        File file = new File("src/test/resources/samples/sample2.pdf");

        try ( FileInputStream inputStream = new FileInputStream(file) ){

            parser.parse(inputStream, wrapper.getTransformerHandler(), metadata, context);

            System.out.println(writer.toString());
            //getting the content of the document
//            System.out.println("Contents of the PDF :" + wrapper.toString());

            //getting metadata of the document
            //System.out.println("Metadata of the PDF:");

        } catch (TikaException | SAXException | IOException e) {
            LOG.error("Tika process: " + e.getMessage());
            e.printStackTrace();
        }

    }
}