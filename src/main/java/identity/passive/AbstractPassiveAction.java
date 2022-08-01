package identity.passive;

import identity.exception.ParameterException;

import java.util.Map;

public abstract class AbstractPassiveAction implements PassiveAction {

    private final Map<String, Object> data;

    public AbstractPassiveAction(Map<String, Object> data) {
        this.data = data;
    }

    public Object getData(String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new ParameterException("Passive Action missing parameter : " + key);
        }
        return value;
    }

}
