import identity.entity.GeneralContentHandler;
import identity.entity.TextToAction;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.ToXMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static identity.utils.IdentityParser.parseTTA;

public class ContextMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ContextMapper.class);

    File pdf;
    String identity;


    public static void transformFileByTTA(String inputFile, String outputFile, String ttaPath) {
        File ttaFile = new File(ttaPath);
        if (!ttaFile.isFile() || !ttaFile.canRead()) {
            throw new RuntimeException("Can find or read TTA file " + ttaPath);
        }

        LOG.info("Loading TTA");
        TextToAction tta = parseTTA(ttaFile);

        try (OutputStream stream = new FileOutputStream(outputFile);
             Writer writer = new OutputStreamWriter(stream, "UTF-8")) {

            GeneralContentHandler generalContentHandler = new GeneralContentHandler(tta, writer);
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            try (FileInputStream inputStream = new FileInputStream(inputFile)) {
                AutoDetectParser parser = new AutoDetectParser();
                parser.parse(inputStream, generalContentHandler, metadata, context);
            } catch (TikaException | SAXException e) {
                LOG.error("Tika process: " + e.getMessage());
                e.printStackTrace();
            }

            LOG.info("Finished transforming file to: " + outputFile);
        }
        catch (Exception e) {
            LOG.error("Failed to write output: " + e.getMessage());
        }
    }

    public ContextMapper(String pdfPath, String identityPath) {
        pdf = new File(pdfPath);
       identity = identityPath;
    }

    public String processToXML() throws IOException {

        ToXMLContentHandler handler = new ToXMLContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(pdf) ){
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);
        } catch (TikaException | SAXException e) {
            LOG.error("Tika XML process: " + e.getMessage());
        }

        return handler.toString();
    }

    public static String processTo(ContentHandler handler, File file) throws IOException {
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try ( FileInputStream inputStream = new FileInputStream(file) ){
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, context);
        } catch (TikaException | SAXException e) {
            LOG.error("Tika XML process: " + e.getMessage());
        }

        return handler.toString();
    }

}
