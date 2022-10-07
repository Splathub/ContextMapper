import identity.entity.HashedTextToAction;
import identity.entity.ModeledTextToAction;
import identityMaster.IdentityMasterBuilder;
import identityMaster.ModeledTransformer;
import identityMaster.IdentityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ContextMapperBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ContextMapperBuilder.class);
    private final String TEXT_TO_ACTION_PATH = "identity/textToAction/";

    private IdentityMasterBuilder identityMasterBuilder = new IdentityMasterBuilder();
    private final ModeledTransformer modeledTransformer = ModeledTransformer.getInstance();
    private IdentityBuilder identityBuilder;


    public ContextMapperBuilder() throws IOException {
        identityBuilder = new IdentityBuilder(identityMasterBuilder.getIdentityMaster());
    }

    public ContextMapperBuilder(String name) throws IOException {
        loadIdentityMaster(name);
    }

    public void loadIdentityMaster(String name) throws IOException {
        identityMasterBuilder = new IdentityMasterBuilder(name);
        identityBuilder = new IdentityBuilder(identityMasterBuilder.getIdentityMaster());
    }

    // Only HTML input to learn from
    public void includeInToIdentityMaster(File file) throws IOException {
        if (file.isFile()) {
            identityMasterBuilder.build(file);
        }
        else if (file.isDirectory()) {
            identityMasterBuilder.buildFromDirectory(file);
        }
        else {
            LOG.error("Invalid file input, not a file or directory");
        }
    }

    // uses IdentityMaster and makes training data and model
    public String buildModeledIdentity() throws IOException {
        File catFile = identityBuilder.syncedCATFileWithRootIdentityMap();
        File modelFile = modeledTransformer.train(catFile);

        ModeledTextToAction modeledTextToAction = new ModeledTextToAction();
        modeledTextToAction.setModelPath(modelFile.getPath());
        modeledTextToAction.setIdentityMap(identityBuilder.getIdentityMap());

        String path = TEXT_TO_ACTION_PATH + modelFile.getName().split("\\.")[0]+".modeled.yaml";

        Yaml yaml = new Yaml();
        Writer writer = new FileWriter(path);
        yaml.dump(modeledTextToAction, writer);

        return path;
    }

    // uses IdentityMaster and builds hashed with selective algroithm
    public String buildHashedIdentity() throws IOException {
        identityBuilder.buildRootIdentityMap();

        HashedTextToAction hashedTextToAction = new HashedTextToAction();
        //hashedTextToAction.setContentMap();//TODO: figure out hashing order adn key checjking
        hashedTextToAction.setKeyGenerator(identityMasterBuilder.getIdentityMaster().getKeyGenerator());
        hashedTextToAction.setIdentityMap(identityBuilder.buildRootIdentityMap());

        String path = TEXT_TO_ACTION_PATH + identityMasterBuilder.getIdentityMaster().getName().split("\\.")[0]+".hashed.yaml";

        Yaml yaml = new Yaml();
        Writer writer = new FileWriter(path);
        yaml.dump(hashedTextToAction, writer);

        return path;
    }

}
