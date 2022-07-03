import Identity.Identity;
import Identity.Passive.PassiveAction;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ContextMapper {

    File pdf;
    File identity;

    StringBuffer buffer; //OUTPUT Obj
    List<Identity> identities;
    List<PassiveAction> passiveActions;

    //onPoint;
    //lastPoint;


    //Meda .. maybe used..and moved to an object as a ref.
    int pageCount;
    int sectionCount;
    int tableCount;
    int imageCount;
    int hyperlinkCount;

    public ContextMapper(String pdfPath, String identityPath) {
        pdf = new File(pdfPath);
        identity = new File(identityPath);
    }

    public void loadIdentities(String identityPath) {

    }

    public String process() throws IOException {

        try( PDDocument document = PDDocument.load(pdf)){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }

    }

    private void preProcess(){

    }

    private void postProcess() {

    }
}
