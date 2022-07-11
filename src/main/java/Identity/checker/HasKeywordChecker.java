package Identity.checker;

import Identity.exception.ParameterException;

import java.util.Map;

public class HasKeywordChecker extends AbstractIdentityChecker {

    private final String keyword;

    public HasKeywordChecker(Map<String, Object> data) {
        super(data);
        this.keyword = (String) data.get("keyword");

        if (this.keyword == null) {
            throw new ParameterException("HasKeywordChecker Key: 'keyword' is invalid");
        }
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        int j;
        for (int i=start; i<start+length; i++) {
            if (context[i] == keyword.charAt(0)) {
                j=0;
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

    @Override
    public boolean check(String str) {
        return check(str.toCharArray(), 0, str.length()-1);
    }
}
