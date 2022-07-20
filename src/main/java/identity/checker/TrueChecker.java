package identity.checker;

import java.util.Map;

public class TrueChecker extends AbstractIdentityChecker {

    public TrueChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        return true;
    }

    @Override
    public boolean check(String str) {
        return true;
    }
}
