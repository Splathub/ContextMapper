package identity.entity;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizer;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class ModeledTextToAction implements TextToAction, Serializable {

    private String modelPath;
    private Map<String, Identity> identityMap;
    private DocumentCategorizer categorizer;


    @Override
    public Identity identify(StringBuilder sb) {
        // TODO: assumes loaded path
        String[] cleanText = sb.toString().toLowerCase().split(" ");
        double[] outcomes = categorizer.categorize(cleanText);
        String category = categorizer.getBestCategory(outcomes);

        return identityMap.get(category);
    }


    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) throws IOException {
        this.modelPath = modelPath;
        categorizer = new DocumentCategorizerME(
                new DoccatModel(new File(modelPath)));
    }

    public Map<String, Identity> getIdentityMap() {
        return identityMap;
    }

    public void setIdentityMap(Map<String, Identity> identityMap) {
        this.identityMap = identityMap;
    }
}
