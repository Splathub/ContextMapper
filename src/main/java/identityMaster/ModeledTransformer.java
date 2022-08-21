package identityMaster;

import identityMaster.RootIdentityBuilder;
import identityMaster.entity.IdentityMaster;
import opennlp.tools.doccat.*;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModeledTransformer {

    private final String MODEL_PATH = "identity/models";
    private DoccatModel model;


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
        File modelFile = new File(MODEL_PATH + trainFile.getName() + ".model");

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
