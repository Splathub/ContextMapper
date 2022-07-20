
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

public class ContextMapperProcess {

    final static String SAMPLE_PDF = "src/test/resources/samples/sample2.pdf";
    final static String SAMPLE_YAML = "src/test/resources/identities/sample1.yml";
    final static String BROKEN_YAML = "src/test/resources/identities/BrokenSample1.yml";

    final static String SAMPLE_DOCX = "src/test/resources/samples/sample1.docx";
    final static String ROOT_IDENTITY = "src/test/resources/identities/root/Sample1Docx.yml";

    public static void main (String[] args) throws IOException {
        ContextMapper cm;

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "PDF/DOCX files (only docx now)", "pdf", "docx");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            cm = new ContextMapper(chooser.getSelectedFile().getAbsolutePath(), ROOT_IDENTITY);
        }
        else {
            cm = new ContextMapper(SAMPLE_DOCX, ROOT_IDENTITY);
        }

        cm.setPartPath("src/test/resources/identities/parts/");
        System.out.println("XML:\n"+cm.processToXML() + "\n---------------------------------------\n");
        System.out.println(cm.process());
    }

}
