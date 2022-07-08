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
        int j=0;
        for (int i=start; i<start+length; i++) {
            if (context[i] != text.charAt(j++)) {
                return false;
            }
        }
        return true;
    }


}
