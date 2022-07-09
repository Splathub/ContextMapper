package Identity.checker;

import java.util.Map;

public class MatchingTextChecker extends AbstractIdentityChecker {

    private final String text;

    public MatchingTextChecker(Map<String, Object> data) {
        super(data);
        this.text = (String) data.get("text");
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        int offset = 0;
        for (int wordStart = start; wordStart < start + length; wordStart++) {
            if (context[wordStart] != text.charAt(offset++)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean check(String str) {
        return str.equals(text);
    }

}

