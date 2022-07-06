package Identity.Checker;

public class Checker implements IdentityChecker{
    private String pattern;

    public Checker(String pattern){
        this.pattern = pattern;
    }

    @Override
    public boolean check(String context) {
        return false;
    }
}
