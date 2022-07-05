package Identity;

import Identity.Action.Action;
import Identity.Checker.Checker;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Parses identities definition yaml files to Identity object;
 */
public class IdentityParser {

    public static ArrayList<Identity> parse(String path) throws IOException {
        Yaml yaml = new Yaml();

        File initialFile = new File(path);
        InputStream inputStream = Files.newInputStream(initialFile.toPath());

        ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

        ArrayList<Identity> identities = new ArrayList<Identity>();

        for (Map<String, Object> identityData : identitiesData) {

            ArrayList<String> args = (ArrayList<String>) identityData.get("args");

            identities.add(new Identity(
                    new Checker((String) identityData.get("checker")),
                    new Action((String) identityData.get("actions")),
                    (String) identityData.get("name"),
                    new String[]{}
            ));
        }

        return identities;
    }

}
