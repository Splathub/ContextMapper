import java.io.IOException;

public class ContextMapperProcess {


    final static String SAMPLE_PDF = "src/test/resources/samples/sample2.pdf";

    public static void main (String[] args) {
        ContextMapper cm = new ContextMapper(SAMPLE_PDF, null);

        System.out.println(cm.process());
    }
}
