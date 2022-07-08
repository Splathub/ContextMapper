package Identity.checker;

public class MatchingTextChecker implements IdentityChecker {

    private final String text;

    protected MatchingTextChecker(String text) {
        this.text = text;
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

}
