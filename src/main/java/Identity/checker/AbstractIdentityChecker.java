package Identity.checker;

import java.util.Map;

public abstract class AbstractIdentityChecker implements IdentityChecker{

    private final Map<String, Object> data;

    public AbstractIdentityChecker(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public boolean check(char[] context, int start, int length) {
        return false;
    }
}
