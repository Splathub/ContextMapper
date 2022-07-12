package Identity.utils;

import Identity.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;

/**
 * Parses identities definition yaml files to Identity object.
 */
public class IdentityParser {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityParser.class);

    public static Identity[] parse(String path) throws IOException {
        Identity[] identities;

        try (FileInputStream inputStream = new FileInputStream(path)) {
            Yaml yaml = new Yaml();
            ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

            identities = new Identity[identitiesData.size()];

            for (int i = 0; i < identities.length ; i++){
                identities[i] = IdentityFactory.createIdentity(identitiesData.get(i));
            }
        }

        LOG.info("Successful Parsed Identity");
        return identities;
    }

}
