import java.io.IOException;

public class ContextMapperProcess {

    final static String SAMPLE_PDF = "src/test/resources/samples/sample2.pdf";
    final static String SAMPLE_YAML = "src/test/resources/identities/sample1.yml";
    final static String BROKEN_YAML = "src/test/resources/identities/BrokenSample1.yml";

    public static void main (String[] args) throws IOException {
       ContextMapper cm = new ContextMapper(SAMPLE_PDF, SAMPLE_YAML);
       System.out.println(cm.process());
    }
}
