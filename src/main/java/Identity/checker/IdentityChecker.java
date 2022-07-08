package Identity.checker;

public interface IdentityChecker {

    /**
     * Checks if matches the
     * @return true is the context matches the relevant text;
     */
    boolean check(char[] context, int start, int length);

}
