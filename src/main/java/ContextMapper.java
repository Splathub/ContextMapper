
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
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


    public ContextMapper(String pdfPath, String identityPath) {
        pdf = new File(pdfPath);
       identity = identityPath;
    }

    public void loadIdentities(String identityPath) {

    }

    public String process() throws IOException {
        return process(false);
    }

    public String process(boolean defaultToXML) throws IOException {

        XMLStyleCodeContentHandler handler = new XMLStyleCodeContentHandler(identity, defaultToXML);
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(pdf) ){
            //parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputStream, handler, metadata,pcontext);

            //getting the content of the document
            //System.out.println("Contents of the PDF :" + handler.toString());

            //getting metadata of the document
            //System.out.println("Metadata of the PDF:");
            String[] metadataNames = metadata.names();

//            for(String name : metadataNames) {
//                System.out.println(name+ " : " + metadata.get(name));
//            }
        } catch (TikaException | SAXException e) {
            //e.printStackTrace();
            System.out.println("ERROR: Tika process: " + e.getMessage());
            e.printStackTrace();
        }

        return handler.toString();
    }

    private void preProcess(){

    }

    private void postProcess() {

    }
}
