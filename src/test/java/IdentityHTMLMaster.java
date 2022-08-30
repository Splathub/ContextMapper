import java.io.File;
import java.io.IOException;

public class IdentityHTMLMaster {

    final static String SAMPLE_HTML= "src/test/resources/samples/sample18.htm";
    final static String SAMPLE_DIR_HTML= "C:\\Users\\holyg\\Desktop\\Repos\\gray";

    public static void main (String[] args) throws IOException {


        ContextMapperBuilder contextMapperBuilder = new ContextMapperBuilder();
        contextMapperBuilder.includeInToIdentityMaster(new File(SAMPLE_DIR_HTML));

        //contextMapperBuilder.writeRootIdentityMap();
        //System.out.println("Created IdentityMap");

        String ttaPath = contextMapperBuilder.buildModeledIdentity(); //TODO: fix for model to have scyn ID keys with map (empty are skiped out of sync)
        System.out.println("Created IdentityModel TTA: " + ttaPath);


        // -----------------------------------------------
        //String ttaPath = "identity/textToAction/tempIdentityMaster.modeled.yaml";

        ContextMapper.transformFileByTTA(SAMPLE_HTML, "identity/tempOutput.HTML", ttaPath);
    }
}
