package Identity.checker;

public class MatchingTextChecker implements IdentityChecker{

    private final String text;

    public MatchingTextChecker(String text) {
        this.text=  text;
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
