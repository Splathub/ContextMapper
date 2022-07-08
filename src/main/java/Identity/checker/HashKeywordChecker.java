package Identity.checker;

public class HashKeywordChecker implements IdentityChecker {

    private final String keyword;

    protected HashKeywordChecker(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        int offset;

        for (int wordStart = start; wordStart < start + length; wordStart++) {

            offset = 0;

            while (context[wordStart + offset] == keyword.charAt(offset)
                    && wordStart + offset < context.length ) {

                offset++;

                if (offset == keyword.length()) {
                    return true;
                }
            }

        }

        return false;
    }
}
