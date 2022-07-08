package Identity.checker;

import Identity.annotation.Checker;

import java.util.Map;

@Checker(name = "MATCHING-TEXT")
public class MatchingTextChecker implements IdentityChecker {

    private final String text;

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

    public  MatchingTextChecker(Map<String, Object> config) {
        this.text = (String) config.get("data");
    }

}
