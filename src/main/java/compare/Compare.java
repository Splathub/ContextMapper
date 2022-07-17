package compare;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.jsoup.nodes.Document;

public class Compare {

  public static void main(String[] args){
    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain("pdfToString(pdfStream)","htmlToString(htmlStream)");

    System.out.println(dmp.diffPrettyHtml(diffs));
  }

  public static void compare(InputStream pdfStream, InputStream htmlStream) throws TikaException, IOException, SAXException {
    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(pdfToString(pdfStream), htmlToString(htmlStream));

    System.out.println(dmp.diffPrettyHtml(diffs));
  }

  protected static String pdfToString(InputStream input) throws TikaException, IOException, SAXException {
    Metadata metadata = new Metadata();
    ParseContext parseContext = new ParseContext();
    AutoDetectParser parser = new AutoDetectParser();
    BodyContentHandler handler = new BodyContentHandler();

    parser.parse(input, handler, metadata, parseContext);

    return handler.toString();
  }

  protected static String htmlToString(InputStream input) throws IOException {
    Document document = Jsoup.parse(input, "UTF-8", null);

    return document.text();
  }

}
