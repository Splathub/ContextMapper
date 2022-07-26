package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;

public class FalseChecker extends AbstractIdentityChecker {

    public FalseChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return false;
    }
}
