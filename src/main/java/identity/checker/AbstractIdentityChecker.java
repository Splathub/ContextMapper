package identity.checker;

import identity.exception.ParameterException;

import java.util.Map;

public abstract class AbstractIdentityChecker implements IdentityChecker{

    private final Map<String, Object> data;

    public AbstractIdentityChecker(Map<String, Object> data) {
        this.data = data;
    }

    public Object getData(String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new ParameterException("Identity Checker missing parameter : " + key);
        }
        return value;
    }
}
