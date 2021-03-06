package identity.checker;

import identity.exception.ParameterException;

import java.util.Map;

public class MatchingTextChecker extends AbstractIdentityChecker {

    private final String text;

    public MatchingTextChecker(Map<String, Object> data) {
        super(data);
        this.text = (String) data.get("text");

        if (this.text == null) {
            throw new ParameterException("MatchingTextChecker Key: 'text' is invalid");
        }
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        if(length != text.length()) {
            return false;
        }

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
