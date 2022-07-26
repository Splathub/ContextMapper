package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;

public class HasKeywordChecker extends AbstractIdentityChecker {

    public HasKeywordChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return sb.indexOf( (String) getData("keyword")) > -1;
    }
}
