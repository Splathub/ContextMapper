import java.io.File;
import java.io.IOException;

public class IdentityHTMLMaster {

    final static String SAMPLE_HTML= "src/test/resources/samples/sample18.htm";
    final static String SAMPLE_PDF= "src/test/resources/samples/sample18.PDF";
    final static String SAMPLE_DIR_HTML= "C:\\Users\\holyg\\Desktop\\Repos\\gray";

    final static String SAMPLE_HTML_large_doc= "src/test/resources/samples/sample18.htm";
    final static String SAMPLE_PDF_large_doc= "C:\\Users\\holyg\\Desktop\\Clean.pdf";
    final static String SAMPLE_DIR_HTML_large_doc= "C:\\Users\\holyg\\Desktop\\Repos\\LargeDocBoarded_BlueTable";

    public static void main (String[] args) throws IOException {


        ContextMapperBuilder contextMapperBuilder = new ContextMapperBuilder();
        contextMapperBuilder.includeInToIdentityMaster(new File(SAMPLE_DIR_HTML_large_doc));

        //For hash
        //contextMapperBuilder.buildHashedIdentity();
        //System.out.println("Created IdentityMap");

        String ttaPath = contextMapperBuilder.buildModeledIdentity(); //TODO: fix for model to have scyn ID keys with map (empty are skiped out of sync)
        System.out.println("Created IdentityModel TTA: " + ttaPath);


        // -----------------------------------------------
        //String ttaPath = "identity/textToAction/tempIdentityMaster.modeled.yaml";

        ContextMapper.transformFileByTTA(SAMPLE_PDF_large_doc, "identity/tempOutput.HTML", ttaPath);
    }
}
