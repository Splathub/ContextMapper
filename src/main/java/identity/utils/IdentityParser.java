package identity.utils;

import identity.entity.HashedTextToAction;
import identity.entity.ModeledTextToAction;

import java.io.File;

import identity.entity.TextToAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Parses identities definition yaml files to Identity object.
 */
public class IdentityParser {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityParser.class);

    public static TextToAction parseTTA(File file) {
        String type = file.getName().split("\\.")[1];
        Yaml yaml = new Yaml();

        if (type.equalsIgnoreCase("modeled")) {
            ModeledTextToAction textToAction = null;
            try (FileInputStream inputStream = new FileInputStream(file)) {
                textToAction = yaml.load(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return textToAction;
        }
        else if (type.equalsIgnoreCase("hashed")) {
            HashedTextToAction textToAction = null;
            try (FileInputStream inputStream = new FileInputStream(file)) {
                textToAction = yaml.load(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return textToAction;
        }
        else {
            throw new RuntimeException("Invalid file name");
        }
    }

}
