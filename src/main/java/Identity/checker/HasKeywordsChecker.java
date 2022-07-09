package Identity.checker;

import java.util.HashMap;
import java.util.Map;

public class HasKeywordsChecker extends AbstractIdentityChecker {

    private final HashMap<String, Integer> keywords; // Use Trie
    private final int longestWord;

    public HasKeywordsChecker(Map<String, Object> data) {
        super(data);
        for (String str : (String[]) data.get("keyword") ) {

        }
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

    private class Trie {

    }

}
