package identity.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class CustomWriter extends Writer {
    private final static String KEY_WORD = "<div class=\"page\">";
    LinkedList<String> matchers = new LinkedList<String>();
    StringBuffer stringBuffer = new StringBuffer();
    int mismatchCount = 0;
    int currentIndex = 0;
    LinkedHashSet<String> matches = new LinkedHashSet<>();

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        System.out.print(new String(cbuf, off, len));
        this.stringBuffer.append(new String(cbuf, off, len));
        this.runMatch();
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    private void runMatch() {
        for (; currentIndex + KEY_WORD.length() < stringBuffer.length(); currentIndex++) {

            String relevantSubstring = stringBuffer.substring(currentIndex, currentIndex + KEY_WORD.length());

            if (KEY_WORD.equals(relevantSubstring)) {
                save(15);
            }

        }
    }

    private void save(int sampleOffset) {
        int start = currentIndex - sampleOffset;
        start = Math.max(0, start);

        String matcher = stringBuffer.substring(start, start + sampleOffset );

        if (matches.contains(matcher)) {
            save(sampleOffset + 5);
            return;
        }

        matches.add(matcher);
    }

    @Override
    public String toString() {
        return matchers.toString();
    }
}
