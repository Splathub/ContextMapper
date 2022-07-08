package Identity.utils;

import Identity.Identity;
import Identity.action.Action;
import Identity.checker.CheckerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Parses identities definition yaml files to Identity object;
 */
public class IdentityParser {

    /**
     * Parses identities definition yaml files to Identity object;
     *
     * @param path path to the file to parse
     * @return an array of {@link Identity}
     * @throws IOException if file not found;
     */
    public static Identity[] parse(String path) throws IOException {
        Yaml yaml = new Yaml();

        // Creating file stream
        File initialFile = new File(path);
        InputStream inputStream = Files.newInputStream(initialFile.toPath());

        // Load raw yaml data as a list of hashmaps
        ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

        // Creation of empty identity array to store identities
        Identity[] identities = new Identity[identitiesData.size()];

        for ( int i = 0; i < identities.length ; i ++ ){
            Map<String, Object> identityData = identitiesData.get(i);

            identities[i] = new Identity(
                    CheckerFactory.build((Map<String, Object>) identityData.get("checker")),
                    new Action((String) identityData.get("actions")),
                    (String) identityData.get("name"),
                    (Map<String, Object>) identityData.get("args")
            );
        }

        return identities;
    }

}
