package Identity.checker;

import java.util.Map;

public interface IdentityChecker {

    /**
     * Checks if matches the
     * @return true is the context matches the relevant text;
     */
    boolean check(char[] context, int start, int length);

    boolean check(String str);

}
