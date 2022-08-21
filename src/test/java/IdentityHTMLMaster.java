import identityMaster.IdentityMasterBuilder;
import identityMaster.RootIdentityBuilder;
import identityMaster.entity.IdentityMaster;

import java.io.File;
import java.io.IOException;

public class IdentityHTMLMaster {

    final static String SAMPLE_HTML= "src/test/resources/samples/sample18.htm";
    final static String SAMPLE_DIR_HTML= "C:\\Users\\holyg\\Desktop\\Repos\\gray";

    public static void main (String[] args) throws IOException {
        IdentityMasterBuilder builder = new IdentityMasterBuilder();
        //IdentityMaster master = builder.build(new File(SAMPLE_HTML));
        IdentityMaster master = builder.buildFromDirectory(new File(SAMPLE_DIR_HTML));

        RootIdentityBuilder rootBuilder = new RootIdentityBuilder(master);
        System.out.println("Created IdentityMap : "+rootBuilder.buildRootIdentityMap());
        System.out.println("Created IdentityModel : "+rootBuilder.createRootIdentityModel());

    }
}
