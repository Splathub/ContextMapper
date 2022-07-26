package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;

public class ToggleChecker extends AbstractIdentityChecker {

    private boolean visited;

    public ToggleChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        if (!visited) {
            visited = true;
            return true;
        }
        return false;
    }
}
