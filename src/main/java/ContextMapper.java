import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ContextMapper {

    File pdf;
    File identity;

    //StringBuffer buffer; //OUTPUT Obj
    //List<Identity> identities;
    //List<PassiveAction> passiveActions;



    public ContextMapper(String pdfPath, String identityPath) {
        pdf = new File(pdfPath);
       //identity = new File(identityPath);
    }

    public void loadIdentities(String identityPath) {

    }

    public String process() {

        ContentHandler handler = new XMLStyleCodeContentHandler(null);
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();

        try ( FileInputStream inputstream = new FileInputStream(pdf) ){
            //parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata,pcontext);

            //getting the content of the document
            System.out.println("Contents of the PDF :" + handler.toString());

            //getting metadata of the document
            System.out.println("Metadata of the PDF:");
            String[] metadataNames = metadata.names();

            for(String name : metadataNames) {
                System.out.println(name+ " : " + metadata.get(name));
            }
        } catch (IOException | TikaException | SAXException e) {
            //e.printStackTrace();
            System.out.println("ERROR: Tika process: " + e.getMessage());
        }

        return handler.toString();
    }

    private void preProcess(){

    }

    private void postProcess() {

    }
}
