package Identity.checker;

import Identity.annotation.Checker;

import java.util.Map;

@Checker(name = "HASH-KEY")
public class HashKeywordChecker implements IdentityChecker {

    private final String keyword;

    protected HashKeywordChecker(Map<String, Object> config) {
        this.keyword = (String) config.get("data");
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
