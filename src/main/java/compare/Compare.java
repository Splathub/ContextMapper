package compare;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.nodes.Document;

public class Compare {

//  private static final Logger LOG = LoggerFactory.getLogger(Compare.class);

  public static void main(String[] args) throws IOException, TikaException, SAXException {
    compare(new FileInputStream("src/test/resources/samples/sample3.pdf"),
            new FileInputStream("src/test/resources/samples/sample3.html"),
            new FileOutputStream("src/main/resources/report/sample3.html", false));
  }

  public static void compare(InputStream pdfStream, InputStream htmlStream, OutputStream out) throws TikaException, IOException, SAXException {
    DiffMatchPatch dmp = new DiffMatchPatch();
    LinkedList<DiffMatchPatch.Diff> diffs = dmp.diffMain(pdfToString(pdfStream), htmlToString(htmlStream));

    try (Writer w = new OutputStreamWriter(out, "UTF-8")) {
      w.write(toHTML(diffs));
    }
  }

  protected static String toHTML(LinkedList<DiffMatchPatch.Diff> diffs){
    StringBuilder first = new StringBuilder();
    StringBuilder second = new StringBuilder();
    Iterator iterator = diffs.iterator();

    while(iterator.hasNext()) {
      DiffMatchPatch.Diff diff = (DiffMatchPatch.Diff) iterator.next();
      String text = diff.text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "&para;<br>");

      switch (diff.operation) {
        case INSERT:
          second.append("<span class='appended'>").append(text).append("</span>");
          break;
        case DELETE:
          first.append("<span class='deleted'>").append(text).append("</span>");
          break;
        case EQUAL:
          first.append("<span>").append(text).append("</span>");
          second.append("<span>").append(text).append("</span>");
      }
    }

    return mergeTemplate(first.toString(), second.toString(),diffs.size());
  }

  protected static String mergeTemplate(String first, String second, int diffNumber) {
    try {
      String content = Files.readString(Path.of("src/main/resources/templates/compare.html"), StandardCharsets.UTF_8);
      return String.format(content, first, second, diffNumber);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected static String pdfToString(InputStream input) throws TikaException, IOException, SAXException {
    Metadata metadata = new Metadata();
    ParseContext parseContext = new ParseContext();
    AutoDetectParser parser = new AutoDetectParser();
    BodyContentHandler handler = new BodyContentHandler();

    parser.parse(input, handler, metadata, parseContext);

    return handler.toString();
  }

  protected static String htmlToString(InputStream input) throws IOException, TikaException, SAXException {
    Metadata metadata = new Metadata();
    ParseContext parseContext = new ParseContext();
    AutoDetectParser parser = new AutoDetectParser();
    BodyContentHandler handler = new BodyContentHandler();

    parser.parse(input, handler, metadata, parseContext);

    return handler.toString();
  }
}
