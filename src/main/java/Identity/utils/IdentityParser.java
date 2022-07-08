package Identity.utils;

import Identity.Identity;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Parses identities definition yaml files to Identity object;
 */
public class IdentityParser {

    public static Identity[] parse(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Yaml yaml = new Yaml();

        File initialFile = new File(path);
        InputStream inputStream = Files.newInputStream(initialFile.toPath());

        ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

        Identity[] identities = new Identity[identitiesData.size()];

        for ( int i = 0; i < identities.length ; i ++ ){
            Map<String, Object> identityData = identitiesData.get(i);
            identities[i] = IdentityFactory.createIdentity(identityData);
        }

        return identities;
    }

}
