package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;

public class TrueChecker extends AbstractIdentityChecker {

    public TrueChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return true;
    }
}
