package Identity.checker;

public class Checker implements IdentityChecker{
    private String pattern;

    public Checker(String pattern){
        this.pattern = pattern;
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        return false;
    }
}
