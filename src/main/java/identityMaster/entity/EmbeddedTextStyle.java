package identityMaster.entity;

import constants.Constants;
import identityMaster.ModeledTransformer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class EmbeddedTextStyle implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedTextStyle.class);
    private Map<String, String[]> regexInserts;
    private final ModeledTransformer modeledTransformer;

    public EmbeddedTextStyle(ModeledTransformer modeledTransformer) {
        this.modeledTransformer = modeledTransformer;
    }

    //TODO: For upgrade, could do other text effect here instead of template order insert
    public void buildSelector(String text, List<String[]> insertsInOrder) {
        regexInserts = ModeledTransformer.buildSelector(text, insertsInOrder);
    }

    public void run(StringBuilder sb) {
        if (regexInserts != null && !regexInserts.isEmpty() && sb != null) {
            //TODO: use regex and insert tags
        }
    }

    public Map<String, String[]> getRegexInserts() {
        return regexInserts;
    }

    public void setRegexInserts(Map<String, String[]> regexInserts) {
        this.regexInserts = regexInserts;
    }
}
