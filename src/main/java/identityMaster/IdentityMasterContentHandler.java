package identityMaster;

import identityMaster.entity.IdentityMaster;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;

import java.io.File;

public class IdentityMasterContentHandler {


    public static String build(File toXML, File html) {
        String identityMasterPath = "";
        IdentityMaster identityMaster = new IdentityMaster(identityMasterPath);

        TeeContentHandler teeContentHandler = new TeeContentHandler(
                new ToXMLContentHandler()
        );

        return identityMasterPath;
    }
}
