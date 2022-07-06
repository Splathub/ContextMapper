package Identity.utils;

import Identity.Identity;
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

    public static Identity[] parse(String path) throws IOException {
        Yaml yaml = new Yaml();

        File initialFile = new File(path);
        InputStream inputStream = Files.newInputStream(initialFile.toPath());

        ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

        ArrayList<Identity> identities = new ArrayList<>(identitiesData.size());

        for (Map<String, Object> identityData : identitiesData) {

            Map<String, String> args = (Map<String, String>) identityData.get("args");

            identities.add(new Identity(
                    new IdentityChecker.CheckerC((String) identityData.get("checker")),
                    new Identity.Action.ActionC((String) identityData.get("actions")),
                    (String) identityData.get("name"),
                    args
            ));
        }

        return identities;
    }

}
