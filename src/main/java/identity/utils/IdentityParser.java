package identity.utils;

import identity.entity.CheckedIdentity;
import identity.action.BaseIdentityAction;
import identity.entity.RootIdentityContentHandler;
import java.util.AbstractMap.SimpleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;

/**
 * Parses identities definition yaml files to Identity object.
 */
public class IdentityParser {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityParser.class);

    public static CheckedIdentity[] parseIdentities(String path) throws IOException {
        CheckedIdentity[] identities;
        LOG.info("Parsing " + path);
        try (FileInputStream inputStream = new FileInputStream(path)) {
            Yaml yaml = new Yaml();
            ArrayList<Map<String, Object>> identitiesData = yaml.load(inputStream);

            identities = new CheckedIdentity[identitiesData.size()];

            for (int i = 0; i < identities.length ; i++){
                identities[i] = IdentityFactory.createIdentity(identitiesData.get(i));
            }
        }

        LOG.info("Successful Parsed Identities");
        return identities;
    }

    public static RootIdentityContentHandler parseRoot(String path, String partsPath) throws IOException {

        RootIdentityContentHandler root = null;
        LOG.info("Parsing " + path);
        try (FileInputStream inputStream = new FileInputStream(path)) {
            Constructor constructor = new Constructor(RootIdentityContentHandler.class);

            TypeDescription configDesc = new TypeDescription(SimpleEntry.class);
            configDesc.addPropertyParameters("defaultAction", BaseIdentityAction.class);

            constructor.addTypeDescription(configDesc);

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);
            root = IdentityFactory.createRootIdentity(data, partsPath);
        }

        LOG.info("Successful Parsed RootIdentity");
        return root;
    }

}
