package Identity.checker;

public class HasKeywordChecker implements IdentityChecker{

    private final String keyword;

    public HasKeywordChecker(String keyword) {
        this.keyword=  keyword;
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        int j;
        for (int i=start; i<start+length; i++) {
            if (context[i] == keyword.charAt(0)) {
                j=1;
                while (context[i+j] == keyword.charAt(j) && j < keyword.length()) {
                    j++;
                    if (j == keyword.length()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
