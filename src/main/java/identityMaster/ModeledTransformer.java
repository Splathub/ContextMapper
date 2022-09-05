package identityMaster;

import constants.Constants;
import identityMaster.entity.IdentityMaster;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.doccat.NGramFeatureGenerator;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ModeledTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(ModeledTransformer.class);
    private static ModeledTransformer modeledTransformer = null;
    private DoccatModel model;
    private static Tokenizer tokenizer;
    private static POSTaggerME posTagger; //parts-of-speech

    private ModeledTransformer(){
        zeroPrep();
    }

    public static ModeledTransformer getInstance() {
        if (modeledTransformer == null) {
            modeledTransformer = new ModeledTransformer();
        }
        return modeledTransformer;
    }

    private static void zeroPrep() {
        try {
            TokenizerModel tokenModel = new TokenizerModel( new File("models"+ File.separator+"en-token.bin"));
            tokenizer = new TokenizerME(tokenModel);

            POSModel posModel = new POSModel( new File("models"+File.separator+"en-pos-maxent.bin"));
            posTagger = new POSTaggerME(posModel);

             /*
                ChunkerModel chunkerModel = new ChunkerModel( new File("models"+File.separator+"en-chunker.bin"));
                ChunkerME chunker = new ChunkerME(chunkerModel);
                // chunking the given sentence : chunking requires sentence to be tokenized and pos tagged
                String[] chunks = chunker.chunk(textTokens,tags);
              */
        } catch (FileNotFoundException e){
            LOG.error("File not found: " + e.getMessage());
        } catch (IOException e) {
            LOG.error("Unknown exception: " + e.getMessage());
        }
    }

    public static Map<String, String[]> buildSelector(String text, List<String[]> insertsInOrder) {
        Map<String, String[]> regexInserts = new HashMap<>();
        if (insertsInOrder != null && !insertsInOrder.isEmpty()) {
            String textTokens[] = tokenizer.tokenize(text);
            String tags[] = posTagger.tag(textTokens);

            Map<String, Integer> POSCount = new HashMap<>();
            ListIterator iterator = insertsInOrder.listIterator();

            for(int i=0; i < textTokens.length; i++){
                if (textTokens[i].contains(Constants.SPECIAL_NOTE_PREFIX)) {
                    i++;
                    POSCount.compute(tags[i], (k, v) -> v==null? v=1 :v++ );
                    if (iterator.hasNext()) {
                        regexInserts.put(tags[i] + "-" + POSCount.get(tags[i]), (String[]) iterator.next());    //TODO: need usable find algorithm
                    }
                    else {
                        LOG.error("Incomplete: Invalid amount of Inserts, more Pins found than Inserts");
                        break;
                    }
                }
                else if (!textTokens[i].contains(Constants.SPECIAL_NOTE_POSTFIX)) {
                    POSCount.compute(tags[i], (k, v) -> v == null ? v = 1 : v++);
                }
            }

            if (iterator.hasNext()) {
                LOG.error("Incomplete: Invalid amount of Inserts, more Inserts than Pins found");
            }
        }
        return regexInserts;
    }

    public void load(File modelFile) throws IOException {
        model = new DoccatModel(modelFile);
    }

    public String transform(IdentityMaster master) {
        if (model == null) {
            throw new RuntimeException("Invalid model");
        }
        return "";
    }

    public File train(File trainFile) {
        File modelFile = new File(Constants.IDENTITY_MODELS_PATH + trainFile.getName().split("\\.")[0] + ".model");

        try(ObjectStream<String> lineStream = new PlainTextByLineStream(
                new MarkableFileInputStreamFactory(trainFile), StandardCharsets.UTF_8)) {

            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);


            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 200+"");
            params.put(TrainingParameters.CUTOFF_PARAM, 0+"");
            params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

            // feature generators - N-gram feature generators
            FeatureGenerator[] featureGenerators = { new NGramFeatureGenerator(1,1),
                    new NGramFeatureGenerator(2,3) };
            DoccatFactory factory = new DoccatFactory(featureGenerators);


            model = DocumentCategorizerME.train("en", sampleStream, params, factory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
            model.serialize(modelOut);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return modelFile;
    }




}
