
import identity.entity.RootIdentityContentHandler;
import identity.utils.IdentityParser;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ContextMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ContextMapper.class);

    File pdf;
    String identity;
    String partsPath = "src/main/resources/identity/parts/";


    public ContextMapper(String pdfPath, String identityPath) {
        pdf = new File(pdfPath);
       identity = identityPath;
    }

    public void setPartPath(String path) {
        partsPath = path;
    }

    public String process() throws IOException {

        RootIdentityContentHandler handler = IdentityParser.parseRoot(identity, partsPath);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(pdf) ){
            //parsing the document using PDF parser
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);

            //getting the content of the document
            //System.out.println("Contents of the PDF :" + handler.toString());

            //getting metadata of the document
            //System.out.println("Metadata of the PDF:");

        } catch (TikaException | SAXException e) {
            LOG.error("Tika process: " + e.getMessage());
            e.printStackTrace();
        }

        return handler.toString();
    }

    public String processToXML() throws IOException {

        ToXMLContentHandler handler = new ToXMLContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(pdf) ){
            //parsing the document using PDF parser
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);

            //getting the content of the document
            //System.out.println("Contents of the PDF :" + handler.toString());

            //getting metadata of the document
            //System.out.println("Metadata of the PDF:");

        } catch (TikaException | SAXException e) {
            LOG.error("Tika XML process: " + e.getMessage());
        }

        return handler.toString();
    }

    private void preProcess(){

    }

    private void postProcess() {

    }
}
