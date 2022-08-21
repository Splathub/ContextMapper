
import identity.entity.RootIdentityContentHandler;
import identity.utils.IdentityParser;
import identityMaster.RootIdentityBuilder;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
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

    public String buildModelTransformer(String model) {


        //TODO: pre made here, wrong palce below
        RootIdentityBuilder rib = new RootIdentityBuilder(master);
        rib.buildRootIdentityMap();
        //serialize map
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
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);
        } catch (TikaException | SAXException e) {
            LOG.error("Tika XML process: " + e.getMessage());
        }

        return handler.toString();
    }

    public static String processTo(ContentHandler handler, File file) throws IOException {
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(file) ){
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);
        } catch (TikaException | SAXException e) {
            LOG.error("Tika XML process: " + e.getMessage());
        }

        return handler.toString();
    }

}
